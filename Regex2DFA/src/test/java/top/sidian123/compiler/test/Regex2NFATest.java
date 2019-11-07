package top.sidian123.compiler.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.sidian123.compiler.Regex2DFA.Regex2NFA.Regex2NFA;
import top.sidian123.compiler.Regex2DFA.entity.Graph;

/**
 * @author sidian
 * @date 2019/11/6 22:06
 */
@Log4j2
public class Regex2NFATest {

    @Test
    public void test(){
        String example1="a(b|c)*";
        String example2="ab|c(d*|ef)*g";
        Regex2NFA regex2NFA=new Regex2NFA();

        Graph graph = regex2NFA.parse(example1);
        log.info(graph);
        Assertions.assertEquals(example1,graph.getMeta());

        graph=regex2NFA.parse(example2);
        log.info(graph);
        Assertions.assertEquals(example2,graph.getMeta());
    }
}
