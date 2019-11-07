package top.sidian123.compiler.Regex2DFA.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sidian.Luo
 * @date 2019/11/6 14:08
 */
public class Edge extends Element{
    /**
     * 下一个节点
     */
    @Setter
    @Getter
    private Node nextNode;
    /**
     * 输入字符
     */
    @Setter
    @Getter
    private String input;

    Edge(){
        this.setType(ElementType.EDGE);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "input='" + input + '\'' +
                '}';
    }
}
