package com.serwios.amberdb.sql.lexer.scanners;

import com.serwios.amberdb.sql.lexer.core.CharStream;
import com.serwios.amberdb.sql.lexer.core.KeywordTable;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenFactory;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import org.springframework.stereotype.Component;

@Component
public class IdentifierScanner implements TokenScanner {

    @Override
    public boolean supports(char c, CharStream stream) {
        return Character.isLetter(c) || c == '_';
    }

    @Override
    public Token scan(CharStream stream) {
        int start = stream.position();

        while (!stream.isAtEnd()) {
            char ch = stream.peek();
            if (Character.isLetterOrDigit(ch) || ch == '_') {
                stream.advance();
            } else break;
        }

        String text = stream.slice(start, stream.position());
        TokenType type = KeywordTable.lookup(text);
        if (type == null) type = TokenType.IDENTIFIER;

        return TokenFactory.make(type, text, start);
    }
}
