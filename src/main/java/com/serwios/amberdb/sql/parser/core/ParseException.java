package com.serwios.amberdb.sql.parser.core;

import com.serwios.amberdb.sql.lexer.model.Token;
import lombok.Getter;

@Getter
public class ParseException extends RuntimeException {
    private final int position;
    private final Token offendingToken;

    public ParseException(String message, int position, Token offendingToken) {
        super(message);
        this.position = position;
        this.offendingToken = offendingToken;
    }
}
