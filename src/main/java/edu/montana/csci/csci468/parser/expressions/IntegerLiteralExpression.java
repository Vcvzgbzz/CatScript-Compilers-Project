package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.SymbolTable;

public class IntegerLiteralExpression extends Expression {
    private final int integerVal;

    public IntegerLiteralExpression(String value) {
        this.integerVal = Integer.parseInt(value);
    }

    public int getValue() {
        return integerVal;
    }

    @Override
    public String toString() {
        return integerVal + "";
    }

    @Override
    public void validate(SymbolTable symbolTable) {}

    @Override
    public CatscriptType getType() {
        return CatscriptType.INT;
    }

    //==============================================================
    // Implementation
    //==============================================================

    @Override
    public Object evaluate(CatscriptRuntime runtime) {
        return integerVal;
    }

    @Override
    public void transpile(StringBuilder javascript) {
        javascript.append(integerVal);
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        code.pushConstantOntoStack(integerVal);
    }
}
