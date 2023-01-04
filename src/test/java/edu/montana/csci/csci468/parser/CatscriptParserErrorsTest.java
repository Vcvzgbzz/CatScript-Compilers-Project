package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.CatscriptTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CatscriptParserErrorsTest extends CatscriptTestBase {

    @Test
    public void basicSymbolErrorWorks() {
        List<ParseError> errors = getErrors("asdf");
        assertError(errors, 0, ErrorType.UNKNOWN_NAME, 1, 0);
    }

    private void assertError(List<ParseError> errors, int errorIndex, ErrorType errorType) {
        assertError(errors, errorIndex, errorType, -1, -1);
    }
    private void assertError(List<ParseError> errors, int errorIndex, ErrorType errorType, int line, int lineOffset) {
        if (errorIndex >= errors.size()) {
            fail("No error at index " + errorIndex);
        } else {
            ParseError parseError = errors.get(errorIndex);
            assertEquals(errorType, parseError.getErrorType());
            if (line > 0) {
                assertEquals(line, parseError.getLocation().getLine());
            }
            if (lineOffset > 0) {
                assertEquals(lineOffset, parseError.getLocation().getLineOffset());
            }
        }
    }


}
