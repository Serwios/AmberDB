package com.serwios.amberdb.sql.parser.ast.expressions;

import lombok.Value;

@Value
public class LiteralStringExpression implements Expression {
    String value;
}
