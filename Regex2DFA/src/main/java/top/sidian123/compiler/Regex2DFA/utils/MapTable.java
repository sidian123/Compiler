package top.sidian123.compiler.Regex2DFA.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 行,列,值可为任意类型的二维数组,由Map实现
 * @param <R> row行类型
 * @param <C> column列类型
 * @param <V> value值类型
 * @author Sidian.Luo
 * @date 2019/11/7 15:23
 */
public class MapTable<R,C,V> {
    private Map<R,Map<C,V>> table=new HashMap<>();

    /**
     * 表中(添加|修改)一项
     * @param row 行
     * @param column 列
     * @param value 值
     */
    public void add(R row,C column,V value){
        //若不存在行, 则创建
        if(!table.containsKey(row)){
            table.put(row,new HashMap<>());
        }
        //添加|修改值
        table.get(row).put(column,value);
    }

    /**
     * 获取表中一项的值
     * @param row 行
     * @param column 列
     * @return 值,不存在时则null
     */
    public V get(R row,C column){
        //必须存在行
        if(table.containsKey(row)){
            //必须存在列
            if(table.get(row).containsKey(column)){
                return table.get(row).get(column);
            }
        }
        return null;
    }

    /**
     * 获取整行
     * @param row 行
     * @return 整行, 不存在则null
     */
    public Map<C,V> getRow(R row){
        return table.get(row);
    }

}
