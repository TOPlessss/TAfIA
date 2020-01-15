package Interpreter;

import java.util.regex.Pattern;

public enum TokenType{
    DO              ("do"),                 // do-while цикл
    WHILE           ("while"),              // для do-while цикл
    NUMBER          ("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"), // Числа
    PRINT           ("print"),              // Вывод идентификатора
    SEMICOLON       (";"),                  // Точка с запятой
    ID              ("[a-zA-Z_][\\w]*"),    // Индентификатор
    GREATER         (">"),                  // Сравнение БОЛЬШЕ. Левый больше правого?
    LESS            ("<"),                  // Сравнение МЕНЬШЕ. Левый меньше правого?
    EQUAL           ("="),                  // Равенство сравниваемых элементов
    INC             ("\\++"),               // Инкремент
    DEC             ("\\--"),               // Декремент
    LINE            ("[\\r]*[\\n]"),        // Перенос строки (служит разделителем команд)
    SPACE           ("[ \\s\\t\\v]+");      // Пробел

    Pattern pattern;

    TokenType(String regEx) {
        this.pattern = Pattern.compile(regEx);
    }
}
