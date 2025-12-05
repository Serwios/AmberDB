package com.serwios.amberdb.shell;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {

    public void handle(String line, Terminal terminal) {
        AttributedString out = new AttributedString(
                ">> " + line,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
        );
        terminal.writer().println(out.toAnsi());
        terminal.writer().flush();
    }
}
