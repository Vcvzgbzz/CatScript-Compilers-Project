package edu.montana.csci.csci468.parser.statements;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.ParseError;
import edu.montana.csci.csci468.parser.SymbolTable;
import edu.montana.csci.csci468.parser.expressions.Expression;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ForStatement extends Statement {
    private Expression expression;
    private String variableName;
    private List<Statement> body;

    public void setExpression(Expression expression) {
        this.expression = addChild(expression);
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setBody(List<Statement> statements) {
        this.body = new LinkedList<>();
        for (Statement statement : statements) {
            this.body.add(addChild(statement));
        }
    }

    public Expression getExpression() {
        return expression;
    }

    public String getVariableName() {
        return variableName;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public void validate(SymbolTable symbolTable) {
        symbolTable.pushScope();
        if (symbolTable.hasSymbol(variableName)) {
            addError(ErrorType.DUPLICATE_NAME);
        } else {
            expression.validate(symbolTable);
            CatscriptType type = expression.getType();
            if (type instanceof CatscriptType.ListType) {
                symbolTable.registerSymbol(variableName, getComponentType());
            } else {
                addError(ErrorType.INCOMPATIBLE_TYPES, getStart());
                symbolTable.registerSymbol(variableName, CatscriptType.OBJECT);
            }
        }
        for (Statement statement : body) {
            statement.validate(symbolTable);
        }
        symbolTable.popScope();
    }

    private CatscriptType getComponentType() {
        return ((CatscriptType.ListType) expression.getType()).getComponentType();
    }

    //==============================================================
    // Implementation
    //==============================================================
    @Override
    public void execute(CatscriptRuntime runtime) {

        Iterable listToIterateOver = (Iterable) expression.evaluate(runtime);
        runtime.pushScope();
        for(Object currentValue:listToIterateOver){
            runtime.setValue(variableName,currentValue);
            for(Statement statement:body){
                statement.execute(runtime);
            }
        }
        runtime.popScope();
    }

    @Override
    public void transpile(StringBuilder javascript) {
        super.transpile(javascript);
    }

    @Override
    public void compile(ByteCodeGenerator code) {

        Integer iteratorSlot = code.nextLocalStorageSlot();
        Label forLoopStart = new Label();
        Label forLoopEnd = new Label();

        getExpression().compile(code);

        code.addMethodInstruction(Opcodes.INVOKEINTERFACE, ByteCodeGenerator.internalNameFor(List.class), "iterator",
                "()Ljava/util/Iterator;");
        code.addVarInstruction(Opcodes.ASTORE, iteratorSlot);

        code.addLabel(forLoopStart);
        code.addVarInstruction(Opcodes.ALOAD, iteratorSlot);
        code.addMethodInstruction(Opcodes.INVOKEINTERFACE, ByteCodeGenerator.internalNameFor(Iterator.class), "hasNext", "()Z");

        code.addJumpInstruction(Opcodes.IFEQ, forLoopEnd);

        code.addVarInstruction(Opcodes.ALOAD, iteratorSlot);
        code.addMethodInstruction(Opcodes.INVOKEINTERFACE, ByteCodeGenerator.internalNameFor(Iterator.class), "next",
                "()Ljava/lang/Object;");
        String loopVariableName = ByteCodeGenerator.internalNameFor(getComponentType().getJavaType());
        code.addTypeInstruction(Opcodes.CHECKCAST, loopVariableName);
        unbox(code, getComponentType());

        Integer loopVarSlot = code.createLocalStorageSlotFor(variableName);
        if (getComponentType().equals(CatscriptType.INT) || getComponentType().equals(CatscriptType.BOOLEAN)) {
            code.addVarInstruction(Opcodes.ISTORE, loopVarSlot);
        } else {
            code.addVarInstruction(Opcodes.ASTORE, loopVarSlot);
        }
//        System.out.println(body.size());
//        for (Statement stmt : getBody()) {
//            stmt.compile(code);
//        }

        body.forEach(statement -> {
            statement.compile(code);

        });
        code.addJumpInstruction(Opcodes.GOTO, forLoopStart);

        code.addLabel(forLoopEnd);
    }

}
