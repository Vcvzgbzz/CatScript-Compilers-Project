package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.ParseError;
import edu.montana.csci.csci468.parser.SymbolTable;
import edu.montana.csci.csci468.tokenizer.Token;
import edu.montana.csci.csci468.tokenizer.TokenType;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class UnaryExpression extends Expression {

    private final Token operator;
    private final Expression rightHandSide;

    public UnaryExpression(Token operator, Expression rightHandSide) {
        this.rightHandSide = addChild(rightHandSide);
        this.operator = operator;
    }

    public Expression getRightHandSide() {
        return rightHandSide;
    }

    public boolean isMinus() {
        return operator.getType().equals(TokenType.MINUS);
    }

    public boolean isNot() {
        return !isMinus();
    }

    @Override
    public String toString() {
        return super.toString() + "[" + operator.getStringValue() + "]";
    }

    @Override
    public void validate(SymbolTable symbolTable) {
        rightHandSide.validate(symbolTable);
        if (isNot() && !rightHandSide.getType().equals(CatscriptType.BOOLEAN)) {
            addError(ErrorType.INCOMPATIBLE_TYPES);
        } else if(isMinus() && !rightHandSide.getType().equals(CatscriptType.INT)) {
            addError(ErrorType.INCOMPATIBLE_TYPES);
        }
    }

    @Override
    public CatscriptType getType() {
        if (isMinus()) {
            return CatscriptType.INT;
        } else {
            return CatscriptType.BOOLEAN;
        }
    }

    //==============================================================
    // Implementation
    //==============================================================

    @Override
    public Object evaluate(CatscriptRuntime runtime) {

        if (this.isMinus()) {
            Object rhsValue = getRightHandSide().evaluate(runtime);
            return -1 * (Integer) rhsValue;
        } else if(operator.getType().equals(TokenType.NOT)) {
            Boolean rhsValue = (Boolean) getRightHandSide().evaluate(runtime);
            return  !rhsValue;
        }
        return null;
    }

    @Override
    public void transpile(StringBuilder javascript) {
        super.transpile(javascript);
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        Label setTrue = new Label();
        Label end = new Label();
        rightHandSide.compile(code);
        if (isMinus()) {
            code.addInstruction(Opcodes.INEG);

        } else if(isNot()){
            code.addJumpInstruction(Opcodes.IFNE, setTrue);
            code.addInstruction(Opcodes.ICONST_1);
            code.addJumpInstruction(Opcodes.GOTO, end);
            code.addLabel(setTrue);
            code.addInstruction(Opcodes.ICONST_0);
            code.addLabel(end);

        }


    }


}
