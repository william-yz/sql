package enums;

public enum Keyword {
    SELECT,
    FROM,
    WHERE,
    AS,
    AND,
    OR,
    LEFT,
    RIGHT,
    JOIN,
    ON,
    IS,
    NOT,
    NULL,
    ;

    public static boolean is(String word) {
        for (Keyword keyword : Keyword.values()) {
            if (keyword.toString().equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }


}
