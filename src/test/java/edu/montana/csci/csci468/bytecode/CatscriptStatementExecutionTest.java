package edu.montana.csci.csci468.bytecode;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatscriptStatementExecutionTest extends CatscriptTestBase {

    @Test
    void printStatementWorksProperly() {
        assertEquals("1\n", compile("print(1)"));
        assertEquals("1\n2\n", compile("print(1)\n" +
                "print(2)"));
        assertEquals("true\n", compile("print(true)"));
        assertEquals("[1, 2, 3]\n", compile("print([1, 2, 3])"));
    }

    @Test
    void ifStatementWorksProperly() {
        assertEquals("1\n", compile("if(true){ print(1) }"));
        assertEquals("", compile("if(false){ print(1) }"));
        assertEquals("1\n", compile("if(true){ print(1) } else { print(2) }"));
        assertEquals("2\n", compile("if(false){ print(1) }  else { print(2) }"));
    }

    @Test
    void varStatementWorksProperly() {
        assertEquals("1\n", compile("var x = 1\n" +
                "print(x)"));
        assertEquals("1\n", compile("var x = 1\n" +
                "var y = x\n" +
                "print(y)"));
        assertEquals("null\n", compile("var x = null\n" +
                "print(x)"));
    }

    @Test
    void localVarStatementsWorkProperly() {
        assertEquals("1\n2\n3\n", compile("for( x in [1, 2, 3] ) {\n" +
                "  var y = x\n" +
                "  print(y)\n" +
                "}\n"));
    }

    @Test
    void varInsideFunctionWorksProperly() {
        assertEquals("42\n", compile("function foo() : int {\n" +
                "  var x = 42\n" +
                "  return x\n" +
                "}\n" +
                "print( foo() )\n"));

    }

    @Test
    void forStatementWorksProperly() {
        assertEquals("1\n2\n3\n", compile("for(x in [1, 2, 3]) { print(x) }"));
    }

    @Test
    void functionDeclarationWorksProperly() {
        assertEquals("1\n2\n3\n", compile("function foo(x) { print(x) }\n" +
                "foo(1)\n" +
                "foo(2)\n" +
                "foo(3)"
        ));
    }

    @Test
    void recursiveFunctionWorksProperly() {
        assertEquals("9\n8\n7\n6\n5\n4\n3\n2\n1\n0\n", compile(
                "function foo(x : int) {\n" +
                        "print(x)" +
                        "if(x > 0) {" +
                        "  foo(x - 1)" +
                        "}" +
                "}\n" +
                "foo(9)"
        ));
    }

    @Test
    void returnStatementWorks() {
        assertEquals("10\n", compile(
                "function foo(x : int) : int {\n" +
                        "return x + 1" +
                "}\n" +
                "print(foo(9))"
        ));
    }


}
