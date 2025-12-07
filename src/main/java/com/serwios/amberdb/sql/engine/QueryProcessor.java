package com.serwios.amberdb.sql.engine;

import com.serwios.amberdb.sql.lexer.core.Lexer;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.parser.ast.Statement;
import com.serwios.amberdb.sql.parser.core.AstPrinter;
import com.serwios.amberdb.sql.parser.core.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueryProcessor {

    private final Lexer lexer;
    private final AstPrinter astPrinter;

    public String debugAst(String sql) {
        Statement stmt = parse(sql);
        return astPrinter.print(stmt);
    }

    public void assertValid(String sql) {
        parse(sql);
    }

    public Statement parse(String sql) {
        List<Token> tokens = lexer.tokenize(sql);
        Parser parser = new Parser(tokens);
        return parser.parseStatement();
    }
}