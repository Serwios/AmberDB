package com.serwios.amberdb.sql.lexer.scanners;

import com.serwios.amberdb.sql.lexer.core.CharStream;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenFactory;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import org.springframework.stereotype.Component;

@Component
public class SymbolScanner implements TokenScanner {

    @Override
    public boolean supports(char c, CharStream stream) {
        return switch (c) {
            case '(', ')', ',', ';', '*', '=', '!', '<', '>' -> true;
            default -> false;
        };
    }

    @Override
    public Token scan(CharStream stream) {
        int start = stream.position();
        char c = stream.advance();

        return switch (c) {
            case '(' -> TokenFactory.make(TokenType.LPAREN, "(", start);
            case ')' -> TokenFactory.make(TokenType.RPAREN, ")", start);
            case ',' -> TokenFactory.make(TokenType.COMMA, ",", start);
            case ';' -> TokenFactory.make(TokenType.SEMICOLON, ";", start);
            case '*' -> TokenFactory.make(TokenType.STAR, "*", start);
            case '=' -> TokenFactory.make(TokenType.EQ, "=", start);
            case '!' -> {
                if (stream.peek() == '=') {
                    stream.advance();
                    yield TokenFactory.make(TokenType.NEQ, "!=", start);
                }
                throw new LexException("Unexpected '!' at " + start);
            }
            case '<' -> {
                if (stream.peek() == '=') {
                    stream.advance();
                    yield TokenFactory.make(TokenType.LTE, "<=", start);
                }
                yield TokenFactory.make(TokenType.LT, "<", start);
            }
            case '>' -> {
                if (stream.peek() == '=') {
                    stream.advance();
                    yield TokenFactory.make(TokenType.GTE, ">=", start);
                }
                yield TokenFactory.make(TokenType.GT, ">", start);
            }
            default -> throw new LexException("Unexpected character '" + c + "' at " + start);
        };
    }
}
