package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.parser.expressions.*;
import edu.montana.csci.csci468.parser.statements.*;
import edu.montana.csci.csci468.tokenizer.CatScriptTokenizer;
import edu.montana.csci.csci468.tokenizer.Token;
import edu.montana.csci.csci468.tokenizer.TokenList;
import edu.montana.csci.csci468.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.montana.csci.csci468.tokenizer.TokenType.*;

public class CatScriptParser {

    private TokenList tokens;
    private FunctionDefinitionStatement currentFunctionDefinition;

    public CatScriptProgram parse(String source) {
        tokens = new CatScriptTokenizer(source).getTokens();

        // first parse an expression
        CatScriptProgram program = new CatScriptProgram();
        program.setStart(tokens.getCurrentToken());
        Expression expression = null;
        try {
            expression = parseExpression();
        } catch(RuntimeException re) {
            // ignore :)
        }
        if (expression == null || tokens.hasMoreTokens()) {
            tokens.reset();
            while (tokens.hasMoreTokens()) {
                program.addStatement(parseProgramStatement());
            }
        } else {
            program.setExpression(expression);
        }

        program.setEnd(tokens.getCurrentToken());
        return program;
    }

    public CatScriptProgram parseAsExpression(String source) {
        tokens = new CatScriptTokenizer(source).getTokens();
        CatScriptProgram program = new CatScriptProgram();
        program.setStart(tokens.getCurrentToken());
        Expression expression = parseExpression();
        program.setExpression(expression);
        program.setEnd(tokens.getCurrentToken());
        return program;
    }

    //============================================================
    //  Statements
    //============================================================

    private Statement parseProgramStatement() {
        Statement printStmt = parsePrintStatement();
        if (printStmt != null) {
            return printStmt;
        }
        return new SyntaxErrorStatement(tokens.consumeToken());
    }

    private Statement parsePrintStatement() {
        if (tokens.match(PRINT)) {

            PrintStatement printStatement = new PrintStatement();
            printStatement.setStart(tokens.consumeToken());

            require(LEFT_PAREN, printStatement);
            printStatement.setExpression(parseExpression());
            printStatement.setEnd(require(RIGHT_PAREN, printStatement));

            return printStatement;
        } else {
            return null;
        }
    }

    //============================================================
    //  Expressions
    //============================================================

    private Expression parseExpression() {
        return parseEqualityExpression();

    }

    private Expression parseAdditiveExpression() {
        Expression expression = parseFactorExpression();
        while (tokens.match(PLUS, MINUS)) {
            Token operator = tokens.consumeToken();
            final Expression rightHandSide = parseFactorExpression();
            AdditiveExpression additiveExpression = new AdditiveExpression(operator, expression, rightHandSide);
            additiveExpression.setStart(expression.getStart());
            additiveExpression.setEnd(rightHandSide.getEnd());
            expression = additiveExpression;
        }
        return expression;
    }

    private Expression parseFactorExpression(){
        Expression expression = parseUnaryExpression();
        while (tokens.match(SLASH, STAR)) {
            Token operator = tokens.consumeToken();
            final Expression rightHandSide = parseUnaryExpression();
            FactorExpression factorExpression = new FactorExpression(operator, expression, rightHandSide);
            factorExpression.setStart(expression.getStart());
            factorExpression.setEnd(rightHandSide.getEnd());
            expression = factorExpression;
        }
        return expression;
    }
    private Expression parseComparisonExpression(){
        Expression expression = parseAdditiveExpression();
        while (tokens.match(GREATER, GREATER_EQUAL,LESS,LESS_EQUAL)) {
            Token operator = tokens.consumeToken();
            final Expression rightHandSide = parseAdditiveExpression();
            ComparisonExpression comparisonExpression = new ComparisonExpression(operator, expression, rightHandSide);
            comparisonExpression.setStart(expression.getStart());
            comparisonExpression.setEnd(rightHandSide.getEnd());
            expression = comparisonExpression;
        }
        return expression;
    }
    private Expression parseEqualityExpression(){
        Expression expression=parseComparisonExpression();
        while (tokens.match(EQUAL_EQUAL,BANG_EQUAL)) {
            Token operator = tokens.consumeToken();
            final Expression rightHandSide = parseComparisonExpression();
            EqualityExpression equalityExpression = new EqualityExpression(operator, expression, rightHandSide);
            equalityExpression.setStart(expression.getStart());
            equalityExpression.setEnd(rightHandSide.getEnd());
            expression = equalityExpression;
        }
        return expression;
    }
    private Expression parseUnaryExpression() {
        if (tokens.match(MINUS, NOT)) {
            Token token = tokens.consumeToken();
            Expression rhs = parseUnaryExpression();
            UnaryExpression unaryExpression = new UnaryExpression(token, rhs);
            unaryExpression.setStart(token);
            unaryExpression.setEnd(rhs.getEnd());
            return unaryExpression;
        } else {
            return parsePrimaryExpression();
        }
    }

