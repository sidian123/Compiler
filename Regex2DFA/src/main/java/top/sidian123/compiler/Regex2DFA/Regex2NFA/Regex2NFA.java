package top.sidian123.compiler.Regex2DFA.Regex2NFA;

import lombok.extern.log4j.Log4j2;
import top.sidian123.compiler.Regex2DFA.entity.Element;
import top.sidian123.compiler.Regex2DFA.entity.ElementType;
import top.sidian123.compiler.Regex2DFA.entity.Graph;
import top.sidian123.compiler.Regex2DFA.entity.Node;
import top.sidian123.compiler.Regex2DFA.exception.SyntaxErrorException;

import java.lang.annotation.Target;
import java.util.*;

/**
 * @author Sidian.Luo
 * @date 2019/11/6 13:25
 */
@Log4j2
public class Regex2NFA {
    /**
     * 源
     */
    private LinkedList<Element> source=new LinkedList<>();
    /**
     * 栈,与正则操作的优先级有关
     */
    private LinkedList<Element> stack=new LinkedList<>();

    /**
     * RE的关键字, 其中""表示连接操作
     */
    private List<String> keywords= Arrays.asList("(",")","*","|","");

    /**
     * 规则比对表, 行列与字符的对应关系见文档
     */
    private String[][] ruleTable={
            {RuleType.ERROR,RuleType.COMPUTE,RuleType.IN,RuleType.COMPUTE_RIGHT,RuleType.IN,RuleType.ERROR},
            {RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR},
            {RuleType.ERROR,RuleType.COMPUTE,RuleType.COMPUTE,RuleType.COMPUTE_RIGHT,RuleType.IN,RuleType.COMPUTE},
            {RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR},
            {RuleType.COMPUTE,RuleType.COMPUTE,RuleType.COMPUTE,RuleType.COMPUTE_RIGHT,RuleType.COMPUTE,RuleType.COMPUTE},
            {RuleType.IN,RuleType.ERROR,RuleType.IN,RuleType.COMPUTE_RIGHT,RuleType.IN,RuleType.EXIT}
    };
    interface RuleType{
        String ERROR="error";//错误
        String COMPUTE="compute";//计算
        String COMPUTE_RIGHT="compute-right";//计算右边的
        String IN="in";//入栈
        String EXIT="exit";//结束
    }

    static class Operator extends Element{
        String op;
        Operator(){
            this.setType("op");
        }

        @Override
        public String toString() {
            return "Operator{" +
                    "op='" + op + '\'' +
                    '}';
        }
    }

    public Regex2NFA() {
    }

