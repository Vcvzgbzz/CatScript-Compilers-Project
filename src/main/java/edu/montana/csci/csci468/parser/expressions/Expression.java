package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.ParseElement;

public abstract class Expression extends ParseElement {

    public Object evaluate(CatscriptRuntime runtime) {
        throw new UnsupportedOperationException("evaluate needs to be implemented for " + this.getClass().getName());
    }

    @Override
    public void transpile(StringBuilder javascript) {
        throw new UnsupportedOperationException("transpile needs to be implemented for " + this.getClass().getName());
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        throw new UnsupportedOperationException("compile needs to be implemented for " + this.getClass().getName());
    }

    public abstract CatscriptType getType();
}
