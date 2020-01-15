package Interpreter;

public class Token {

    public final TokenType type;    //Тип токена
    public final String text;       // Текст токена
    public final int pos;           //Индекс первого символа лексемы в строке
    public final int line;          //Индекс строки, в которой находится лексема

    public Token(TokenType type, String text, int pos, int line) {
        this.type = type;
        this.text = text;
        this.pos = pos;
        this.line = line;
    }
    @Override
    public String toString() {
        return "{\ntokenType: " + type + ",\nline: "+ line +",\npos: " + pos + ",\ntext: " + text + "\n}";
    }
}