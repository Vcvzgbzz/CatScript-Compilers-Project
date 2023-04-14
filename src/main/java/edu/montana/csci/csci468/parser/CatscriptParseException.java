package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.tokenizer.Token;

public class CatscriptParseException extends RuntimeException {
    private final Token token;

    public CatscriptParseException(Token token) {
        this.token = token;
    }
    public Token getToken(){
        return token;
    }
}
