package com.serwios.amberdb.sql.lexer.core;

import com.serwios.amberdb.sql.lexer.model.TokenType;

import java.util.HashMap;
import java.util.Map;

public class KeywordTable {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("CREATE", TokenType.CREATE);
        KEYWORDS.put("TABLE", TokenType.TABLE);
        KEYWORDS.put("INSERT", TokenType.INSERT);
        KEYWORDS.put("INTO", TokenType.INTO);
        KEYWORDS.put("VALUES", TokenType.VALUES);
        KEYWORDS.put("SELECT", TokenType.SELECT);
        KEYWORDS.put("FROM", TokenType.FROM);
        KEYWORDS.put("WHERE", TokenType.WHERE);
        KEYWORDS.put("LIMIT", TokenType.LIMIT);
        KEYWORDS.put("AND", TokenType.AND);
        KEYWORDS.put("OR", TokenType.OR);
        KEYWORDS.put("NULL", TokenType.NULL);

        KEYWORDS.put("INT", TokenType.INT);
        KEYWORDS.put("BIGINT", TokenType.BIGINT);
        KEYWORDS.put("TEXT", TokenType.TEXT);
    }

    public static TokenType lookup(String raw) {
        return KEYWORDS.get(raw.toUpperCase());
    }
}
