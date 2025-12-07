package com.serwios.amberdb.sql.parser;

import com.serwios.amberdb.sql.SqlParsingTestBase;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.parser.ast.Statement;
import com.serwios.amberdb.sql.parser.core.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AstPrinterIntegrationTest extends SqlParsingTestBase {

    @Test
    void printsReadableAstForSelectWithWhereAndLimit() {
        String sql = "SELECT name FROM users WHERE age >= 18 AND active = 1 LIMIT 5;";

        List<Token> tokens = lexer.tokenize(sql);
        Parser parser = new Parser(tokens);
        Statement stmt = parser.parseStatement();

        String out = astPrinter.print(stmt);

        assertTrue(out.contains("SelectStatement"));
        assertTrue(out.contains("projections"));
        assertTrue(out.contains("from: users"));
        assertTrue(out.toLowerCase().contains("where"));
        assertTrue(out.toLowerCase().contains("comparison"));
        assertTrue(out.toLowerCase().contains("age"));
        assertTrue(out.toLowerCase().contains("literalint(18)"));
        assertTrue(out.toLowerCase().contains("limit: 5"));
    }
}
