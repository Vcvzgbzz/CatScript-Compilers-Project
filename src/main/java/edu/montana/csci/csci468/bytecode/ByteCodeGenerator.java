package edu.montana.csci.csci468.bytecode;

import edu.montana.csci.csci468.parser.statements.CatScriptProgram;
import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;


public class ByteCodeGenerator {

    private static final AtomicInteger classInteger = new AtomicInteger();
    private static final DynamicClassLoader CLASS_LOADER = new DynamicClassLoader();

    private ClassWriter classWriter;
    private MethodGenerator currentMethod;
    private Stack<MethodGenerator> methodStack;

    private final CatScriptProgram program;
    private String internalClassName;
    private String dotClassName;

    public ByteCodeGenerator(CatScriptProgram program) {
        this.program = program;
    }

    public CatScriptProgram compileToBytecode() {
        methodStack = new Stack<>();
        classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        internalClassName = "edu/montana/csci/csci466/bytecode/CatScriptProgram" + classInteger.incrementAndGet();
        dotClassName = internalClassName.replace('/', '.');
        makeClass(internalClassName);
        makeConstructor();

        currentMethod = makeMethod(Opcodes.ACC_PUBLIC, "execute", "()V");
        program.compile(this);
        currentMethod.close();

        classWriter.visitEnd();
        byte[] classBytes = classWriter.toByteArray();
        printClassASM(classBytes);
        return loadClass(dotClassName, classBytes);
    }

    private void makeClass(String className) {
        classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC,
                className, null, internalNameFor(CatScriptProgram.class) , null);
    }

    private void makeConstructor() {
        try (MethodGenerator constructor = makeMethod(Opcodes.ACC_PUBLIC, "<init>", "()V")) {
            constructor.addVarInstruction(Opcodes.ALOAD, 0);
            constructor.addMethodInstruction(Opcodes.INVOKESPECIAL, internalNameFor(CatScriptProgram.class), "<init>", "()V");
            constructor.addInstruction(Opcodes.RETURN);
        }
    }

    public static String internalNameFor(Class clazz) {
        final String name = clazz.getName();
        return name.replace(".", "/");
    }

    public void pushMethod(int access, String name, String descriptor) {
        methodStack.push(currentMethod);
        currentMethod = makeMethod(access, name, descriptor);
    }

    public void popMethod() {
        currentMethod.close();
        currentMethod = methodStack.pop();
    }

    private MethodGenerator makeMethod(int access, String name, String descriptor) {
        MethodVisitor method = classWriter.visitMethod(access, name, descriptor, null, null);
        method.visitCode();
        return new MethodGenerator(method);
    }

    private void printClassASM(byte[] classBytes) {
        StringWriter writer = new StringWriter();
        var visitor = new TraceClassVisitor(new PrintWriter(writer));
        CheckClassAdapter checkAdapter = new CheckClassAdapter(visitor);
        ClassReader reader = new ClassReader(classBytes);
        reader.accept(checkAdapter, 0);
        String decompiledTransformedClass = writer.getBuffer().toString();
        System.out.println(" JVM Bytecode ===================================\n");
        System.out.println(decompiledTransformedClass);
        System.out.println("\n ================================================");
    }

    private CatScriptProgram loadClass(String dotClassName, byte[] classBytes) {
        try {
            CLASS_LOADER.defineClass(dotClassName, classBytes);
            Class<?> clazz = CLASS_LOADER.loadClass(dotClassName);
            return (CatScriptProgram) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer nextLocalStorageSlot() {
        return currentMethod.nextLocalStorageSlot();
    }

    public Integer createLocalStorageSlotFor(String name){
        return currentMethod.createLocalStorageSlotFor(name);
    }

    public Integer resolveLocalStorageSlotFor(String name) {
        return currentMethod.resolveLocalStorageSlotFor(name);
    }

    public String getProgramInternalName() {
        return internalClassName;
    }

    static class DynamicClassLoader extends ClassLoader {
        public void defineClass(String name, byte[] bytes) {
            defineClass(name, bytes, 0, bytes.length);
        }
        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name);
        }
    }

    public void addInstruction(int opcode) {
        currentMethod.addInstruction(opcode);
    }

    public void addIntInstruction(int opcode, int operand) {
        currentMethod.addIntInstruction(opcode, operand);
    }

    public void addVarInstruction(int opcode, int var) {
        currentMethod.addVarInstruction(opcode, var);
    }

    public void addTypeInstruction(int opcode, String type) {
        currentMethod.addTypeInstruction(opcode, type);
    }

    public void addFieldInstruction(int opcode, String name, String descriptor, String className) {
        currentMethod.addFieldInstruction(opcode, className, name, descriptor);
    }

    public void addMethodInstruction(int opcode, String owner, String name, String descriptor) {
        currentMethod.addMethodInstruction(opcode, owner, name, descriptor);
    }

    public void addJumpInstruction(int opcode, Label label) {
        currentMethod.addJumpInstruction(opcode, label);
    }

    public void addLabel(Label label) {
        currentMethod.addLabel(label);
    }

    public void pushConstantOntoStack(Object value) {
        currentMethod.pushConstantOntoStack(value);
    }

    public void addField(String name, String descriptor) {
        FieldVisitor fieldVisitor = classWriter.visitField(Opcodes.ACC_PRIVATE, name, descriptor, null, null);
        fieldVisitor.visitEnd();
    }
}
