package edu.montana.csci.csci468.eval;

public class ReturnException extends RuntimeException {
    private Object value;
    public ReturnException(Object value) {
        this.value = value;
    }
    public Object getValue() {
        return value;
    }
}
