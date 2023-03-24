package edu.montana.csci.csci468.parser;

import edu.montana.csci.csci468.parser.expressions.*;
import edu.montana.csci.csci468.parser.statements.*;
import edu.montana.csci.csci468.tokenizer.CatScriptTokenizer;
import edu.montana.csci.csci468.tokenizer.Token;
import edu.montana.csci.csci468.tokenizer.TokenList;
import edu.montana.csci.csci468.tokenizer.TokenType;

import javax.swing.plaf.nimbus.State;
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
        Statement functionDefStatement = parseFunctionDefintionStatement();
        if(functionDefStatement!=null){
            return functionDefStatement;
        }
        return parseStatement();
    }
    private Statement parseFunctionDefintionStatement(){
        //function x() {}
        if (tokens.match(FUNCTION)) {
            FunctionDefinitionStatement def = new FunctionDefinitionStatement();
            def.setStart((tokens.consumeToken()));
            if(tokens.match(IDENTIFIER)){
                def.setName(tokens.consumeToken().getStringValue());
                require(LEFT_PAREN,def);
                    if (!tokens.match(RIGHT_PAREN)) {
                        do {
                            Token id = require(IDENTIFIER,def);
                                TypeLiteral typeLiteral = null;
                                if (tokens.matchAndConsume(COLON)) {
                                    typeLiteral=parseTypeExpression();
                                }
                                def.addParameter(id.getStringValue(), typeLiteral);



                        } while (tokens.matchAndConsume(COMMA));
                    }

                require(RIGHT_PAREN,def);
                if(tokens.matchAndConsume(COLON)){
                    def.setType(parseTypeExpression());
                }else{
                    TypeLiteral typeLiteral = new TypeLiteral();
                    typeLiteral.setType(CatscriptType.VOID);
                    def.setType(typeLiteral);
                }
            }
           require(LEFT_BRACE,def);
            //if(!tokens.matchAndConsume(RIGHT_BRACE)) {


                List<Statement> statementList = new LinkedList();
                this.currentFunctionDefinition = def;

                while (!tokens.match(RIGHT_BRACE) && tokens.hasMoreTokens()) {
                    if (tokens.match(RETURN)) {

                        statementList.add(parseReturnStatement());
                    } else {
                        statementList.add(parseStatement());
                    }

                }
                def.setBody(statementList);
                this.currentFunctionDefinition = null;
                require(RIGHT_BRACE, def);
            return def;

        }
        return null;
    }


    private TypeLiteral parseTypeExpression(){

        TypeLiteral typeLiteral = new TypeLiteral();
        Token typeToken = tokens.consumeToken();
        if(typeToken.getStringValue().equals("int")){
            typeLiteral.setType(CatscriptType.INT);
        } else if (typeToken.getStringValue().equals("string")) {
            typeLiteral.setType(CatscriptType.STRING);
        } else if (typeToken.getStringValue().equals("bool")) {
            typeLiteral.setType(CatscriptType.BOOLEAN);
        } else if (typeToken.getStringValue().equals("object")) {
            typeLiteral.setType(CatscriptType.OBJECT);
        } else if (typeToken.getStringValue().equals("list")) {

            typeLiteral.setType(CatscriptType.getListType(CatscriptType.OBJECT));
            if(tokens.matchAndConsume(LESS)){
                typeLiteral.setType(CatscriptType.getListType(parseTypeExpression().getType()));
                require(GREATER,typeLiteral);
            }

        }
        else{
            typeLiteral.setType(CatscriptType.getListType(CatscriptType.OBJECT));
        }
        //typeLiteral.setType(CatscriptType.BOOLEAN);
        //recursive call for lists
        //typeLiteral.setType();
        return typeLiteral;
    }
    private Statement parseStatement() {
        Statement printStmt = parsePrintStatement();
        if (printStmt != null) {
            return printStmt;
        }
        Statement forStmt = parseForStatement();
        if (forStmt != null) {
            return forStmt;
        }
        Statement ifStmt = parseIfStatement();
        if(ifStmt!=null){
            return ifStmt;
        }

        Statement returnStmt= parseReturnStatement();{
            if(returnStmt != null){
                return returnStmt;
            }
        }
        Statement assignmentOrFuncCall = parseAssignmentOrFunctionCallStatement();
        if(assignmentOrFuncCall!=null){
            return  assignmentOrFuncCall;
        }
        Statement variableStatement = parseVariableStatement();
        if(variableStatement!=null){
            return variableStatement;
        }
        if(currentFunctionDefinition !=null){
            Statement stmt =parseReturnStatement();
            if(stmt!=null){
                return stmt;
            }
        }
        return new SyntaxErrorStatement(tokens.consumeToken());
    }

    private Statement parseVariableStatement() {
        VariableStatement variableStatement = new VariableStatement();
        if(tokens.match(VAR)){
            variableStatement.setStart(tokens.consumeToken());

            if(tokens.match(IDENTIFIER)){
                variableStatement.setVariableName(tokens.consumeToken().getStringValue());

            if(tokens.matchAndConsume(COLON)){
                TypeLiteral type = parseTypeExpression();


                variableStatement.setExplicitType(type.getType());
            }
            require(EQUAL,variableStatement);
            variableStatement.setExpression(parseExpression());
            return variableStatement; }

        }
        return null;
    }

    private Statement parseIfStatement() {
        IfStatement ifStatement = new IfStatement();
        if(tokens.match(IF)){
            ifStatement.setStart(tokens.consumeToken());
            require(LEFT_PAREN,ifStatement);
            ifStatement.setExpression(parseExpression());
            require(RIGHT_PAREN,ifStatement);
            require(LEFT_BRACE,ifStatement);
            List<Statement> statementList = new LinkedList();
            Statement pieceStmt = null;
            while(tokens.hasMoreTokens()&&!tokens.match(RIGHT_BRACE)) {
                pieceStmt=parseStatement();
                statementList.add(pieceStmt);
            }

            ifStatement.setTrueStatements(statementList);
            require(RIGHT_BRACE,ifStatement);
            if(tokens.matchAndConsume(ELSE)){

                    require(LEFT_BRACE,ifStatement);
                    List<Statement> statementListElse = new LinkedList();
                    Statement pieceStmtElse = null;
                    while(tokens.hasMoreTokens()&&!tokens.match(RIGHT_BRACE)) {
                        pieceStmtElse=parseStatement();
                        statementListElse.add(pieceStmtElse);
                    }

                    ifStatement.setElseStatements(statementListElse);
                    require(RIGHT_BRACE,ifStatement,ErrorType.UNEXPECTED_TOKEN);

            }
        return ifStatement;
        }
        return null;
    }

    private Statement parseAssignmentOrFunctionCallStatement() {
        if(tokens.match(IDENTIFIER)){
            Token id = tokens.consumeToken();
            if(tokens.match(LEFT_PAREN)) {
                return parseFunctionCallStatement(id);
            }else{
                return parseAssignmentStatement(id);
            }
        }
        return null;
    }
    private Statement parseAssignmentStatement(Token id){
        AssignmentStatement assignmentStatement = new AssignmentStatement();
        assignmentStatement.setStart(id);
        require(EQUAL,assignmentStatement);
        assignmentStatement.setVariableName(id.getStringValue());
        assignmentStatement.setExpression(parseExpression());
        return assignmentStatement;


    }
    private Statement parseFunctionCallStatement(Token id){
        if(tokens.matchAndConsume(LEFT_PAREN)){
            List<Expression> expressionList = new LinkedList<>();


            if(tokens.matchAndConsume(RIGHT_PAREN)){
                FunctionCallExpression functionCallExpression = new FunctionCallExpression(id.getStringValue(), expressionList);
                FunctionCallStatement functionCallStatement = new FunctionCallStatement(functionCallExpression);
                functionCallStatement.setStart(id);
                return functionCallStatement;
            }else{
                do{
                    expressionList.add(parseExpression());
                }while(tokens.matchAndConsume(COMMA));
                FunctionCallExpression functionCallExpression = new FunctionCallExpression(id.getStringValue(), expressionList);
                FunctionCallStatement functionCallStatement = new FunctionCallStatement(functionCallExpression);
                functionCallStatement.setStart(id);
                require(RIGHT_PAREN,functionCallStatement);
                return functionCallStatement;
            }


        }


        return null;
    }
    private Statement parseForStatement(){
        ForStatement forStatement = new ForStatement();
        if(tokens.match(FOR)){
            forStatement.setStart(tokens.consumeToken());
            require(LEFT_PAREN,forStatement);
                if(tokens.match(IDENTIFIER)){
                    forStatement.setVariableName(tokens.consumeToken().getStringValue());
                    require(IN,forStatement);
                        forStatement.setExpression(parseExpression());
                        require(RIGHT_PAREN,forStatement);
                            require(LEFT_BRACE,forStatement);
                                List<Statement> statementList = new LinkedList();
                                Statement pieceStmt = null;
                                while(tokens.hasMoreTokens()&&!tokens.match(RIGHT_BRACE)) {
                                    pieceStmt=parseStatement();
                                    statementList.add(pieceStmt);
                                }

                                forStatement.setBody(statementList);
                                require(RIGHT_BRACE,forStatement,ErrorType.UNEXPECTED_TOKEN);
                                return forStatement;


                        }


            }else{
                //no opening left paren{ for ( <--  }
            }

        return null;
    }
    private Statement parseReturnStatement() {
        if(this.currentFunctionDefinition!=null){
            ReturnStatement returnStatement = new ReturnStatement();
            if(tokens.match(RETURN)){
                returnStatement.setStart(tokens.consumeToken());
                returnStatement.setFunctionDefinition(this.currentFunctionDefinition);
                if(tokens.hasMoreTokens()&&!tokens.match(RIGHT_BRACE)){
                    returnStatement.setExpression(parseExpression());


                }
                return returnStatement;
            }
        }
        return null;
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

        } else if (tokens.match(LEFT_PAREN)) {
            Token start = tokens.consumeToken();
            ParenthesizedExpression expression = new ParenthesizedExpression(parseExpression());
            expression.setStart(start);
            require(RIGHT_PAREN,expression);

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
        List<Expression> list = new LinkedList();
        if(tokens.match(LEFT_BRACKET)){
            Token start = tokens.consumeToken();

            if(tokens.matchAndConsume(RIGHT_BRACKET)){
                ListLiteralExpression listLiteralExpression = new ListLiteralExpression(list);
                return listLiteralExpression;
            }
            do{
                list.add(parseExpression());

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
