package com.serwios.amberdb.sql.parser.ast.expressions;

public class LiteralNullExpression implements Expression {
    @Override
    public String toString() {
        return "NULL";
    }
}
