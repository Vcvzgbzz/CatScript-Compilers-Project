package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.CatscriptTestBase;
import edu.montana.csci.csci468.parser.expressions.IntegerLiteralExpression;
import edu.montana.csci.csci468.parser.statements.CatScriptProgram;
import edu.montana.csci.csci468.parser.statements.PrintStatement;
import edu.montana.csci.csci468.parser.statements.Statement;
import edu.montana.csci.csci468.parser.statements.VariableStatement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatscriptParserTypeCheckingTest extends CatscriptTestBase {

    @Test
    void testBasicExpressionsHaveProperType() {
        assertEquals(CatscriptType.INT, parseExpression("1").getType());
        assertEquals(CatscriptType.STRING, parseExpression("\"\"").getType());
        assertEquals(CatscriptType.BOOLEAN, parseExpression("true").getType());
        assertEquals(CatscriptType.BOOLEAN, parseExpression("false").getType());
        assertEquals(CatscriptType.NULL, parseExpression("null").getType());
        assertEquals(CatscriptType.INT, parseExpression("1 + 1").getType());
        assertEquals(CatscriptType.STRING, parseExpression("\"\" + 1").getType());
        assertEquals(CatscriptType.INT, parseExpression("1 * 1").getType());
        assertEquals(CatscriptType.BOOLEAN, parseExpression("1 > 1").getType());
        assertEquals(CatscriptType.BOOLEAN, parseExpression("1 == 1").getType());
        assertEquals(CatscriptType.INT, parseExpression("-1").getType());
        assertEquals(CatscriptType.BOOLEAN, parseExpression("not true").getType());
    }

    @Test
    void identifiersWorkProperly() {
        PrintStatement print = parseStatement("var x = 1 print( x )", 1);
        assertEquals(CatscriptType.INT, print.getExpression().getType());
    }

    @Test
    void functionCallsWorkProperly() {
        PrintStatement print = parseStatement("function x():int{return 10} print( x() )", 1);
        assertEquals(CatscriptType.INT, print.getExpression().getType());
    }

    @Test
    void typeInferenceWorksForVars() {
        VariableStatement var = parseStatement("var x = 10");
        assertEquals(CatscriptType.INT, var.getType());
    }

    @Test
    void varTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("var x : bool = 10"));
    }

    @Test
    void assignmentTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("var x = 10\n" +
                "x = true"));
    }

    @Test
    void unaryTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("not 1"));
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("- true"));
    }

    @Test
    void factorTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("true * false"));
    }

    @Test
    void additiveTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("1 + true"));
    }

    @Test
    void comparativeTypeError() {
        assertEquals(ErrorType.INCOMPATIBLE_TYPES, getParseError("1 > true"));
    }


}
