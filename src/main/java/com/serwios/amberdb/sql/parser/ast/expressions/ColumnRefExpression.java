package com.serwios.amberdb.sql.parser.ast.expressions;

import lombok.Value;

@Value
public class ColumnRefExpression implements Expression {
    String columnName;
}
