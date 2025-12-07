package com.serwios.amberdb.sql.parser.ast.expressions;

import lombok.Value;

@Value
public class ComparisonExpression implements Expression {
    Expression left;
    ComparisonOperator operator;
    Expression right;
}
