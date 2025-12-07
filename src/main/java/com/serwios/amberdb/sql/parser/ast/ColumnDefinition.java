package com.serwios.amberdb.sql.parser.ast;

import lombok.Value;

@Value
public class ColumnDefinition {
    String name;
    ColumnType type;
}
