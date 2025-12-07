package com.serwios.amberdb.sql.parser.ast;

import com.serwios.amberdb.sql.parser.ast.expressions.Expression;
import com.serwios.amberdb.sql.parser.ast.select.SelectItem;
import lombok.Value;

import java.util.List;

@Value
public class SelectStatement implements Statement {
    List<SelectItem> projections;
    String tableName;
    Expression selection;
    Integer limit;
}
