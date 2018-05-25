import enums.TokenType;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
       this.type = type;
       this.value = value;
    }

    public static Token createString(String value) {
        return new Token(TokenType.STRING, value);
    }

    public static Token createNumber(String value) {
        return new Token(TokenType.NUMBER, value);
    }

    public static Token createToken(String value) {
        return new Token(TokenType.TOKEN, value);
    }

    public static Token createPunctuation(String value) {
        return new Token(TokenType.PUNCTUATION, value);
    }

    public static Token createKeyword(String value) {
        return new Token(TokenType.KEYWORD, value);
    }
    public static Token createOperator(String value) {
        return new Token(TokenType.OPERATOR, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Token) {
            return ((Token) obj).type == this.type && (
                    (this.value == null && ((Token) obj).value == null) ||
                    this.value.equals(((Token) obj).value));
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Token{type: %s, value: \"%s\"}", this.type, this.value);
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
