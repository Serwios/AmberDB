package com.serwios.amberdb.shell;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class BannerPrinter {

    private static final String BANNER = ""
            + "    ___    __  _______  __________     ____  ____ \n"
            + "   /   |  /  |/  / __ )/ ____/ __ \\   / __ \\/ __ )\n"
            + "  / /| | / /|_/ / __  / __/ / /_/ /  / / / / __  |\n"
            + " / ___ |/ /  / / /_/ / /___/ _, _/  / /_/ / /_/ / \n"
            + "/_/  |_/_/  /_/_____/_____/_/ |_|  /_____/_____/  \n"
            + "                                                  \n";

    public void print(Terminal terminal) {
        AttributedStyle amberStyle = AttributedStyle.DEFAULT
                .foreground(AttributedStyle.YELLOW);

        AttributedString coloredBanner = new AttributedString(BANNER, amberStyle);

        terminal.writer().println(coloredBanner.toAnsi());
        terminal.writer().println("Welcome to AmberDB REPL. Type 'exit' or 'quit' to leave.");
        terminal.writer().flush();
    }
}