    private Expression parsePrimaryExpression() {
        if (tokens.match(INTEGER)) {
            Token integerToken = tokens.consumeToken();
            IntegerLiteralExpression integerExpression = new IntegerLiteralExpression(integerToken.getStringValue());
            integerExpression.setToken(integerToken);
            return integerExpression;
        } else if(tokens.match(NULL)){
            Token nullToken = tokens.consumeToken();
            NullLiteralExpression nullLiteralExpression = new NullLiteralExpression();
            nullLiteralExpression.setToken(nullToken);
            return nullLiteralExpression;

        } else if (tokens.match(FALSE,TRUE)) {
            Token boolToken=tokens.consumeToken();
            BooleanLiteralExpression boolLiteralExpression = new BooleanLiteralExpression(boolToken.getType().equals(FALSE)?false:true);
            boolLiteralExpression.setToken(boolToken);
            return boolLiteralExpression;
        } else if (tokens.match(IDENTIFIER)) {
            Token identifierToken = tokens.consumeToken();
            IdentifierExpression identifierExpression = new IdentifierExpression(identifierToken.getStringValue());
            identifierExpression.setToken(identifierToken);
            if(tokens.match(LEFT_PAREN)){
                return parseFunctionCall(identifierToken);

            }
            return identifierExpression;

        } else if (tokens.match(STRING)) {
            Token stringToken = tokens.consumeToken();
            StringLiteralExpression stringLiteralExpression = new StringLiteralExpression(stringToken.getStringValue());
            stringLiteralExpression.setToken(stringToken);
            return stringLiteralExpression;

        } else if (tokens.match(STRING,LEFT_PAREN)) {
            Token insideExpression = tokens.consumeToken();
            ParenthesizedExpression expression = new ParenthesizedExpression(parseExpression());

            System.out.println(expression);
            return expression;
        } else if (tokens.match(LEFT_BRACKET) ){
            return parseListLiteral();
    }
        {
            SyntaxErrorExpression syntaxErrorExpression = new SyntaxErrorExpression(tokens.consumeToken());
            return syntaxErrorExpression;
        }
    }
    private Expression parseFunctionCall(Token identifier) {
        List<Expression> argumentList = new LinkedList();
        //FunctionCallExpression functionCallExpression = new FunctionCallExpression();
        if(tokens.match(LEFT_PAREN)){
            Token start = tokens.consumeToken();

            if(tokens.matchAndConsume(RIGHT_PAREN)){
                //handle empty case foo()
                FunctionCallExpression functionCallExpression = new FunctionCallExpression(identifier.getStringValue(),argumentList);
                return functionCallExpression;
            }
            do{
                argumentList.add(parseExpression());
                //System.out.println(list.size());

            }while(tokens.matchAndConsume(COMMA));

            if(tokens.match(RIGHT_PAREN)){
                Token end = tokens.consumeToken();
                //list.add(end);
                FunctionCallExpression functionCallExpression = new FunctionCallExpression(identifier.getStringValue(),argumentList);
                return functionCallExpression;
            }else{


                FunctionCallExpression functionCallExpression = new FunctionCallExpression(identifier.getStringValue(),argumentList);
                functionCallExpression.addError(ErrorType.UNTERMINATED_ARG_LIST,tokens.getCurrentToken());
                return functionCallExpression;
            }
        }else{
            return null;
        }

    }
    private Expression parseListLiteral(){
        System.out.println("list literal");
        List<Expression> list = new LinkedList();
        //ArrayList list = new ArrayList<Expression>();
        if(tokens.match(LEFT_BRACKET)){
            Token start = tokens.consumeToken();

            if(tokens.matchAndConsume(RIGHT_BRACKET)){
                ListLiteralExpression listLiteralExpression = new ListLiteralExpression(list);
                return listLiteralExpression;
            }
            do{
                list.add(parseExpression());
                System.out.println(list.size());

            }while(tokens.matchAndConsume(COMMA));

            if(tokens.match(RIGHT_BRACKET)){
                Token end = tokens.consumeToken();
                //list.add(end);
                ListLiteralExpression listLiteralExpression = new ListLiteralExpression(list);
                return listLiteralExpression;
            }else{
                ListLiteralExpression listLiteralExpression = new ListLiteralExpression(list);
                listLiteralExpression.addError(ErrorType.UNTERMINATED_LIST,tokens.getCurrentToken());
                return listLiteralExpression;
            }
        }else{
            return null;
        }
    }

    //============================================================
    //  Parse Helpers
    //============================================================
    private Token require(TokenType type, ParseElement elt) {
        return require(type, elt, ErrorType.UNEXPECTED_TOKEN);
    }

    private Token require(TokenType type, ParseElement elt, ErrorType msg) {
        if(tokens.match(type)){
            return tokens.consumeToken();
        } else {
            elt.addError(msg, tokens.getCurrentToken());
            return tokens.getCurrentToken();
        }
    }

}
