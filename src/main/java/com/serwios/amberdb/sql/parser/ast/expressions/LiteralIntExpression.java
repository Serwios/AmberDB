package com.serwios.amberdb.sql.parser.ast.expressions;

import lombok.Value;

@Value
public class LiteralIntExpression implements Expression {
    int value;
}
