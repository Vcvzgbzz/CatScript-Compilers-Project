package edu.montana.csci.csci468.bytecode;

import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MethodGenerator implements AutoCloseable {

    private AtomicInteger localStorageSlot = new AtomicInteger();
    Map<String, Integer> localStorageMap = new HashMap<>();
    private final MethodVisitor delegate;

    public Integer nextLocalStorageSlot() {
        return localStorageSlot.incrementAndGet();
    }

    public Integer createLocalStorageSlotFor(String name){
        int i = nextLocalStorageSlot();
        localStorageMap.put(name, i);
        return i;
    }

    public Integer resolveLocalStorageSlotFor(String name) {
        return localStorageMap.get(name);
    }

    public MethodGenerator(MethodVisitor delgate) {
        this.delegate = delgate;
    }

    @Override
    public void close()  {
        delegate.visitMaxs(0, 0);
        delegate.visitEnd();
    }

    // delegation code
    public void addInstruction(int opcode) {
        delegate.visitInsn(opcode);
    }

    public void addIntInstruction(int opcode, int operand) {
        delegate.visitIntInsn(opcode, operand);
    }

    public void addVarInstruction(int opcode, int var) {
        delegate.visitVarInsn(opcode, var);
    }

    public void addTypeInstruction(int opcode, String type) {
        delegate.visitTypeInsn(opcode, type);
    }

    public void addFieldInstruction(int opcode, String owner, String name, String descriptor) {
        delegate.visitFieldInsn(opcode, owner, name, descriptor);
    }

    public void addMethodInstruction(int opcode, String owner, String name, String descriptor) {
        delegate.visitMethodInsn(opcode, owner, name, descriptor);
    }

    public void addJumpInstruction(int opcode, Label label) {
        delegate.visitJumpInsn(opcode, label);
    }

    public void addLabel(Label label) {
        delegate.visitLabel(label);
    }

    public void pushConstantOntoStack(Object value) {
        if (value == null) {
            addInstruction(Opcodes.ACONST_NULL);
        } else {
            delegate.visitLdcInsn(value);
        }
    }
}
