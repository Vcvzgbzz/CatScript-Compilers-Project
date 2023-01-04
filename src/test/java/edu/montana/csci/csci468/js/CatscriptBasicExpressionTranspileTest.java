package edu.montana.csci.csci468.js;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatscriptBasicExpressionTranspileTest extends CatscriptTestBase {

    @Test
    void literalExpressionsEvaluatesProperly() {
        assertEquals("1\n", transpile("1"));
        assertEquals("true\n", transpile("true"));
        assertEquals("null\n", transpile("null"));
        assertEquals("asdf\n", transpile("\"asdf\""));
        assertEquals("1,2,3\n", transpile("[1, 2, 3]"));
    }

    @Test
    void unaryExpressionEvaluatesProperly() {
        assertEquals("-1\n", transpile("-1"));
        assertEquals("false\n", transpile("not true"));
    }

    @Test
    void factorExpressionEvaluatesProperly() {
        assertEquals("2\n", transpile("1 * 2"));
        assertEquals("4\n", transpile("8 / 2"));
        assertEquals("4\n", transpile("2 * 4 / 2"));
    }

    @Test
    void additiveExpressionEvaluatesProperly() {
        assertEquals("2\n", transpile("1 + 1"));
        assertEquals("0\n", transpile("1 - 1"));
        assertEquals("-2\n", transpile("1 - 2 - 1"));
    }

    @Test
    void additiveStringExpressionEvaluatesProperly() {
        assertEquals("1a\n", transpile("1 + \"a\""));
        assertEquals("a1\n", transpile("\"a\" + 1"));
        assertEquals("nulla\n", transpile("null + \"a\""));
        assertEquals("anull\n", transpile("\"a\" + null"));
    }

    @Test
    void comparisonExpressionEvaluatesProperly() {
        assertEquals("false\n", transpile("1 > 2"));
        assertEquals("false\n", transpile("1 >= 2"));
        assertEquals("true\n", transpile("1 <= 2"));
        assertEquals("true\n", transpile("1 < 2"));

        assertEquals("true\n", transpile("2 > 1"));
        assertEquals("true\n", transpile("2 >= 1"));
        assertEquals("false\n", transpile("2 <= 1"));
        assertEquals("false\n", transpile("2 < 1"));

        assertEquals("false\n", transpile("1 > 1"));
        assertEquals("true\n", transpile("1 >= 1"));
        assertEquals("true\n", transpile("1 <= 1"));
        assertEquals("false\n", transpile("2 < 1"));
    }

    @Test
    void equalityExpressionEvaluatesProperly() {
        assertEquals("true\n", transpile("1 == 1"));
        assertEquals("true\n", transpile("true == true"));
        assertEquals("true\n", transpile("null == null"));
        assertEquals("true\n", transpile("true != null"));
        // TODO javascript semantics
        // assertEquals("true\n", transpile("true != 1"));
    }

    @Test
    void parenthesizedExpressionEvaluatesProperly() {
        assertEquals("1\n", transpile("(1)"));
    }

}
