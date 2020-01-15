import AST.StmtNode;
import Interpreter.Lexer;
import Interpreter.Parser;
import Interpreter.Token;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String code = "x++;\n" +
                "print x;\n" +
                "do\n" +
                "    x++;\n" +
                "    print x;\n" +
                "while x < 16;\n";
        Lexer l = new Lexer(code);
        List<Token> tokens = l.lex();
        Parser p = new Parser(tokens);
        while(true) {
            StmtNode node = p.parse();
            if (node == null)
                break;
            Parser.eval(node);
        }
    }
}
