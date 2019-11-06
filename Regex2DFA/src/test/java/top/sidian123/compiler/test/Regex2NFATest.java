package top.sidian123.compiler.test;

import org.junit.jupiter.api.Test;
import top.sidian123.compiler.Regex2DFA.Regex2NFA.Regex2NFA;
import top.sidian123.compiler.Regex2DFA.entity.Graph;

/**
 * @author sidian
 * @date 2019/11/6 22:06
 */
public class Regex2NFATest {

    @Test
    public void test(){
        Regex2NFA regex2NFA=new Regex2NFA();
        Graph graph = regex2NFA.parse("a(b|c)*");
        System.out.println(graph);
    }
}
