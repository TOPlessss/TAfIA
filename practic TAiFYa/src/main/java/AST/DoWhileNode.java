package AST;

import java.util.List;

public class DoWhileNode extends StmtNode {

    public final List<StmtNode> statements;
    public final ExprNode condition;

    public DoWhileNode(List<StmtNode> statements, ExprNode condition) {
        this.statements = statements;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "do\n" +statements.toString() +   "\nwhile " + condition.toString() +  "\n";
    }
}


