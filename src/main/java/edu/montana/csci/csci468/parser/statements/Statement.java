package edu.montana.csci.csci468.parser.statements;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.ParseElement;

public abstract class Statement extends ParseElement {

    public void execute(CatscriptRuntime runtime) {
        throw new UnsupportedOperationException("execute needs to be implemented for " + this.getClass().getName());
    }

}
