package edu.montana.csci.csci468.eval;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CatscriptBasicExpressionEvalTest extends CatscriptTestBase {

    @Test
    void literalExpressionsEvaluatesProperly() {
        assertEquals(1, evaluateExpression("1"));
        assertEquals(true, evaluateExpression("true"));
        assertNull(evaluateExpression("null"));
        assertEquals("asdf", evaluateExpression("\"asdf\""));
        assertEquals(Arrays.asList(1, 2, 3), evaluateExpression("[1, 2, 3]"));
    }

    @Test
    void unaryExpressionEvaluatesProperly() {
        assertEquals(-1, evaluateExpression("-1"));
        assertEquals(false, evaluateExpression("not true"));
    }

    @Test
    void factorExpressionEvaluatesProperly() {
        assertEquals(2, evaluateExpression("1 * 2"));
        assertEquals(4, evaluateExpression("8 / 2"));
        assertEquals(4, evaluateExpression("2 * 4 / 2"));
    }

    @Test
    void additiveExpressionEvaluatesProperly() {
        assertEquals(2, evaluateExpression("1 + 1"));
        assertEquals(0, evaluateExpression("1 - 1"));
        assertEquals(-2, evaluateExpression("1 - 2 - 1"));
    }

    @Test
    void additiveStringExpressionEvaluatesProperly() {
        assertEquals("1a", evaluateExpression("1 + \"a\""));
        assertEquals("a1", evaluateExpression("\"a\" + 1"));
        assertEquals("nulla", evaluateExpression("null + \"a\""));
        assertEquals("anull", evaluateExpression("\"a\" + null"));
    }

    @Test
    void comparisonExpressionEvaluatesProperly() {
        assertEquals(false, evaluateExpression("1 > 2"));
        assertEquals(false, evaluateExpression("1 >= 2"));
        assertEquals(true, evaluateExpression("1 <= 2"));
        assertEquals(true, evaluateExpression("1 < 2"));

        assertEquals(true, evaluateExpression("2 > 1"));
        assertEquals(true, evaluateExpression("2 >= 1"));
        assertEquals(false, evaluateExpression("2 <= 1"));
        assertEquals(false, evaluateExpression("2 < 1"));

        assertEquals(false, evaluateExpression("1 > 1"));
        assertEquals(true, evaluateExpression("1 >= 1"));
        assertEquals(true, evaluateExpression("1 <= 1"));
        assertEquals(false, evaluateExpression("2 < 1"));
    }

    @Test
    void equalityExpressionEvaluatesProperly() {
        assertEquals(true, evaluateExpression("1 == 1"));
        assertEquals(true, evaluateExpression("true == true"));
        assertEquals(true, evaluateExpression("null == null"));
        assertEquals(true, evaluateExpression("true != null"));
        assertEquals(true, evaluateExpression("true != 1"));
    }

    @Test
    void parenthesizedExpressionEvaluatesProperly() {
        assertEquals(1, evaluateExpression("(1)"));
    }

}
