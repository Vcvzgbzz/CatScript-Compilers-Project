package edu.montana.csci.csci468.parser;

import java.util.List;

public class ParseErrorException extends RuntimeException {
    private final List<ParseError> errors;

    public ParseErrorException(List<ParseError> errors) {
        super(makeMessage(errors));
        this.errors = errors;
    }

    private static String makeMessage(List<ParseError> errors) {
        StringBuilder errorMessage = new StringBuilder("Parse Errors Occurred:\n\n");
        for (ParseError error : errors) {
            errorMessage.append(error.getFullMessage()).append("\n\n");
        }
        return errorMessage.toString();
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}
