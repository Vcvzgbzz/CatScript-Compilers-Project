package edu.montana.csci.csci468.eval;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NewEvalTests extends CatscriptTestBase {

    @Test
    void functionCallWithForAndIf() {
        //assertEquals(false, evaluateExpression("1 > 2"));

        assertEquals("Ending Number:6\n", executeProgram("var x = [1,2,3,4,5]\n" +
                "var counter = 0\n" +
                "function counterIncrement(){\n" +
                "if(counter==0){\n" +
                "    for(i in x){\n" +
                "        if(i==4){\n" +
                "            counter=counter+2\n" +
                "        }else{\n" +
                "            counter = counter + 1\n" +
                "        }\n" +
                "        \n" +
                "    }\n" +
                "}\n" +
                "}\n" +
                "counterIncrement()\n" +
                "print(\"Ending Number:\"+counter)\n"));
    }
    @Test
    void weirdMath() {
        assertEquals("59618\n", executeProgram("function recursiveSolution(number:int){\n" +
                "    if(number<10000){\n" +
                "        number=(number*2)/3+(number*7+2)\n" +
                "        recursiveSolution(number)\n" +
                "    }else{\n" +
                "        return(number)\n" +
                "    }\n" +
                "} \n" +
                "var number = 0\n" +
                "recursiveSolution(number)\n" +
                "print(number)"));

    }
}
