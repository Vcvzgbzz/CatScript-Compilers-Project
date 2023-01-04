package edu.montana.csci.csci468.tokenizer;

public class Token {

    int start;
    int end;
    int line;
    int lineOffset;
    String stringValue;
    TokenType type;
    private final CatScriptTokenizer tokenizer;

    public Token(int start, int end, int line, int lineOffset, String stringValue, TokenType type, CatScriptTokenizer tokenizer) {
        this.start = start;
        this.end = end;
        this.line = line;
        this.lineOffset = lineOffset;
        this.stringValue = stringValue;
        this.type = type;
        this.tokenizer = tokenizer;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLine() {
        return line;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public String getStringValue() {
        return stringValue;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Token(\"" + stringValue + "\"){" +
                "type=" + type +
                ", start=" + start +
                ", end=" + end +
                ", line=" + line +
                ", offset=" + lineOffset +
                '}';
    }

    public String getLineContent() {
        String[] lines = tokenizer.src.split("\n");
        return lines[line - 1];
    }
}
