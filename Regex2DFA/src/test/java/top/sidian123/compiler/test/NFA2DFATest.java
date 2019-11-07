package top.sidian123.compiler.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import top.sidian123.compiler.Regex2DFA.NFA2DFA.NFA2DFA;
import top.sidian123.compiler.Regex2DFA.Regex2NFA.Regex2NFA;
import top.sidian123.compiler.Regex2DFA.entity.Graph;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sidian.Luo
 * @date 2019/11/7 17:11
 */
@Log4j2
public class NFA2DFATest {


    @Test
    public void test(){
        String regex="a(b|c)*";
        Regex2NFA regex2NFA=new Regex2NFA();
        NFA2DFA nfa2DFA=new NFA2DFA();
        //RE to NFA
        Graph graph = regex2NFA.parse(regex);
        //label node for conveniently debug
        AtomicInteger i= new AtomicInteger();
        graph.traverse(node -> node.setMeta("n"+ i.getAndIncrement()));
        //NFA to DFA
        graph=nfa2DFA.translate(graph);
        log.info(graph);
    }
}
