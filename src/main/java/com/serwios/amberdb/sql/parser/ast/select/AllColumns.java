package com.serwios.amberdb.sql.parser.ast.select;

public class AllColumns implements SelectItem {
    @Override
    public String toString() {
        return "*";
    }
}
