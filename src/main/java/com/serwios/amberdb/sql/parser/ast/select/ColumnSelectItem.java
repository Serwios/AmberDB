package com.serwios.amberdb.sql.parser.ast.select;

import lombok.Value;

@Value
public class ColumnSelectItem implements SelectItem {
    String columnName;
}
