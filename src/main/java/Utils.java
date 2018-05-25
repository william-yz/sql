import java.util.regex.Pattern;

public class Utils {
    public static boolean isWhitespace(String str) {
        return Pattern.matches("\\s", str);
    }

    public static boolean isDigit(String str) {
        return Pattern.matches("\\d", str);
    }

    public static boolean isTokenStart(String str) {
        return Pattern.matches("[\\w$_`]", str);
    }
    public static boolean isTokenChar(String str) {
        return Pattern.matches("[\\w$_\\d`]", str);
    }

    public static boolean isOperator(String str) {
        return "+-*/<>=.".indexOf(str) != -1;
    }

}
