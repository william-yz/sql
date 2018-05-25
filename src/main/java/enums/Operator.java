package enums;

public enum Operator {
    ADD("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    GREAT_THAN(">"),
    LESS_THAN("<"),
    EQUAL("="),
    DOT("."),
    ;

    private String symbol;
    Operator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public static boolean is(String word) {
        for (Operator operator : Operator.values()) {
            if (operator.toString().equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
