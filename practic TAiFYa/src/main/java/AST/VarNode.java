package AST;

import Interpreter.Token;

public class VarNode extends ExprNode{

    public final Token id;

    public VarNode(Token id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.text;
    }
}