    /**
     * 预处理, 将字符串转化为图的集合
     * @param source 字符串
     */
    private void initSource(String source) {
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(isNonKeyword(chars[i])){//非关键字
                //建立图,并存入source
                addGraph(chars[i]);
                //预处理
                int next=i+1;
                if(next<chars.length){
                    if(isNonKeyword(chars[next])){//两个字符间有连接操作
                        addOperator("");
                    }
                    if(chars[next]=='('){
                        addOperator("");
                    }
                }
            }else{//关键字
                // 存入source
                addOperator(String.valueOf(chars[i]));
                //预处理
                int next=i+1;
                if(next<chars.length && isNonKeyword(chars[next])){
                    if(chars[i]=='*'){
                        addOperator("");
                    }
                    if(chars[i]==')'){
                        addOperator("");
                    }
                }
            }
        }
    }

    private void addGraph(char c){
        Node node = new Node();
        Node node1 = new Node();
        node.pointNode(node1,String.valueOf(c));
        Graph graph = new Graph(node, node1);
        graph.setMeta(String.valueOf(c));
        this.source.add(graph);
    }

    private void addOperator(String op){
        Operator operator = new Operator();
        operator.op=op;
        this.source.add(operator);
    }

    private boolean isNonKeyword(char c){
        return !keywords.contains(String.valueOf(c));
    }

    public Graph parse(String regex){
        //初始化
        initSource(regex);
        stack.clear();;
        //校验参数
        if(source.size()==0){
            return null;
        }
        //遍历解析
        Element curCharElement=null;//当前元素
        while ((curCharElement=source.poll())!=null){
            if(isOperand(curCharElement)){//操作数
                Operator operator =null;
                //从栈中取出左操作符
                String leftOp="空";
                operator = (Operator) stack.peekFirst();//肯定是操作符或空
                if(operator!=null){//存在操作符
                    leftOp=operator.op;
                }
                //获取右操作符
                String rightOp="空";
                operator=(Operator) source.peekFirst();
                if(operator!=null){//存在操作符
                    rightOp=((Operator)operator).op;
                }
                //判断使用何种操作
                String command = getCommand(leftOp, rightOp);//获取命令
                log.info(String.format("当前操作数:%s 左操作符:%s 右操作符:%s 执行操作:%s", curCharElement,leftOp,rightOp,command));
                Graph result = exec(command, (Graph) curCharElement);
                if(result!=null){//结束
                    return result;
                }
            }else{//操作符
                //入栈
                stack.push(curCharElement);
            }
        }
        return null;
    }

    /**
     * 执行命令
     * @param command 命令
     * @param operand 操作数
     * @return 图或null, 仅在EXIT时返回结果
     */
    private Graph exec(String command, Graph operand) {
        switch (command){
            case RuleType.ERROR:{
                throw new SyntaxErrorException("语法错误");
            }
            case RuleType.IN:{
                //入栈
                stack.push(operand);
                break;
            }
            case RuleType.EXIT:{
                return operand;
            }
            case RuleType.COMPUTE_RIGHT:{
                Operator operator = (Operator) source.poll();
                if(operator.op.equals("*")){
                    //闭包操作, 合并图
                    Graph graph=closure(operand);
                    //压入source
                    graph.setMeta(operand.getMeta()+"*");
                    source.push(graph);
                }
                break;
            }
            case RuleType.COMPUTE:{
                //取出操作符
                Operator operator = (Operator) stack.poll();
                switch (operator.op){
                    case "(":{
                        //去除左右括号
                        source.poll();
                        //入source
                        operand.setMeta("("+operand.getMeta()+")");
                        source.push(operand);
                        break;
                    }
                    case "|":{
                        //|操作, 合并图
                        Graph leftOperand=(Graph) stack.poll();
                        Graph graph=alternation(leftOperand,operand);
                        //压入source
                        graph.setMeta(leftOperand.getMeta()+"|"+operand.getMeta());
                        source.push(graph);
                        break;
                    }
                    case "":{
                        //连接操作, 合并图
                        Graph leftOperand=(Graph) stack.poll();
                        Graph graph=concatenation(leftOperand,(Graph) operand);
                        //压入source
                        graph.setMeta(leftOperand.getMeta()+""+operand.getMeta());
                        source.push(graph);
                        break;
                    }
                }
                break;
            }
        }
        return null;
    }

    /**
     * 正则的闭包操作
     */
    private Graph closure(Graph graph) {
        Node startNode=new Node();
        Node endNode=new Node();
        //连接
        startNode.pointNode(graph.getFirstStartNode(),"");
        startNode.pointNode(endNode,"");
        graph.getFirstEndNode().pointNode(graph.getFirstStartNode(),"");
        graph.getFirstEndNode().pointNode(endNode,"");
        //创建图
        return new Graph(startNode,endNode);
    }

    /**
     * 正则的连接操作
     */
    private Graph concatenation(Graph left, Graph right) {
        //连接
        left.getFirstEndNode().pointNode(right.getFirstStartNode(),"");
        //创建图
        return new Graph(left.getFirstStartNode(),right.getFirstEndNode());
    }

    /**
     * 正则表达的|操作
     * @param left 左图
     * @param right 右图
     * @return 合成图
     */
    private Graph alternation(Graph left, Graph right) {
        //开始节点
        Node startNode=new Node();
        startNode.pointNode(left.getFirstStartNode(),"");
        startNode.pointNode(right.getFirstStartNode(),"");
        //结束节点
        Node endNode=new Node();
        left.getFirstEndNode().pointNode(endNode,"");
        right.getFirstEndNode().pointNode(endNode,"");
        //创建图
        return new Graph(startNode,endNode);
    }


    private String getCommand(String leftOp,String rightOp){
        return ruleTable[getTableIndex(leftOp)][getTableIndex(rightOp)];
    }

    private int getTableIndex(String op){
        int i=0;
        switch (op){
            case "(":i=0;break;
            case ")":i=1;break;
            case "|":i=2;break;
            case "*":i=3;break;
            case "" :i=4;break;
            case "空":i=5;break;
        }
        return i;
    }

    /**
     * 是否为操作数
     */
    private boolean isOperand(Element element){
        return !element.getType().equals(ElementType.OP);
    }


}
