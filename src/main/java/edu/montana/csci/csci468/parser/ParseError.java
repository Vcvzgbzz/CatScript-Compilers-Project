package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.tokenizer.Token;

public class ParseError {

    private Token location;
    private ErrorType errorType;
    private String message;

    public ParseError(Token location, ErrorType errorType, Object... args) {
        this.location = location;
        this.errorType = errorType;
        this.message = String.format(errorType.toString(), args);
    }

    public Token getLocation() {
        return location;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getFullMessage() {
        StringBuilder sb = new StringBuilder();
        String lineStart = "Line " + location.getLine() + ":";
        sb.append(lineStart);
        sb.append(location.getLineContent());
        sb.append("\n");
        sb.append(" ".repeat(lineStart.length() + location.getLineOffset() - 1));
        sb.append("^\n\n");
        sb.append("Error: ");
        sb.append(message);
        return sb.toString();
    }
}
