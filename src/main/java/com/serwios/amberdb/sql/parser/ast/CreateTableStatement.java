package com.serwios.amberdb.sql.parser.ast;

import lombok.Value;

import java.util.List;

@Value
public class CreateTableStatement implements Statement {
    String tableName;
    List<ColumnDefinition> columns;
}
