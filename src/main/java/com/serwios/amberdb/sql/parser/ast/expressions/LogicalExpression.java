package com.serwios.amberdb.sql.parser.ast.expressions;

import lombok.Value;

@Value
public class LogicalExpression implements Expression {
    Expression left;
    LogicalOperator operator;
    Expression right;
}
