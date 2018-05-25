package enums;

public enum Punctuation {
    COMMA(","),
    SEMICOLON(";"),
    LEFT_BRACKET("("),
    RIGHT_BRACKET(")"),
    ;

    private String symbol;
    Punctuation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public static boolean is(String word) {
        for (Punctuation punctuation : Punctuation.values()) {
            if (punctuation.toString().equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
