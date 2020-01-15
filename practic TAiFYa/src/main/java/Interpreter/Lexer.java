package Interpreter;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private String src;
    private int pos;
    private int line;
    private LinkedList<Token> tokens;

    public Lexer(String src) {
        this.src = src;
        pos = 0;
        line = 1;
        tokens = new LinkedList<>();
    }

    private int sumPreviousPos(){
        int sum = 0;
        for (Token token : tokens){
            if (token.type == TokenType.LINE){
                sum += token.pos + 2;
            }
        }
        return sum;
    }

    private boolean nextToken(){
        if (pos >= src.length())
            return false;
        else{
            for (TokenType tt : TokenType.values()){
                Matcher m = tt.pattern.matcher(src);
                m.region(pos, src.length());
                if (m.lookingAt()){
                    tokens.add(new Token(tt, m.group(), pos - sumPreviousPos(), line));
                    if (tt == TokenType.LINE) {
                        line++;
                    }
                    pos = m.end();
                    return true;
                }
            }
            throw new RuntimeException("Неизвестный символ\nСтрока: " + line +"\nПозиция: " + pos + "\n");
        }
    }

    public List<Token> lex(){
        while(nextToken());
        if (!tokens.isEmpty())
            tokens.add(new Token(TokenType.LINE, "\n", 0, tokens.getLast().line));
        return tokens;
    }

}