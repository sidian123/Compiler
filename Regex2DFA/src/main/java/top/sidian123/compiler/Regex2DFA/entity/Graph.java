package top.sidian123.compiler.Regex2DFA.entity;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 状态图
 * @author Sidian.Luo
 * @date 2019/11/6 13:30
 */
@Data
public class Graph extends Element{
    public Graph(){
        this.setType(ElementType.GRAPH);
    }

    public Graph(Node StartNode,Node endNode){
        this();
        startNodes.add(StartNode);
        endNodes.add(endNode);
    }
    /**
     * 开始节点
     */
    private List<Node> startNodes=new LinkedList<>();

    public Node getFirstStartNode(){
        return startNodes.get(0);
    }

    public Node getFirstEndNode(){
        return endNodes.get(0);
    }

    /**
     * 结束节点
     */
    private List<Node> endNodes=new LinkedList<>();

    @Override
    public String toString() {
        return "Graph{" +
                "meta="+getMeta()+
                ", startNodes=" + startNodes +
                ", endNodes=" + endNodes +
                '}';
    }
}
