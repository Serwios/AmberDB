package com.serwios.amberdb.sql.parser;

import com.serwios.amberdb.sql.SqlParsingTestBase;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.parser.core.ParseException;
import com.serwios.amberdb.sql.parser.core.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlParsingErrorTest extends SqlParsingTestBase {

    private Parser parserFor(String sql) {
        List<Token> tokens = lexer.tokenize(sql);
        return new Parser(tokens);
    }

    @Test
    void invalidKeyword_selec_insteadOfSelect_throwsLexOrParseError() {
        String sql = "SELEC id FROM users;";

        Exception ex = assertThrows(RuntimeException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex instanceof LexException || ex instanceof ParseException);
    }

    @Test
    void selectWithoutProjection_isError() {
        String sql = "SELECT FROM users;";

        ParseException ex = assertThrows(ParseException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex.getMessage().toLowerCase().contains("column name or '*'"));
    }

    @Test
    void whereWithoutExpression_isError() {
        String sql = "SELECT id FROM users WHERE;";

        ParseException ex = assertThrows(ParseException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex.getMessage().toLowerCase().contains("expected expression after where"));
    }

    @Test
    void insertWithoutValues_isError() {
        String sql = "INSERT INTO t VALUES ();";

        ParseException ex = assertThrows(ParseException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex.getMessage().toLowerCase().contains("values list cannot be empty"));
    }

    @Test
    void createTableWithNoColumns_isError() {
        String sql = "CREATE TABLE t ();";

        ParseException ex = assertThrows(ParseException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex.getMessage().toLowerCase().contains("at least one column"));
    }

    @Test
    void unexpectedEndOfInput_inParenthesis_isError() {
        String sql = "SELECT id FROM users WHERE (age > 10;";

        ParseException ex = assertThrows(ParseException.class, () -> parserFor(sql).parseStatement());
        assertTrue(ex.getMessage().toLowerCase().contains("expected ')'"));
    }
}
