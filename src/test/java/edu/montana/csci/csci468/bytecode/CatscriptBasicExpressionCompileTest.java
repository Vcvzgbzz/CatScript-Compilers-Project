package edu.montana.csci.csci468.bytecode;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatscriptBasicExpressionCompileTest extends CatscriptTestBase {

    @Test
    void literalExpressionsCompilesProperly() {
        assertEquals("1\n", compile("1"));
        assertEquals("true\n", compile("true"));
        assertEquals("null\n", compile("null"));
        assertEquals("asdf\n", compile("\"asdf\""));
        assertEquals("[1, 2, 3]\n", compile("[1, 2, 3]"));
    }

    @Test
    void unaryExpressionCompilesProperly() {
        assertEquals("-1\n", compile("-1"));
        assertEquals("false\n", compile("not true"));
    }

    @Test
    void factorExpressionCompilesProperly() {
        assertEquals("2\n", compile("1 * 2"));
        assertEquals("4\n", compile("8 / 2"));
        assertEquals("4\n", compile("2 * 4 / 2"));
    }

    @Test
    void additiveExpressionCompilesProperly() {
        assertEquals("2\n", compile("1 + 1"));
        assertEquals("0\n", compile("1 - 1"));
        assertEquals("-2\n", compile("1 - 2 - 1"));
    }

    @Test
    void additiveStringExpressionCompilesProperly() {
        assertEquals("1a\n", compile("1 + \"a\""));
        assertEquals("a1\n", compile("\"a\" + 1"));
        assertEquals("nulla\n", compile("null + \"a\""));
        assertEquals("anull\n", compile("\"a\" + null"));
    }

    @Test
    void comparisonExpressionCompilesProperly() {
        assertEquals("false\n", compile("1 > 2"));
        assertEquals("false\n", compile("1 >= 2"));
        assertEquals("true\n", compile("1 <= 2"));
        assertEquals("true\n", compile("1 < 2"));

        assertEquals("true\n", compile("2 > 1"));
        assertEquals("true\n", compile("2 >= 1"));
        assertEquals("false\n", compile("2 <= 1"));
        assertEquals("false\n", compile("2 < 1"));

        assertEquals("false\n", compile("1 > 1"));
        assertEquals("true\n", compile("1 >= 1"));
        assertEquals("true\n", compile("1 <= 1"));
        assertEquals("false\n", compile("2 < 1"));
    }

    @Test
    void equalityExpressionCompilesProperly() {
        assertEquals("true\n", compile("1 == 1"));
        assertEquals("true\n", compile("true == true"));
        assertEquals("true\n", compile("null == null"));
        assertEquals("true\n", compile("true != null"));
        assertEquals("true\n", compile("true != 1"));
    }

    @Test
    void parenthesizedExpressionCompilesProperly() {
        assertEquals("1\n", compile("(1)"));
    }

}
