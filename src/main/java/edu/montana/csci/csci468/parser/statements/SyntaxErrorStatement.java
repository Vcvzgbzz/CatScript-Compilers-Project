package edu.montana.csci.csci468.parser.statements;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.SymbolTable;
import edu.montana.csci.csci468.tokenizer.Token;

public class SyntaxErrorStatement extends Statement {

    public SyntaxErrorStatement(Token start) {
        setToken(start);
        addError(ErrorType.UNEXPECTED_TOKEN);
    }

    @Override
    public void validate(SymbolTable symbolTable) {}

    //==============================================================
    // Implementation
    //==============================================================
    @Override
    public void execute(CatscriptRuntime runtime) {
        throw new IllegalStateException("Bad token : " + getStart());
    }

    @Override
    public void transpile(StringBuilder javascript) {
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        super.compile(code);
    }

}
