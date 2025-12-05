package com.serwios.amberdb.sql.lexer.model;

public class TokenFactory {
    public static Token make(TokenType type, String lexeme, int position) {
        return new Token(type, lexeme, position);
    }
}
