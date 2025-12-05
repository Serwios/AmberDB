package com.serwios.amberdb.sql.lexer.core;


public class CharStream {
    private final String input;
    private final int length;
    private int pos = 0;

    public CharStream(String input) {
        this.input = input;
        this.length = input.length();
    }

    public boolean isAtEnd() {
        return pos >= length;
    }

    public char peek() {
        return isAtEnd() ? '\0' : input.charAt(pos);
    }

    public char peekNext() {
        return pos + 1 >= length ? '\0' : input.charAt(pos + 1);
    }

    public char advance() {
        return input.charAt(pos++);
    }

    public int position() {
        return pos;
    }

    public String slice(int start, int end) {
        return input.substring(start, end);
    }
}
