package com.serwios.amberdb.sql.lexer.scanners;

import com.serwios.amberdb.sql.lexer.core.CharStream;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenFactory;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import org.springframework.stereotype.Component;

@Component
public class StringScanner implements TokenScanner {

    @Override
    public boolean supports(char c, CharStream stream) {
        return c == '\'';
    }

    @Override
    public Token scan(CharStream stream) {
        int start = stream.position();
        stream.advance(); // skip opening '

        StringBuilder sb = new StringBuilder();
        boolean closed = false;

        while (!stream.isAtEnd()) {
            char ch = stream.advance();
            if (ch == '\'') {
                closed = true;
                break;
            }
            sb.append(ch);
        }

        if (!closed) {
            throw new LexException("Unterminated string literal at " + start);
        }

        return TokenFactory.make(TokenType.STRING_LITERAL, sb.toString(), start);
    }
}
