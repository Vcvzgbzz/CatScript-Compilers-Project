package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.SymbolTable;
import edu.montana.csci.csci468.tokenizer.Token;

public class SyntaxErrorExpression extends Expression {

    public SyntaxErrorExpression(Token consumeToken) {
        setToken(consumeToken);
        addError(ErrorType.UNEXPECTED_TOKEN);
    }

    @Override
    public CatscriptType getType() {
        return CatscriptType.OBJECT;
    }

    @Override
    public void validate(SymbolTable symbolTable) {}

    //==============================================================
    // Implementation
    //==============================================================

    @Override
    public Object evaluate(CatscriptRuntime runtime) {
        throw new IllegalStateException("Bad token : " + getStart());
    }

    @Override
    public void transpile(StringBuilder javascript) {
        throw new IllegalStateException("Bad token : " + getStart());
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        throw new IllegalStateException("Bad token : " + getStart());
    }

}
