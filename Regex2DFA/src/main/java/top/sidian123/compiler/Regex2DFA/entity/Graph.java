package top.sidian123.compiler.Regex2DFA.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 状态图
 * @author Sidian.Luo
 * @date 2019/11/6 13:30
 */
public class Graph extends Element{
    /**
     * 开始节点
     */
    @Setter
    @Getter
    private List<Node> startNodes=new LinkedList<>();

    /**
     * 结束节点
     */
    @Setter
    @Getter
    private List<Node> endNodes=new LinkedList<>();

    public Graph(){
        this.setType(ElementType.GRAPH);
    }

    public Graph(Node StartNode,Node endNode){
        this();
        startNodes.add(StartNode);
        endNodes.add(endNode);
    }

    public Node getFirstStartNode(){
        return startNodes.get(0);
    }

    public Node getFirstEndNode(){
        return endNodes.get(0);
    }

    /**
     * 得到图中所有的输入(不包含空字符串)
     */
    public Set<String> getAllInput(){
        HashSet<String> set = new HashSet<>();
        traverse(node -> {
            node.getEdges().forEach(edge -> {
                if(!edge.getInput().isEmpty()){
                    set.add(edge.getInput());
                }
            });
        });
        return set;
    }

    /**
     * 遍历图的所有节点
     * @param consumer 回调
     */
    public void traverse(Consumer<Node> consumer){
        //初始化
        Set<Node> traversedNodeSet =new HashSet<>();
        //遍历
        startNodes.forEach(node -> {
            _traverse(node,consumer,traversedNodeSet);
        });
    }

    /**
     * 遍历所有未遍历的节点
     * @param node 节点
     * @param consumer 回调
     * @param traversedNodeSet 已遍历节点集合
     */
    private void _traverse(Node node, Consumer<Node> consumer, Set<Node> traversedNodeSet){
        if(!traversedNodeSet.contains(node)){//当前节点未被遍历
            //消费
            consumer.accept(node);
            traversedNodeSet.add(node);//打上已遍历标志
            //遍历子节点
            node.getEdges().forEach(edge -> {
                _traverse(edge.getNextNode(),consumer,traversedNodeSet);
            });
        }
    }



    @Override
    public String toString() {
        return "Graph{" +
                "meta="+getMeta()+
                ", startNodes=" + startNodes +
                ", endNodes=" + endNodes +
                '}';
    }
}
