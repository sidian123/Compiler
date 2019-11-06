package top.sidian123.compiler.Regex2DFA.Regex2NFA;

import top.sidian123.compiler.Regex2DFA.entity.Element;
import top.sidian123.compiler.Regex2DFA.entity.ElementType;
import top.sidian123.compiler.Regex2DFA.entity.Graph;
import top.sidian123.compiler.Regex2DFA.entity.Node;

import java.util.*;

/**
 * @author Sidian.Luo
 * @date 2019/11/6 13:25
 */
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
            {RuleType.ERROR,RuleType.COMPUTE,RuleType.IN,RuleType.IN,RuleType.IN,RuleType.ERROR},
            {RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR},
            {RuleType.IN,RuleType.COMPUTE,RuleType.COMPUTE,RuleType.IN,RuleType.IN,RuleType.COMPUTE},
            {RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR,RuleType.ERROR},
            {RuleType.COMPUTE,RuleType.COMPUTE,RuleType.COMPUTE,RuleType.IN,RuleType.COMPUTE,RuleType.COMPUTE},
            {RuleType.IN,RuleType.ERROR,RuleType.IN,RuleType.IN,RuleType.IN,RuleType.IN}
    };
    interface RuleType{
        String ERROR="error";//错误
        String COMPUTE="compute";//计算
        String IN="in";//入栈
    }

    static class Operator extends Element{
        String op;
        Operator(){
            this.setType("op");
        }
    }

    public Regex2NFA(String source) {
        initSource(source);
    }

    /**
     * 将字符串转化为图的集合
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
        node.pointNode(node1,c);
        Graph graph = new Graph();
        graph.getStartNodes().add(node);
        graph.getEndNodes().add(node1);
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

    public void parse(){
        for (int i = 0; i < this.source.size(); i++) {
            if(isOperand(source.get(i))){//操作数
                String leftOp="空";
                Operator operator = (Operator) stack.peekFirst();//肯定是操作符或空
                if(operator!=null){//操作符
                    leftOp=operator.op;
                }
                String rightOp="空";
                if(i+1<source.size()){//肯定是操作符
                    rightOp=((Operator)source.peekFirst()).op;
                }
                String command = getCommand(leftOp, rightOp);
                exec(command);
            }else{//操作符
                //入栈
                stack.push(source.get(i));
            }
        }
    }

    private void exec(String command) {
        switch ()
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
