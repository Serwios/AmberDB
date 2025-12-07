package com.serwios.amberdb.sql;

import com.serwios.amberdb.sql.lexer.core.Lexer;
import com.serwios.amberdb.sql.lexer.core.WhitespaceSkipper;
import com.serwios.amberdb.sql.lexer.scanners.IdentifierScanner;
import com.serwios.amberdb.sql.lexer.scanners.NumberScanner;
import com.serwios.amberdb.sql.lexer.scanners.StringScanner;
import com.serwios.amberdb.sql.lexer.scanners.SymbolScanner;
import com.serwios.amberdb.sql.parser.core.AstPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        classes = {
                Lexer.class,
                IdentifierScanner.class,
                NumberScanner.class,
                StringScanner.class,
                SymbolScanner.class,
                WhitespaceSkipper.class,
                AstPrinter.class
        }
)
@ActiveProfiles("test")
public abstract class SqlParsingTestBase {

    @Autowired
    protected Lexer lexer;

    @Autowired
    protected AstPrinter astPrinter;
}