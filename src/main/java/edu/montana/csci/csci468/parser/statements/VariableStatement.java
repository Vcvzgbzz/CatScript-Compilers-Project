package edu.montana.csci.csci468.parser.statements;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.ParseError;
import edu.montana.csci.csci468.parser.SymbolTable;
import edu.montana.csci.csci468.parser.expressions.Expression;
import org.eclipse.jetty.websocket.common.OpCode;
import org.objectweb.asm.Opcodes;

public class VariableStatement extends Statement {
    private Expression expression;
    private String variableName;
    private CatscriptType explicitType;
    private CatscriptType type;

    public Expression getExpression() {
        return expression;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setExpression(Expression parseExpression) {
        this.expression = addChild(parseExpression);
    }

    public void setExplicitType(CatscriptType type) {
        this.explicitType = type;
    }

    public CatscriptType getExplicitType() {
        return explicitType;
    }

    public boolean isGlobal() {
        return getParent() instanceof CatScriptProgram;
    }

    @Override
    public void validate(SymbolTable symbolTable) {
        expression.validate(symbolTable);
        if (symbolTable.hasSymbol(variableName)) {
            addError(ErrorType.DUPLICATE_NAME);
        } else {
            type = null;
            if(explicitType!=null){
                type=explicitType;
                if(!explicitType.isAssignableFrom(expression.getType())){
                    addError(ErrorType.INCOMPATIBLE_TYPES);
                }
            }else{
                type=expression.getType();
            }
            symbolTable.registerSymbol(variableName, type);
        }
    }

    public CatscriptType getType() {
        return type;
    }

    //==============================================================
    // Implementation
    //==============================================================
    @Override
    public void execute(CatscriptRuntime runtime) {
        Object e = expression.evaluate(runtime);
        runtime.setValue(variableName,e);
    }

    @Override
    public void transpile(StringBuilder javascript) {
        super.transpile(javascript);
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        String descriptor = "L"+ByteCodeGenerator.internalNameFor(getType().getJavaType())+";";
        if(isGlobal()){

            if(getType().equals(CatscriptType.INT)||getType().equals(CatscriptType.BOOLEAN)){
                code.addVarInstruction(Opcodes.ALOAD,0);
                getExpression().compile(code);

                code.addField(variableName,"I");
                code.addFieldInstruction(Opcodes.PUTFIELD,getVariableName(),"I",code.getProgramInternalName());
            }else{
                code.addVarInstruction(Opcodes.ALOAD,0);
                getExpression().compile(code);
                code.addField(variableName, descriptor);
                code.addFieldInstruction(Opcodes.PUTFIELD,getVariableName(),descriptor,code.getProgramInternalName());
            }

        }else{

                Integer slotNum = code.createLocalStorageSlotFor(getVariableName());
                getExpression().compile(code);

            if(getType().equals(CatscriptType.INT)||getType().equals(CatscriptType.BOOLEAN)) {
                code.addField(variableName, "I");
                code.addVarInstruction(Opcodes.ISTORE,slotNum);
//                code.addVarInstruction(Opcodes.ILOAD,slotNum);
            }else{
                code.addField(variableName, descriptor);
                code.addVarInstruction(Opcodes.ASTORE,slotNum);

//                code.addVarInstruction(Opcodes.ALOAD,slotNum);
            }
        }
    }
}
