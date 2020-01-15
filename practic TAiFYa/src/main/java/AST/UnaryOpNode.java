package AST;

import Interpreter.Token;

public class UnaryOpNode extends StmtNode {

    public final Token operation;
    public final VarNode varNode;

    public UnaryOpNode(Token operation, VarNode varNode) {

        this.operation = operation;
        this.varNode = varNode;
    }

    @Override
    public String toString() {
        return (varNode == null ? "null" : varNode.toString()) + " " + operation.text;
    }
}
