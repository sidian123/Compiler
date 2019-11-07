package top.sidian123.compiler.Regex2DFA.NFA2DFA;

import lombok.NonNull;
import top.sidian123.compiler.Regex2DFA.entity.Graph;
import top.sidian123.compiler.Regex2DFA.entity.Node;
import top.sidian123.compiler.Regex2DFA.utils.MapTable;

import java.util.*;

/**
 * @author Sidian.Luo
 * @date 2019/11/7 10:00
 */
public class NFA2DFA {
    /**
     * 待转化的非确定有限自动机
     */
    private Graph nfaGraph;
    private Set<String> allInput;

    public Graph translate(@NonNull Graph source) {
        LinkedList<Set<Node>> workList=new LinkedList<>();//待处理的状态集合的集合
        MapTable<Set<Node>,String,Set<Node>> mapTable=new MapTable<>();//状态集合与字符的映射规则
        //初始化
        this.nfaGraph = source;
        this.allInput = nfaGraph.getAllInput();
        //得到初始化节点集合
        Set<Node> initialSet = closure(new HashSet<>(this.nfaGraph.getStartNodes()));
        workList.add(initialSet);
        //处理workList所有的状态集合
        Set<Node> stateSet=null;
        while((stateSet=workList.poll())!=null){
            for (String input : this.allInput) {
                //映射
                Set<Node> nextState = closure(delta(stateSet, input));
                //记录映射规则
                mapTable.add(stateSet,input,nextState);
                //不重复地加入到workList中
                if(!workList.contains(nextState) && nextState.size()!=0){//不存在, 且得到的状态集合不为空
                    workList.add(stateSet);
                }
            }
        }
        //将映射表转化为DFA状态图
        return toGraph(mapTable,initialSet,nfaGraph.getFirstEndNode());
    }

    /**
     * 将映射表转化为DFA状态图
     * @param mapTable 映射表
     * @param initialSet 初始状态集合
     * @param endNode NFA结束节点,必定只有一个
     */
    private Graph toGraph(MapTable<Set<Node>, String, Set<Node>> mapTable,Set<Node> initialSet,Node endNode) {
        Set<Set<Node>> doneSet=new HashSet<>();//已扫描过的状态集合
        LinkedList<Set<Node>> workList=new LinkedList<>();//工作集合
        //initial
        workList.add(initialSet);
        //traverse to construct graph
        Graph graph = new Graph();
        Node node=new Node();
        graph.getStartNodes().add(node);
        Set<Node> stateSet=null;
        while((stateSet=workList.poll())!=null){
            for (String input : this.allInput) {
                Set<Node> valueSet=mapTable.get(stateSet,input);
                if(valueSet.size()!=0){
                    //连接到该节点上
                    Node tempNode=new Node();
                    node.pointNode(tempNode,input);
                    //不重复的添加到workList
                    if(!workList.contains(valueSet) && !doneSet.contains(valueSet)){
                        workList.add(valueSet);
                    }
                    //标记该状态集合已被扫描
                    doneSet.add(stateSet);
                    //判断该状态集合是否包含结束状态
                    if(valueSet.contains(endNode)){//有
                        graph.getEndNodes().add(tempNode);
                    }
                }
            }
        }
        return graph;
    }

    /**
     * 空字符的闭包操作, 对于集合中所有状态输入0到多个空字符,得到的所有状态的集合
     *
     * @param set 状态集合
     * @return 结果状态集合
     */
    private Set<Node> closure(Set<Node> set) {
        HashSet<Node> resultSet = new HashSet<>();
        HashSet<Node> traversedNodeSet = new HashSet<>();
        set.forEach(node -> {
            resultSet.addAll(_closure(node,traversedNodeSet));
        });
        return resultSet;
    }

    /**
     * 得到node的闭包状态
     *
     * @param node             待操作的节点
     * @param traversedNodeSet 已遍历节点,防止循环
     */
    private Set<Node> _closure(Node node, Set<Node> traversedNodeSet) {
        HashSet<Node> resultSet = new HashSet<>();
        if (!traversedNodeSet.contains(node)) {//未遍历
            //得到当前节点的闭包状态
            resultSet.add(node);//包含自己
            traversedNodeSet.add(node);
            node.map("")
                    .forEach(node1 -> {
                        resultSet.addAll(_closure(node1,traversedNodeSet));
                    });
        }
        return resultSet;
    }

    /**
     * 对于状态集合set和字符c进行映射, 得到下一状态集合
     *
     * @param set 状态集合
     * @param c   字符
     * @return 下一状态集合
     */
    private Set<Node> delta(Set<Node> set, String c) {
        Set<Node> resultSet = new HashSet<>();
        set.forEach(node -> {//对于每个节点
            //映射
            Set<Node> nextNodeSets = node.map(c);
            //记录
            resultSet.addAll(nextNodeSets);
        });
        return resultSet;
    }

}
