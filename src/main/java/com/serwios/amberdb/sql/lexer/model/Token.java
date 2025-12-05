package com.serwios.amberdb.sql.lexer.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Token {
    private final TokenType type;
    private final String lexeme;
    private final int position;

    Token(TokenType type, String lexeme, int position) {
        this.type = type;
        this.lexeme = lexeme;
        this.position = position;
    }
}

