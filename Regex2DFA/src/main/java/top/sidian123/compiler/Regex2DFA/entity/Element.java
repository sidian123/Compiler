package top.sidian123.compiler.Regex2DFA.entity;

import lombok.Data;

/**
 * 代表状态图中的每个元素
 * @author Sidian.Luo
 * @date 2019/11/6 13:41
 */
@Data
public class Element {
    /**
     * 元素类型
     */
    private String type;
    /**
     * 元素类型相关的元数据
     */
    private Object meta;

}
