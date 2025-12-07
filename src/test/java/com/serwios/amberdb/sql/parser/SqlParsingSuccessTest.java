package com.serwios.amberdb.sql.parser;

import com.serwios.amberdb.sql.SqlParsingTestBase;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.parser.ast.*;
import com.serwios.amberdb.sql.parser.ast.expressions.*;
import com.serwios.amberdb.sql.parser.ast.select.AllColumns;
import com.serwios.amberdb.sql.parser.ast.select.ColumnSelectItem;
import com.serwios.amberdb.sql.parser.ast.select.SelectItem;
import com.serwios.amberdb.sql.parser.core.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlParsingSuccessTest extends SqlParsingTestBase {

    private Statement parse(String sql) {
        List<Token> tokens = lexer.tokenize(sql);
        Parser parser = new Parser(tokens);
        return parser.parseStatement();
    }

    @Test
    void createTable_withIntTextBigintColumns() {
        String sql = "CREATE TABLE users (" +
                "id INT," +
                "name TEXT," +
                "age BIGINT" +
                ");";

        Statement stmt = parse(sql);
        assertInstanceOf(CreateTableStatement.class, stmt);
        CreateTableStatement create = (CreateTableStatement) stmt;

        assertEquals("users", create.getTableName());
        assertEquals(3, create.getColumns().size());

        ColumnDefinition c0 = create.getColumns().get(0);
        assertEquals("id", c0.getName());
        assertEquals(ColumnType.INT, c0.getType());

        ColumnDefinition c1 = create.getColumns().get(1);
        assertEquals("name", c1.getName());
        assertEquals(ColumnType.TEXT, c1.getType());

        ColumnDefinition c2 = create.getColumns().get(2);
        assertEquals("age", c2.getName());
        assertEquals(ColumnType.BIGINT, c2.getType());
    }

    @Test
    void insert_withIntStringNull() {
        String sql = "INSERT INTO users VALUES (1, 'Alice', NULL);";

        Statement stmt = parse(sql);
        assertInstanceOf(InsertStatement.class, stmt);
        InsertStatement insert = (InsertStatement) stmt;

        assertEquals("users", insert.getTableName());
        assertEquals(3, insert.getValues().size());

        Expression e0 = insert.getValues().get(0);
        assertInstanceOf(LiteralIntExpression.class, e0);
        assertEquals(1, ((LiteralIntExpression) e0).getValue());

        Expression e1 = insert.getValues().get(1);
        assertInstanceOf(LiteralStringExpression.class, e1);
        assertEquals("Alice", ((LiteralStringExpression) e1).getValue());

        Expression e2 = insert.getValues().get(2);
        assertInstanceOf(LiteralNullExpression.class, e2);
    }

    @Test
    void insert_withNegativeInteger() {
        String sql = "INSERT INTO t VALUES (-5);";

        Statement stmt = parse(sql);
        assertInstanceOf(InsertStatement.class, stmt);
        InsertStatement insert = (InsertStatement) stmt;

        assertEquals(1, insert.getValues().size());
        Expression e0 = insert.getValues().get(0);
        assertInstanceOf(LiteralIntExpression.class, e0);
        assertEquals(-5, ((LiteralIntExpression) e0).getValue());
    }

    @Test
    void select_starFromTable() {
        String sql = "SELECT * FROM users;";

        Statement stmt = parse(sql);
        assertInstanceOf(SelectStatement.class, stmt);
        SelectStatement select = (SelectStatement) stmt;

        assertEquals("users", select.getTableName());
        assertNull(select.getSelection());
        assertNull(select.getLimit());

        List<SelectItem> projections = select.getProjections();
        assertEquals(1, projections.size());
        assertInstanceOf(AllColumns.class, projections.get(0));
    }

    @Test
    void select_columnsFromTable() {
        String sql = "SELECT id, name FROM users;";

        Statement stmt = parse(sql);
        assertInstanceOf(SelectStatement.class, stmt);
        SelectStatement select = (SelectStatement) stmt;

        assertEquals("users", select.getTableName());
        assertNull(select.getSelection());
        assertNull(select.getLimit());

        List<SelectItem> projections = select.getProjections();
        assertEquals(2, projections.size());

        assertInstanceOf(ColumnSelectItem.class, projections.get(0));
        assertEquals("id", ((ColumnSelectItem) projections.get(0)).getColumnName());

        assertInstanceOf(ColumnSelectItem.class, projections.get(1));
        assertEquals("name", ((ColumnSelectItem) projections.get(1)).getColumnName());
    }

    @Test
    void select_withWhereComparisonAndLimit() {
        String sql = "SELECT name FROM users WHERE age > 18 LIMIT 10;";

        Statement stmt = parse(sql);
        assertInstanceOf(SelectStatement.class, stmt);
        SelectStatement select = (SelectStatement) stmt;

        assertEquals("users", select.getTableName());
        assertEquals(10, select.getLimit());

        Expression where = select.getSelection();
        assertNotNull(where);
        assertInstanceOf(ComparisonExpression.class, where);
        ComparisonExpression cmp = (ComparisonExpression) where;

        assertEquals(ComparisonOperator.GT, cmp.getOperator());
        assertInstanceOf(ColumnRefExpression.class, cmp.getLeft());
        assertEquals("age", ((ColumnRefExpression) cmp.getLeft()).getColumnName());

        assertInstanceOf(LiteralIntExpression.class, cmp.getRight());
        assertEquals(18, ((LiteralIntExpression) cmp.getRight()).getValue());
    }

    @Test
    void select_whereWithAndOrAndParens() {
        String sql = "SELECT id FROM users " +
                "WHERE (age > 18 OR age < 10) AND name = 'Alice';";

        Statement stmt = parse(sql);
        assertInstanceOf(SelectStatement.class, stmt);
        SelectStatement select = (SelectStatement) stmt;

        Expression where = select.getSelection();
        assertNotNull(where);
        assertInstanceOf(LogicalExpression.class, where);
        LogicalExpression andExpr = (LogicalExpression) where;
        assertEquals(LogicalOperator.AND, andExpr.getOperator());

        assertInstanceOf(LogicalExpression.class, andExpr.getLeft());
        LogicalExpression orExpr = (LogicalExpression) andExpr.getLeft();
        assertEquals(LogicalOperator.OR, orExpr.getOperator());

        assertInstanceOf(ComparisonExpression.class, orExpr.getLeft());
        assertInstanceOf(ComparisonExpression.class, orExpr.getRight());

        assertInstanceOf(ComparisonExpression.class, andExpr.getRight());
        ComparisonExpression rightCmp = (ComparisonExpression) andExpr.getRight();

        assertInstanceOf(ColumnRefExpression.class, rightCmp.getLeft());
        assertEquals("name", ((ColumnRefExpression) rightCmp.getLeft()).getColumnName());

        assertInstanceOf(LiteralStringExpression.class, rightCmp.getRight());
        assertEquals("Alice", ((LiteralStringExpression) rightCmp.getRight()).getValue());
    }
}
