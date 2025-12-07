package com.serwios.amberdb.cli;

import java.util.Objects;

public record CliArgs(String query, boolean debugAst) {
    public boolean hasQuery() {
        return query != null && !query.isBlank();
    }

    public static CliArgs parse(String[] args) {
        String query = null;
        boolean debug = false;

        for (int i = 0; i < args.length; i++) {
            String a = args[i];

            if (Objects.equals(a, "--query")) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("--query requires a SQL string");
                }
                query = args[++i];
            } else if (Objects.equals(a, "--debug-ast")) {
                debug = true;
            }
        }

        return new CliArgs(query, debug);
    }
}
