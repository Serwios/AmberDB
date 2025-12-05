package com.serwios.amberdb.sql.lexer.model;

public enum TokenType {
    // Keywords
    CREATE, TABLE, INSERT, INTO, VALUES,
    SELECT, FROM, WHERE, LIMIT,
    AND, OR, NULL,
    INT, BIGINT, TEXT,

    // Identifiers & literals
    IDENTIFIER,      // my_table
    INTEGER_LITERAL, // 123
    STRING_LITERAL,  // '123'

    // Symbols
    LPAREN,     // (
    RPAREN,     // )
    COMMA,      // ,
    SEMICOLON,  // ;
    STAR,       // *

    // Operators
    EQ,         // =
    NEQ,        // !=
    LT,         // <
    LTE,        // <=
    GT,         // >
    GTE,        // >=

    EOF
}
