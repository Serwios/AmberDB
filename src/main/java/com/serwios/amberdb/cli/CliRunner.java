package com.serwios.amberdb.cli;

import com.serwios.amberdb.shell.BannerPrinter;
import com.serwios.amberdb.sql.engine.QueryProcessor;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.parser.core.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
public class CliRunner implements CommandLineRunner {
    private final QueryProcessor queryProcessor;

    @Override
    public void run(String... args) {
        CliArgs parsedArgs = CliArgs.parse(args);

        if (!parsedArgs.hasQuery()) {
            return;
        }

        try {
            if (parsedArgs.debugAst()) {
                String ast = queryProcessor.debugAst(parsedArgs.query());
                System.out.println(ast);
            } else {
                queryProcessor.assertValid(parsedArgs.query());
            }
            System.exit(0);
        } catch (LexException | ParseException e) {
            System.err.println("Syntax error: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(3);
        }
    }
}
