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
    @Test
    void functionCallParameterAndReturn() {
        assertEquals("6\n", executeProgram("var word = [\"q\", \"w\", \"e\" ,\"r\", \"t\", \"y\"]\n" +
                "                function getWordLength(input : list<string>) : int {\n" +
                "                    var length = 0\n" +
                "                    for (c in input) {\n" +
                "                        length = length + 1\n" +
                "                    }\n" +
                "                    return length\n" +
                "                }\n" +
                "                print(getWordLength(word))\n"));
    }

    @Test
    void ifMathBooleanAndReturn() {
        assertEquals("0\n", executeProgram("var number = 20\n" +
                "                function mathGauntlet(input : int) : int {\n" +
                "                    if (input == 1) {\n" +
                "                       return 1 }\n" +
                "                    if (input == 0) {\n" +
                "                       return 0 }\n" +
                "                    if ((-input * (input -1)) >= (input / (input + 1))) {\n" +
                "                       var i = mathGauntlet(1)\n" +
                "                       return i\n" +
                "                    }\n" +
                "                    else {\n" +
                "                       var i = mathGauntlet(0)\n" +
                "                       return i\n" +
                "                    }\n" +
                "                }\n" +
                "                print(mathGauntlet(number))\n"));
    }

    @Test
    void basicTypesAndPrint() {
        assertEquals("a1truenulla\n", executeProgram(
                "    var letter = \"a\"\n" +
                        "        var number = 1\n" +
                        "        var boolean = true\n" +
                        "        var lst = [\"a\"]\n" +
                        "var nuul = null\n" +
                        "        var toPrint = letter + number + boolean + nuul\n" +
                        "        for (i in lst){ " +
                        "   toPrint = toPrint + i" +
                        "   }\n" +
                        "        print(toPrint)"));

    }

    @Test
    void fifthPrint() {
        assertEquals("5\n", executeProgram("function fifthPrint(){\n" +
                "    for(i in [1,2,3,4,5]){\n" +
                "        if(i==5){\n" +
                "            print(i)\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "fifthPrint()"));
    }
}
