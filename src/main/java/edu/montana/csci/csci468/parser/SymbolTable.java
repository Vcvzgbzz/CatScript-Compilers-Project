package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.parser.statements.FunctionDefinitionStatement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SymbolTable {

    LinkedList<Map<String, Object>> symbolStack = new LinkedList<>();

    public SymbolTable(){
        HashMap<String, Object> globalScope = new HashMap<>();
        symbolStack.push(globalScope);
    }

    public boolean hasSymbol(String name) {
        return getSymbol(name) != null;
    }

    private Object getSymbol(String name) {
        Iterator<Map<String, Object>> mapIterator = symbolStack.descendingIterator();
        while (mapIterator.hasNext()) {
            Map<String, Object> next =  mapIterator.next();
            Object val = next.get(name);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    public void registerFunction(String name, FunctionDefinitionStatement func) {
        symbolStack.peek().put(name, func);
    }

    public void registerSymbol(String name, CatscriptType type) {
        symbolStack.peek().put(name, type);
    }

    public CatscriptType getSymbolType(String name) {
        Object object = getSymbol(name);
        if (object instanceof CatscriptType) {
            return (CatscriptType) object;
        } else {
            return null;
        }
    }

    public FunctionDefinitionStatement getFunction(String name) {
        Object object = getSymbol(name);
        if (object instanceof FunctionDefinitionStatement) {
            return (FunctionDefinitionStatement) object;
        } else {
            return null;
        }
    }

    public void pushScope() {
        symbolStack.push(new HashMap<>());
    }

    public void popScope() {
        symbolStack.pop();
    }
}
