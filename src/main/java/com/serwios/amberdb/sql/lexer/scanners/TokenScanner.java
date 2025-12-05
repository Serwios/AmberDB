package com.serwios.amberdb.sql.lexer.scanners;

import com.serwios.amberdb.sql.lexer.core.CharStream;
import com.serwios.amberdb.sql.lexer.model.Token;

public interface TokenScanner {
    boolean supports(char c, CharStream stream);

    Token scan(CharStream stream);
}
