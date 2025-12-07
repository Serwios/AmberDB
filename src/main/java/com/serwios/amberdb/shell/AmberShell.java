package com.serwios.amberdb.shell;

import lombok.RequiredArgsConstructor;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmberShell {
    private final BannerPrinter bannerPrinter;
    private final CommandHandler commandHandler;

    public void start() throws Exception {
        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build()) {

            bannerPrinter.print(terminal);

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            AttributedString amberPrompt = new AttributedString(
                    "amber> ",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)
            );

            while (true) {
                String line;
                try {
                    line = reader.readLine(amberPrompt.toAnsi());
                } catch (UserInterruptException | EndOfFileException e) {
                    break; // Ctrl+C / Ctrl+D / EOF
                }

                if (line == null) {
                    break;
                }

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                    break;
                }

                commandHandler.handle(line, terminal);
            }

            terminal.writer().println("Kiss u.");
            terminal.writer().flush();
        }
    }
}
