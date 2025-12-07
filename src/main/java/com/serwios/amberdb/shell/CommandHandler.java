package com.serwios.amberdb.shell;

import com.serwios.amberdb.sql.engine.QueryProcessor;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.parser.ast.Statement;
import com.serwios.amberdb.sql.parser.core.ParseException;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final QueryProcessor queryProcessor;

    public void handle(String line, Terminal terminal) {
        try {
            Statement stmt = queryProcessor.parse(line);

            AttributedString out = new AttributedString(
                    "[OK] " + stmt,
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
            );
            terminal.writer().println(out.toAnsi());
        } catch (LexException | ParseException e) {
            AttributedString out = new AttributedString(
                    "Syntax error: " + e.getMessage(),
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
            );
            terminal.writer().println(out.toAnsi());
        } catch (Exception e) {
            AttributedString out = new AttributedString(
                    "Error: " + e.getMessage(),
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
            );
            terminal.writer().println(out.toAnsi());
        }
        terminal.writer().flush();
    }
}

