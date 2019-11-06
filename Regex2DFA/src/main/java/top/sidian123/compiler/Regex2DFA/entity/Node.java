package top.sidian123.compiler.Regex2DFA.entity;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 状态图的节点
 * @author Sidian.Luo
 * @date 2019/11/6 13:17
 */
@Data
public class Node extends Element{
    /**
     * 后继分支
     */
    private List<Edge> edges=new LinkedList<>();

    public Node() {
        this.setType(ElementType.NODE);
    }

    /**
     * 指向node节点
     */
    public void pointNode(Node node,char input){
        //判断是否存在node节点
        if(!isExist(node)){//不存在时
            //创建edge
            Edge edge = new Edge();
            edge.setInput(input);
            edge.setNextNode(node);
            this.edges.add(edge);
        }
    }

    public boolean isExist(Node node){
        return edges.stream().anyMatch(edge -> edge.getNextNode().equals(node));
    }
}
