package com.serwios.amberdb.sql.parser.core;

import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import com.serwios.amberdb.sql.parser.ast.*;
import com.serwios.amberdb.sql.parser.ast.expressions.*;
import com.serwios.amberdb.sql.parser.ast.select.AllColumns;
import com.serwios.amberdb.sql.parser.ast.select.ColumnSelectItem;
import com.serwios.amberdb.sql.parser.ast.select.SelectItem;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Statement parseStatement() {
        TokenType type = peek().getType();
        return switch (type) {
            case CREATE -> parseCreateTable();
            case INSERT -> parseInsert();
            case SELECT -> parseSelect();
            default -> throw error("Unexpected token: " + peek(), peek());
        };
    }

    private CreateTableStatement parseCreateTable() {
        expect(TokenType.CREATE, "Expected CREATE");
        expect(TokenType.TABLE, "Expected TABLE after CREATE");
        String tableName = expectIdentifier("Expected table name after TABLE");
        expect(TokenType.LPAREN, "Expected '(' after table name");

        List<ColumnDefinition> columns = new ArrayList<>();

        if (peek().getType() == TokenType.RPAREN) {
            throw error("CREATE TABLE must have at least one column", peek());
        }

        String columnName = expectIdentifier("Expected column name");
        ColumnType columnType = parseColumnType();
        columns.add(new ColumnDefinition(columnName, columnType));

        while (match(TokenType.COMMA)) {
            columnName = expectIdentifier("Expected column name after ','");
            columnType = parseColumnType();
            columns.add(new ColumnDefinition(columnName, columnType));
        }

        expect(TokenType.RPAREN, "Expected ')' after column list");
        match(TokenType.SEMICOLON);

        if (columns.isEmpty()) {
            throw error("CREATE TABLE must have at least one column", previous());
        }

        return new CreateTableStatement(tableName, columns);
    }

    private ColumnType parseColumnType() {
        Token token = advance();
        return switch (token.getType()) {
            case INT -> ColumnType.INT;
            case BIGINT -> ColumnType.BIGINT;
            case TEXT -> ColumnType.TEXT;
            default -> throw error("Unexpected column type: " + token, token);
        };
    }

    private InsertStatement parseInsert() {
        expect(TokenType.INSERT, "Expected INSERT");
        expect(TokenType.INTO, "Expected INTO after INSERT");
        String tableName = expectIdentifier("Expected table name after INTO");
        expect(TokenType.VALUES, "Expected VALUES");
        expect(TokenType.LPAREN, "Expected '(' after VALUES");

        List<Expression> values = new ArrayList<>();

        if (peek().getType() == TokenType.RPAREN) {
            throw error("VALUES list cannot be empty", peek());
        }

        values.add(parseValueExpression());
        while (match(TokenType.COMMA)) {
            values.add(parseValueExpression());
        }

        expect(TokenType.RPAREN, "Expected ')' after VALUES list");
        match(TokenType.SEMICOLON);

        return new InsertStatement(tableName, values);
    }

    private Expression parseValueExpression() {
        Token token = peek();
        return switch (token.getType()) {
            case INTEGER_LITERAL -> {
                advance();
                yield new LiteralIntExpression(Integer.parseInt(token.getLexeme()));
            }
            case STRING_LITERAL -> {
                advance();
                yield new LiteralStringExpression(token.getLexeme());
            }
            case NULL -> {
                advance();
                yield new LiteralNullExpression();
            }
            default -> throw error("Unexpected token in VALUES: " + token, token);
        };
    }

    private SelectStatement parseSelect() {
        expect(TokenType.SELECT, "Expected SELECT");
        List<SelectItem> projections = parseProjectionList();
        expect(TokenType.FROM, "Expected FROM after projection list");
        String tableName = expectIdentifier("Expected table name after FROM");

        Expression selection = null;
        if (match(TokenType.WHERE)) {
            if (peek().getType() == TokenType.EOF || peek().getType() == TokenType.SEMICOLON) {
                throw error("Expected expression after WHERE", peek());
            }
            selection = parseExpression();
        }

        Integer limit = null;
        if (match(TokenType.LIMIT)) {
            Token limitToken = expect(TokenType.INTEGER_LITERAL, "Expected integer after LIMIT");
            limit = Integer.parseInt(limitToken.getLexeme());
        }

        match(TokenType.SEMICOLON);

        return new SelectStatement(projections, tableName, selection, limit);
    }

    private List<SelectItem> parseProjectionList() {
        List<SelectItem> items = new ArrayList<>();

        if (match(TokenType.STAR)) {
            items.add(new AllColumns());
            return items;
        }

        if (peek().getType() != TokenType.IDENTIFIER) {
            throw error("Expected column name or '*' after SELECT", peek());
        }

        items.add(new ColumnSelectItem(expectIdentifier("Expected column name")));
        while (match(TokenType.COMMA)) {
            items.add(new ColumnSelectItem(expectIdentifier("Expected column name after ','")));
        }

        return items;
    }

    private Expression parseExpression() {
        return parseOr();
    }

    private Expression parseOr() {
        Expression expr = parseAnd();
        while (match(TokenType.OR)) {
            Expression right = parseAnd();
            expr = new LogicalExpression(expr, LogicalOperator.OR, right);
        }
        return expr;
    }

    private Expression parseAnd() {
        Expression expr = parseComparison();
        while (match(TokenType.AND)) {
            Expression right = parseComparison();
            expr = new LogicalExpression(expr, LogicalOperator.AND, right);
        }
        return expr;
    }

    private Expression parseComparison() {
        Expression left = parsePrimary();

        if (matchOneOf(TokenType.EQ, TokenType.NEQ, TokenType.LT, TokenType.LTE, TokenType.GT, TokenType.GTE)) {
            Token op = previous();
            Expression right = parsePrimary();
            ComparisonOperator operator = switch (op.getType()) {
                case EQ -> ComparisonOperator.EQ;
                case NEQ -> ComparisonOperator.NEQ;
                case LT -> ComparisonOperator.LT;
                case LTE -> ComparisonOperator.LTE;
                case GT -> ComparisonOperator.GT;
                case GTE -> ComparisonOperator.GTE;
                default -> throw error("Unexpected comparison operator: " + op, op);
            };
            return new ComparisonExpression(left, operator, right);
        }

        return left;
    }

    private Expression parsePrimary() {
        Token token = peek();
        return switch (token.getType()) {
            case LPAREN -> {
                advance();
                Expression inner = parseExpression();
                expect(TokenType.RPAREN, "Expected ')' after expression");
                yield inner;
            }
            case IDENTIFIER -> {
                advance();
                yield new ColumnRefExpression(token.getLexeme());
            }
            case INTEGER_LITERAL -> {
                advance();
                yield new LiteralIntExpression(Integer.parseInt(token.getLexeme()));
            }
            case STRING_LITERAL -> {
                advance();
                yield new LiteralStringExpression(token.getLexeme());
            }
            case NULL -> {
                advance();
                yield new LiteralNullExpression();
            }
            default -> throw error("Unexpected token in expression: " + token, token);
        };
    }

    private Token peek() {
        return tokens.get(position);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) {
            position++;
        }
        return previous();
    }

    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            advance();
            return true;
        }
        return false;
    }

    private boolean matchOneOf(TokenType... types) {
        for (TokenType type : types) {
            if (peek().getType() == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token expect(TokenType type, String message) {
        Token token = peek();
        if (token.getType() != type) {
            throw error(message + ", found: " + token, token);
        }
        advance();
        return token;
    }

    private String expectIdentifier(String message) {
        Token token = expect(TokenType.IDENTIFIER, message);
        return token.getLexeme();
    }

    private ParseException error(String message, Token token) {
        return new ParseException(message, token.getPosition(), token);
    }
}
