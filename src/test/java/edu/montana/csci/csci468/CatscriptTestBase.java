package edu.montana.csci.csci468;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.js.JSTranspiler;
import edu.montana.csci.csci468.parser.CatScriptParser;
import edu.montana.csci.csci468.parser.ErrorType;
import edu.montana.csci.csci468.parser.ParseError;
import edu.montana.csci.csci468.parser.ParseErrorException;
import edu.montana.csci.csci468.parser.expressions.Expression;
import edu.montana.csci.csci468.parser.statements.CatScriptProgram;
import edu.montana.csci.csci468.parser.statements.Statement;
import edu.montana.csci.csci468.tokenizer.CatScriptTokenizer;
import edu.montana.csci.csci468.tokenizer.Token;
import edu.montana.csci.csci468.tokenizer.TokenList;
import edu.montana.csci.csci468.tokenizer.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatscriptTestBase {

    protected <T extends Expression> T parseExpression(String source) {
        return parseExpression(source, true);
    }

    protected  <T extends Expression> T parseExpression(String source, boolean verify) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parseAsExpression(source);
        if(verify) {
            program.verify();
        }
        return (T) program.getExpression();
    }

    protected <T extends Statement> T parseStatement(String source) {
        return parseStatement(source, true);
    }

    protected <T extends Statement> T parseStatement(String source, boolean verify) {
        return parseStatement(source, verify, 0);
    }

    protected <T extends Statement> T parseStatement(String source, int i) {
        return parseStatement(source, true, i);
    }

    protected <T extends Statement> T parseStatement(String source, boolean verify, int i) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(source);
        if(verify) {
            program.verify();
        }
        return (T) program.getStatements().get(i);
    }

    protected void assertTokensAre(String src, TokenType... expected) {
        TokenList tokens = getTokenList(src);
        assertEquals(Arrays.asList(expected), tokens.stream().map(Token::getType).collect(Collectors.toList()));
    }

    protected TokenList getTokenList(String src) {
        CatScriptTokenizer tokenizer = new CatScriptTokenizer(src);
        return tokenizer.getTokens();
    }

    protected List<Token> getTokensAsList(String src) {
        CatScriptTokenizer tokenizer = new CatScriptTokenizer(src);
        return tokenizer.getTokens().stream().collect(Collectors.toList());
    }

    protected void assertTokensAre(String src, String... expected) {
        TokenList tokens = getTokenList(src);
        assertEquals(Arrays.asList(expected), tokens.stream().map(Token::getStringValue).collect(Collectors.toList()));
    }

    protected ErrorType getParseError(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        try {
            program.verify();
            throw new IllegalStateException("The code " + src + " did not throw an error!");
        } catch (ParseErrorException parseErrorException) {
            return parseErrorException.getErrors().get(0).getErrorType();
        }
    }

    protected Object evaluateExpression(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        program.verify();
        return program.getExpression().evaluate(new CatscriptRuntime());
    }

    protected Object executeProgram(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        program.verify();
        program.execute();
        return program.getOutput();
    }

    protected String transpile(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        program.verify();
        JSTranspiler jsTranspiler = new JSTranspiler(program);
        System.out.println(jsTranspiler.getJavascriptSource());
        return jsTranspiler.evaluate();
    }

    protected String compile(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        program.verify();
        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(program);
        CatScriptProgram catScriptProgram = byteCodeGenerator.compileToBytecode();
        catScriptProgram.execute();
        return catScriptProgram.getOutput();
    }

    protected List<ParseError> getErrors(String src) {
        final CatScriptParser parser = new CatScriptParser();
        final CatScriptProgram program = parser.parse(src);
        try {
            program.verify();
            return Collections.emptyList();
        } catch (ParseErrorException parseErrorException) {
            return parseErrorException.getErrors();
        }
    }

}
