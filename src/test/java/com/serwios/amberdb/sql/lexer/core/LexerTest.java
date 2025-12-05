package com.serwios.amberdb.sql.lexer.core;

import com.serwios.amberdb.sql.lexer.model.LexException;
import com.serwios.amberdb.sql.lexer.model.Token;
import com.serwios.amberdb.sql.lexer.model.TokenType;
import com.serwios.amberdb.sql.lexer.scanners.IdentifierScanner;
import com.serwios.amberdb.sql.lexer.scanners.NumberScanner;
import com.serwios.amberdb.sql.lexer.scanners.StringScanner;
import com.serwios.amberdb.sql.lexer.scanners.SymbolScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {
                Lexer.class,
                IdentifierScanner.class,
                NumberScanner.class,
                StringScanner.class,
                SymbolScanner.class,
                WhitespaceSkipper.class
        }
)
class LexerTest {
    @Autowired
    private Lexer lexer;

    private List<Token> tokenize(String sql) {
        return lexer.tokenize(sql);
    }

    private void assertTokenTypes(String sql, TokenType... expectedTypes) {
        List<Token> tokens = tokenize(sql);
        assertEquals(expectedTypes.length, tokens.size(),
                "Unexpected token count for SQL: " + sql + " -> " + tokens);

        for (int i = 0; i < expectedTypes.length; i++) {
            TokenType expected = expectedTypes[i];
            TokenType actual = tokens.get(i).getType();
            assertEquals(expected, actual,
                    "Unexpected token type at index " + i + " for SQL: " + sql
                            + ", tokens=" + tokens);
        }
    }

    @Test
    @DisplayName("Empty input -> EOF")
    void emptyInputProducesOnlyEof() {
        assertTokenTypes(
                "",
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("Whitespace is ignored")
    void ignoresWhitespaceAndNewlines() {
        assertTokenTypes(
                "   \n\t  SELECT  \n  *  \r\n FROM   users   ",
                TokenType.SELECT,
                TokenType.STAR,
                TokenType.FROM,
                TokenType.IDENTIFIER,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("SELECT * FROM table")
    void simpleSelectStarFromTable() {
        assertTokenTypes(
                "SELECT * FROM home",
                TokenType.SELECT,
                TokenType.STAR,
                TokenType.FROM,
                TokenType.IDENTIFIER,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("SELECT with multiple columns")
    void selectMultipleColumns() {
        assertTokenTypes(
                "SELECT id, name, age FROM users",
                TokenType.SELECT,
                TokenType.IDENTIFIER,
                TokenType.COMMA,
                TokenType.IDENTIFIER,
                TokenType.COMMA,
                TokenType.IDENTIFIER,
                TokenType.FROM,
                TokenType.IDENTIFIER,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("Keywords are case-insensitive")
    void keywordsAreCaseInsensitive() {
        assertTokenTypes(
                "select Id, NAME from USERS where Age >= 18 LIMIT 10",
                TokenType.SELECT,
                TokenType.IDENTIFIER,
                TokenType.COMMA,
                TokenType.IDENTIFIER,
                TokenType.FROM,
                TokenType.IDENTIFIER,
                TokenType.WHERE,
                TokenType.IDENTIFIER,
                TokenType.GTE,
                TokenType.INTEGER_LITERAL,
                TokenType.LIMIT,
                TokenType.INTEGER_LITERAL,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("INSERT INTO ... VALUES with numbers and strings")
    void insertIntoValuesWithNumbersAndStrings() {
        assertTokenTypes(
                "INSERT INTO users VALUES (1, 'Alice', 42);",
                TokenType.INSERT,
                TokenType.INTO,
                TokenType.IDENTIFIER,
                TokenType.VALUES,
                TokenType.LPAREN,
                TokenType.INTEGER_LITERAL,
                TokenType.COMMA,
                TokenType.STRING_LITERAL,
                TokenType.COMMA,
                TokenType.INTEGER_LITERAL,
                TokenType.RPAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("INSERT INTO ... VALUES with NULL")
    void insertWithNull() {
        assertTokenTypes(
                "INSERT INTO t VALUES (NULL);",
                TokenType.INSERT,
                TokenType.INTO,
                TokenType.IDENTIFIER,
                TokenType.VALUES,
                TokenType.LPAREN,
                TokenType.NULL,
                TokenType.RPAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("CREATE TABLE with several columns")
    void createTableBasic() {
        assertTokenTypes(
                "CREATE TABLE users (id INT, name TEXT, age BIGINT);",
                TokenType.CREATE,
                TokenType.TABLE,
                TokenType.IDENTIFIER,
                TokenType.LPAREN,
                TokenType.IDENTIFIER,
                TokenType.INT,
                TokenType.COMMA,
                TokenType.IDENTIFIER,
                TokenType.TEXT,
                TokenType.COMMA,
                TokenType.IDENTIFIER,
                TokenType.BIGINT,
                TokenType.RPAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("SELECT with WHERE, AND/OR and LIMIT")
    void selectWithWhereAndLimit() {
        assertTokenTypes(
                "SELECT id FROM users WHERE age >= 18 AND name = 'Alice' OR active = 1 LIMIT 10",
                TokenType.SELECT,
                TokenType.IDENTIFIER,
                TokenType.FROM,
                TokenType.IDENTIFIER,
                TokenType.WHERE,
                TokenType.IDENTIFIER,
                TokenType.GTE,
                TokenType.INTEGER_LITERAL,
                TokenType.AND,
                TokenType.IDENTIFIER,
                TokenType.EQ,
                TokenType.STRING_LITERAL,
                TokenType.OR,
                TokenType.IDENTIFIER,
                TokenType.EQ,
                TokenType.INTEGER_LITERAL,
                TokenType.LIMIT,
                TokenType.INTEGER_LITERAL,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("Negative integer literal")
    void negativeIntegerLiteral() {
        assertTokenTypes(
                "INSERT INTO t VALUES (-5);",
                TokenType.INSERT,
                TokenType.INTO,
                TokenType.IDENTIFIER,
                TokenType.VALUES,
                TokenType.LPAREN,
                TokenType.INTEGER_LITERAL,
                TokenType.RPAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("Multiple statements on one line")
    void multipleStatementsSameLine() {
        assertTokenTypes(
                "SELECT 1; SELECT 2;",
                TokenType.SELECT,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.SELECT,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    @DisplayName("Unknown character -> LexException")
    void unknownCharacterThrows() {
        assertThrows(
                LexException.class,
                () -> tokenize("SELECT @ FROM users")
        );
    }

    @Test
    @DisplayName("Unterminated string literal -> LexException")
    void unterminatedStringLiteralThrows() {
        assertThrows(
                LexException.class,
                () -> tokenize("SELECT 'hello")
        );
    }

    @Test
    @DisplayName("Minus without digit -> LexException")
    void minusWithoutDigitThrows() {
        assertThrows(
                LexException.class,
                () -> tokenize("INSERT INTO t VALUES (-);")
        );
    }

    @Test
    @DisplayName("Exclamation without '=' -> LexException")
    void exclamationWithoutEqualsThrows() {
        assertThrows(
                LexException.class,
                () -> tokenize("SELECT * FROM t WHERE x ! 1")
        );
    }
}
