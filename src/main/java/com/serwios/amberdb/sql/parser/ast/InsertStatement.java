package com.serwios.amberdb.sql.parser.ast;

import com.serwios.amberdb.sql.parser.ast.expressions.Expression;
import lombok.Value;

import java.util.List;

@Value
public class InsertStatement implements Statement {
    String tableName;
    List<Expression> values;
}
