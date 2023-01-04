package edu.montana.csci.csci468.js;

import edu.montana.csci.csci468.parser.statements.CatScriptProgram;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;

public class JSTranspiler {

    private final CatScriptProgram program;
    private final String javascriptSource;

    public JSTranspiler(CatScriptProgram program) {
        this.program = program;
        this.javascriptSource = transpile(program);
    }

    public String evaluate() {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            ScriptContext context = engine.getContext();
            StringWriter writer = new StringWriter();
            context.setWriter(writer);
            engine.eval(javascriptSource);
            return writer.toString();
        } catch (ScriptException e) {
            return e.getMessage();
        }
    }

    private String transpile(CatScriptProgram program) {
        StringBuilder sb = new StringBuilder();
        program.transpile(sb);
        return sb.toString();
    }

    public String getJavascriptSource() {
        return javascriptSource;
    }
}
