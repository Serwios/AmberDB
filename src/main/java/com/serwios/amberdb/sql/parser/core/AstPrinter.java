package com.serwios.amberdb.sql.parser.core;

import com.serwios.amberdb.sql.parser.ast.*;
import com.serwios.amberdb.sql.parser.ast.expressions.*;
import com.serwios.amberdb.sql.parser.ast.select.AllColumns;
import com.serwios.amberdb.sql.parser.ast.select.ColumnSelectItem;
import com.serwios.amberdb.sql.parser.ast.select.SelectItem;
import org.springframework.stereotype.Component;

@Component
public class AstPrinter {

    private static final int INDENT_STEP = 2;

    public String print(Statement stmt) {
        StringBuilder sb = new StringBuilder(256);
        printStatement(stmt, sb, 0);
        return sb.toString();
    }

    private void printStatement(Statement stmt, StringBuilder sb, int indent) {
        if (stmt instanceof CreateTableStatement s) {
            printCreateTable(s, sb, indent);
        } else if (stmt instanceof InsertStatement s) {
            printInsert(s, sb, indent);
        } else if (stmt instanceof SelectStatement s) {
            printSelect(s, sb, indent);
        } else {
            indent(sb, indent).append("UnknownStatement(")
                    .append(stmt.getClass().getSimpleName())
                    .append(")\n");
        }
    }

    private void printCreateTable(CreateTableStatement s, StringBuilder sb, int indent) {
        indent(sb, indent).append("CreateTableStatement\n");
        indent(sb, indent + INDENT_STEP)
                .append("tableName: ")
                .append(s.getTableName())
                .append('\n');

        indent(sb, indent + INDENT_STEP).append("columns:\n");
        if (s.getColumns().isEmpty()) {
            indent(sb, indent + 2 * INDENT_STEP).append("(empty)\n");
            return;
        }

        for (ColumnDefinition col : s.getColumns()) {
            indent(sb, indent + 2 * INDENT_STEP)
                    .append(col.getName())
                    .append(" : ")
                    .append(col.getType())
                    .append('\n');
        }
    }

    private void printInsert(InsertStatement s, StringBuilder sb, int indent) {
        indent(sb, indent).append("InsertStatement\n");
        indent(sb, indent + INDENT_STEP)
                .append("tableName: ")
                .append(s.getTableName())
                .append('\n');

        indent(sb, indent + INDENT_STEP).append("values:\n");
        if (s.getValues().isEmpty()) {
            indent(sb, indent + 2 * INDENT_STEP).append("(empty)\n");
            return;
        }

        for (Expression expr : s.getValues()) {
            printExpression(expr, sb, indent + 2 * INDENT_STEP);
            sb.append('\n');
        }
    }

    private void printSelect(SelectStatement s, StringBuilder sb, int indent) {
        indent(sb, indent).append("SelectStatement\n");

        indent(sb, indent + INDENT_STEP).append("projections:\n");
        if (s.getProjections().isEmpty()) {
            indent(sb, indent + 2 * INDENT_STEP).append("(empty)\n");
        } else {
            for (SelectItem item : s.getProjections()) {
                printSelectItem(item, sb, indent + 2 * INDENT_STEP);
                sb.append('\n');
            }
        }

        indent(sb, indent + INDENT_STEP)
                .append("from: ")
                .append(s.getTableName())
                .append('\n');

        indent(sb, indent + INDENT_STEP).append("where:\n");
        if (s.getSelection() != null) {
            printExpression(s.getSelection(), sb, indent + 2 * INDENT_STEP);
            sb.append('\n');
        } else {
            indent(sb, indent + 2 * INDENT_STEP).append("null\n");
        }

        indent(sb, indent + INDENT_STEP).append("limit: ");
        if (s.getLimit() != null) {
            sb.append(s.getLimit());
        } else {
            sb.append("null");
        }
        sb.append('\n');
    }

    private void printSelectItem(SelectItem item, StringBuilder sb, int indent) {
        if (item instanceof AllColumns) {
            indent(sb, indent).append("AllColumns(*)");
        } else if (item instanceof ColumnSelectItem c) {
            indent(sb, indent)
                    .append("Column(")
                    .append(c.getColumnName())
                    .append(')');
        } else {
            indent(sb, indent)
                    .append("UnknownSelectItem(")
                    .append(item.getClass().getSimpleName())
                    .append(')');
        }
    }

    private void printExpression(Expression expr, StringBuilder sb, int indent) {
        if (expr instanceof ColumnRefExpression e) {
            indent(sb, indent)
                    .append("ColumnRef(")
                    .append(e.getColumnName())
                    .append(')');
        } else if (expr instanceof LiteralIntExpression e) {
            indent(sb, indent)
                    .append("LiteralInt(")
                    .append(e.getValue())
                    .append(')');
        } else if (expr instanceof LiteralStringExpression e) {
            indent(sb, indent)
                    .append("LiteralString('")
                    .append(e.getValue())
                    .append("')");
        } else if (expr instanceof LiteralNullExpression) {
            indent(sb, indent).append("LiteralNull");
        } else if (expr instanceof ComparisonExpression e) {
            printComparison(e, sb, indent);
        } else if (expr instanceof LogicalExpression e) {
            printLogical(e, sb, indent);
        } else {
            indent(sb, indent)
                    .append("UnknownExpr(")
                    .append(expr.getClass().getSimpleName())
                    .append(')');
        }
    }

    private void printComparison(ComparisonExpression e, StringBuilder sb, int indent) {
        indent(sb, indent)
                .append("Comparison(")
                .append(e.getOperator())
                .append(")\n");

        indent(sb, indent + INDENT_STEP).append("left:\n");
        printExpression(e.getLeft(), sb, indent + 2 * INDENT_STEP);
        sb.append('\n');

        indent(sb, indent + INDENT_STEP).append("right:\n");
        printExpression(e.getRight(), sb, indent + 2 * INDENT_STEP);
    }

    private void printLogical(LogicalExpression e, StringBuilder sb, int indent) {
        indent(sb, indent)
                .append("Logical(")
                .append(e.getOperator())
                .append(")\n");

        indent(sb, indent + INDENT_STEP).append("left:\n");
        printExpression(e.getLeft(), sb, indent + 2 * INDENT_STEP);
        sb.append('\n');

        indent(sb, indent + INDENT_STEP).append("right:\n");
        printExpression(e.getRight(), sb, indent + 2 * INDENT_STEP);
    }

    private StringBuilder indent(StringBuilder sb, int n) {
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        return sb;
    }
}
