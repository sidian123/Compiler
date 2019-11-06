package top.sidian123.compiler.Regex2DFA.exception;

/**
 * @author sidian
 * @date 2019/11/6 19:40
 */
public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException(String str) {
        super(str);
    }
}
