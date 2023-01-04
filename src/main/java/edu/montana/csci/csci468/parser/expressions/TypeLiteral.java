package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.SymbolTable;

public class TypeLiteral extends Expression {

    private CatscriptType type;

    public void setType(CatscriptType type) {
        this.type = type;
    }

    @Override
    public CatscriptType getType() {
        return type;
    }

    @Override
    public void validate(SymbolTable symbolTable) {}

    //==============================================================
    // Implementation
    //==============================================================

    @Override
    public Object evaluate(CatscriptRuntime runtime) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public void transpile(StringBuilder javascript) {
        throw new IllegalStateException("Not Supported");
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        throw new IllegalStateException("Not Supported");
    }

}
