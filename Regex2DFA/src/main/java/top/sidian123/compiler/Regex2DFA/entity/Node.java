package top.sidian123.compiler.Regex2DFA.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 状态图的节点
 * @author Sidian.Luo
 * @date 2019/11/6 13:17
 */
public class Node extends Element{
    /**
     * 后继分支
     */
    @Setter
    @Getter
    private List<Edge> edges=new LinkedList<>();

    public Node() {
        this.setType(ElementType.NODE);
    }

    /**
     * 指向node节点
     * @param node 指向的节点
     * @param input 边上的输入,应含一个字符
     */
    public void pointNode(Node node,String input){
        //判断是否存在node节点
        if(!isExist(node)){//不存在时
            //创建edge
            Edge edge = new Edge();
            edge.setInput(input);
            edge.setNextNode(node);
            this.edges.add(edge);
        }
    }

    /**
     * 输入字符c,得到下一状态集合
     * @param c 字符
     * @return 下一状态集合
     */
    public Set<Node> map(String c){
        Set<Node> set=new HashSet<>();
        for (Edge edge : this.getEdges()) {
            if(edge.getInput().equals(c)){//找到下一状态
                set.add(edge.getNextNode());
            }
        }
        return set;
    }

    public boolean isExist(Node node){
        return edges.stream().anyMatch(edge -> edge.getNextNode().equals(node));
    }

    @Override
    public String toString() {
        return "Node{" +
                "meta="+getMeta()+ " "+
                "edges=" + edges +
                '}';
    }
}
