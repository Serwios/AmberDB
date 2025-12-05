package com.serwios.amberdb.shell;

import com.serwios.amberdb.sql.lexer.core.Lexer;
import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final Lexer lexer;

    public void handle(String line, Terminal terminal) {
        try {
            List<Token> tokens = lexer.tokenize(line);

            // TODO: impl parser
            printTokens(tokens, terminal);

        } catch (LexException e) {
            printError("Lexing error: " + e.getMessage(), terminal);
        } catch (Exception e) {
            printError("Unexpected error: " + e.getMessage(), terminal);
        }

        terminal.writer().flush();
    }

    private void printTokens(List<Token> tokens, Terminal terminal) {
        terminal.writer().println(
                new AttributedString(
                        "Tokens:",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)
                ).toAnsi()
        );

        for (Token token : tokens) {
            String line = String.format("  %-15s '%s' @%d",
                    token.getType(),
                    token.getLexeme(),
                    token.getPosition()
            );
            terminal.writer().println(
                    new AttributedString(
                            line,
                            AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
                    ).toAnsi()
            );
        }
    }

    private void printError(String message, Terminal terminal) {
        terminal.writer().println(
                new AttributedString(
                        message,
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
                ).toAnsi()
        );
    }
}

