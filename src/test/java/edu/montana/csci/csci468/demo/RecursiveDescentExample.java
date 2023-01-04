package edu.montana.csci.csci468.demo;

import edu.montana.csci.csci468.parser.expressions.EqualityExpression;
import edu.montana.csci.csci468.parser.expressions.Expression;
import edu.montana.csci.csci468.tokenizer.Token;

public class RecursiveDescentExample {

    Expression parseEqualityExpression() {
        Expression lhsExpression = parseComparisonExpression();
        if (currentTokenIs("==")) {
            Token t = consumeToken();
            Expression rhsExpression = parseEqualityExpression();
            return new EqualityExpression(t, lhsExpression, rhsExpression);
        } else {
            return lhsExpression;
        }
    }

    private Token consumeToken() {
        return null;
    }

    private boolean currentTokenIs(String s) {
        return false;
    }

    private Expression parseComparisonExpression() {
        return null;
    }
}
