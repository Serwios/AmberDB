package com.serwios.amberdb.sql.lexer.core;

import org.springframework.stereotype.Component;

@Component
public class WhitespaceSkipper {
    public void skip(CharStream cs) {
        while (!cs.isAtEnd()) {
            char c = cs.peek();
            if (Character.isWhitespace(c)) {
                cs.advance();
            } else break;
        }
    }
}
