package Interpreter;

import AST.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static Interpreter.TokenType.SEMICOLON;
import static Interpreter.TokenType.WHILE;

public class Parser {
    public static Hashtable<String, Integer> integerVar;

    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokens.removeIf(token -> token.type == TokenType.SPACE || token.type == TokenType.LINE);
        integerVar = new Hashtable<>();
    }

    private void error(String message) {
        if (pos < tokens.size()) {
            Token t = tokens.get(pos);
            throw new RuntimeException(message + "\nin line:  " + t.line + "\nin pos: " + t.pos);
        } else {
            throw new RuntimeException(message + " in the end of file");
        }
    }

    private Token match(TokenType... expected) {
        if (pos < tokens.size()) {
            Token curr = tokens.get(pos);
            if (Arrays.asList(expected).contains(curr.type)) {
                pos++;
                return curr;
            }
        }
        return null;
    }

    private Token require(TokenType... expected) {
        Token t = match(expected);
        if (t == null)
            error("Ожидается " + Arrays.toString(expected));
        return t;
    }

    private ExprNode parseElem() {
        Token num = match(TokenType.NUMBER);
        if (num != null)
            return new NumberNode(num);
        Token id = match(TokenType.ID);
        if (id != null)
            return new VarNode(id);
        error("Awaits Number or ID");
        return null;
    }

    private ExprNode parseLessLEqualGreaterGEqual() {
        ExprNode e1 = parseElem();
        Token op;
        while ((op = match(TokenType.LESS, TokenType.GREATER)) != null) {
            ExprNode e2 = parseElem();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    private ExprNode parseEqualNEqual() {
        ExprNode e1 = parseLessLEqualGreaterGEqual();
        Token op;
        while ((op = match(TokenType.EQUAL)) != null) {
            ExprNode e2 = parseLessLEqualGreaterGEqual();
            e1 = new BinOpNode(op, e1, e2);
        }
        return e1;
    }

    private StmtNode parseStatement() {
        StmtNode stmtNode = null;
        Token token = require(TokenType.PRINT, TokenType.ID, TokenType.DO);
        switch (token.type) {
            case PRINT:
                stmtNode = new PrintNode(new VarNode(require(TokenType.ID)));
                break;
            case ID:
                Token inc_or_dec = require(TokenType.INC, TokenType.DEC);
                stmtNode =  new UnaryOpNode(inc_or_dec, new VarNode(token));
                break;
            case DO:
                List<StmtNode> stmtNodesDoWhile = new ArrayList<>();
                while(match(WHILE) == null){
                    StmtNode stmtNodeT = parse();
                    if (stmtNodeT == null)
                        throw  new RuntimeException("Awaits 'while' for 'DO-WHILE' cycle");
                    stmtNodesDoWhile.add(stmtNodeT);
                }
                final ExprNode conditionWhile = parseExpression();
                stmtNode = new DoWhileNode(stmtNodesDoWhile, conditionWhile);
        }
        require(SEMICOLON);
        return stmtNode;
    }

    public StmtNode parse() {
        if (pos >= tokens.size())
            return null;
        return parseStatement();
    }

    private ExprNode parseExpression() {
        if (pos >= tokens.size())
            return null;
        return parseEqualNEqual();
    }

    public static void eval(StmtNode node) {
       if (node instanceof DoWhileNode) {
            DoWhileNode doWhileNode = (DoWhileNode) node;
            do{
                for (StmtNode stmtNode : doWhileNode.statements){
                    eval(stmtNode);
                }
            }while (valueToBoolean(evalExpr(doWhileNode.condition)));
            return;
        }else if (node instanceof UnaryOpNode) {
           UnaryOpNode unaryOpNode = (UnaryOpNode) node;
           executeIncDec(unaryOpNode.varNode, unaryOpNode.operation);
           return;
        }else if (node instanceof PrintNode) {
            PrintNode printNode = (PrintNode) node;
            System.out.println(evalExpr(printNode.var));
            return;
        }
        throw new IllegalStateException(node.toString());
    }

    public static Integer evalExpr(ExprNode node){
        if (node instanceof NumberNode){
            NumberNode NumberNode = (NumberNode) node;
            return Integer.valueOf(((NumberNode) node).number.text);
        }
        else if (node instanceof VarNode){
            VarNode varNode = (VarNode) node;
            integerVar.putIfAbsent(varNode.id.text, 0);
            return integerVar.get(varNode.id.text);
        } else if (node instanceof BinOpNode) {
            BinOpNode binOp = (BinOpNode) node;
            Integer left = evalExpr(binOp.left);
            Integer right = evalExpr(binOp.right);
            switch (binOp.op.type) {
                case EQUAL:
                    if (left.equals(right)) return 1;
                    else return 0;
                case LESS:
                    if (left < right) return 1;
                    else return 0;
                case GREATER:
                    if (left > right) return 1;
                    else return 0;

            }
        }
        throw new IllegalStateException(node.toString());
    }

    private static void executeIncDec(VarNode varNode, Token operation){
        integerVar.putIfAbsent(varNode.id.text, 0);
        switch (operation.type) {
            case INC:
                integerVar.put(varNode.id.text, integerVar.get(varNode.id.text)+1);
                break;
            case DEC:
                integerVar.put(varNode.id.text, integerVar.get(varNode.id.text)-1);
        }
    }

    private static boolean valueToBoolean(Integer integer) {
        return integer != null && integer != 0;
    }
}
