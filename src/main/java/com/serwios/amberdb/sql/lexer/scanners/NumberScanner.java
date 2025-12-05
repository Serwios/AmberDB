package com.serwios.amberdb.sql.lexer.scanners;

import com.serwios.amberdb.sql.lexer.core.CharStream;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenFactory;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import org.springframework.stereotype.Component;

@Component
public class NumberScanner implements TokenScanner {

    @Override
    public boolean supports(char c, CharStream stream) {
        return Character.isDigit(c) || (c == '-' && Character.isDigit(stream.peekNext()));
    }

    @Override
    public Token scan(CharStream stream) {
        int start = stream.position();

        if (stream.peek() == '-') {
            stream.advance();
        }

        if (!Character.isDigit(stream.peek())) {
            throw new LexException("Expected digit after '-' at " + start);
        }

        while (Character.isDigit(stream.peek())) {
            stream.advance();
        }

        String text = stream.slice(start, stream.position());
        return TokenFactory.make(TokenType.INTEGER_LITERAL, text, start);
    }
}
