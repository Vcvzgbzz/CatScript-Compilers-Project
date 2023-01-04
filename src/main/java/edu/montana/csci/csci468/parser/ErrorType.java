package edu.montana.csci.csci468.parser;

public enum ErrorType {
    UNTERMINATED_LIST("Unterminated list literal"),
    UNTERMINATED_ARG_LIST("Unterminated argument list"),
    BAD_TYPE_NAME("Bad Type Name"),
    DUPLICATE_NAME("This name is already used in this program"),
    INCOMPATIBLE_TYPES("Incompatible types"),
    UNKNOWN_NAME("This symbol is not defined"),
    ARG_MISMATCH("Wrong number of arguments"),
    MISSING_RETURN_STATEMENT("Missing return statement in function"),
    UNEXPECTED_TOKEN("Unexpected Token");

    private final String message;

    ErrorType(String string) {
        message = string;
    }

    @Override
    public String toString() {
        return message;
    }
}
