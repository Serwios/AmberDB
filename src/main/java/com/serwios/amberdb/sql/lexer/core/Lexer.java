package com.serwios.amberdb.sql.lexer.core;

import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenFactory;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import com.serwios.amberdb.sql.lexer.scanners.TokenScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Lexer {
    private final WhitespaceSkipper whitespaceSkipper;
    private final List<TokenScanner> scanners;

    public List<Token> tokenize(String input) {
        CharStream stream = new CharStream(input);
        List<Token> tokens = new ArrayList<>();

        while (!stream.isAtEnd()) {
            whitespaceSkipper.skip(stream);
            if (stream.isAtEnd()) break;

            char c = stream.peek();

            TokenScanner scanner = findScanner(c, stream);
            Token token = scanner.scan(stream);
            tokens.add(token);
        }

        tokens.add(TokenFactory.make(TokenType.EOF, "", stream.position()));
        return tokens;
    }

    private TokenScanner findScanner(char c, CharStream stream) {
        for (TokenScanner scanner : scanners) {
            if (scanner.supports(c, stream)) {
                return scanner;
            }
        }
        throw new LexException("No scanner found for character '" + c + "' at " + stream.position());
    }
}
