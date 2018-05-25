import enums.Keyword;
import enums.Operator;
import enums.Punctuation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Tokenizer implements Stream<Token> {

    private Input input;
    private List<Token> tokens;
    private int cursor;

    public Tokenizer(Input input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public Token read() {
        readWhile(Utils::isWhitespace);
        if (this.input.eof()) return null;

        String ch = this.input.peek();
        if ("-".equals(ch)) {
            this.maybeComment();
        }
        if ("'".equals(ch) || "\"".equals(ch)) {
            return this.readString(ch);
        }
        if (Utils.isDigit(ch)) {
            return this.readNumber();
        }

        if (Utils.isTokenStart(ch)) {
            return this.readToken();
        }
        if (Punctuation.is(ch)) {
            this.input.next();
            return Token.createPunctuation(ch);
        }
        if (Operator.is(ch)) {
            return Token.createOperator(this.readWhile(Utils::isOperator));
        }
        this.croak(String.format("Can't handle char: %s", ch));
        return null;
    }

    private void maybeComment() {
        if (!this.input.eof(1)) {
            return;
        }
        String next = this.input.peek(1);
        if ("-".equals(next)) {
            this.skipComment();
        }
    }

    private void skipComment() {
        this.readWhile((s) -> Input.EOL.equals(s));
        this.input.next();
    }

    public Token readNumber() {
        final Flag hasDot = new Flag(false);
        String numberStr = this.readWhile((String ch) -> {
            if (".".equals(ch)) {
                if (hasDot.isTure()) this.input.croak("解析数字失败");
                hasDot.toggleTure();
                return true;
            }
            return Utils.isDigit(ch);
        });
        return Token.createNumber(numberStr);
    }

    public Token readToken() {
        String token = this.readWhile(Utils::isTokenChar);
        return Keyword.is(token) ? Token.createKeyword(token) : Token.createToken(token);
    }

    public String readWhile(Function<String, Boolean> predicate) {
        StringBuilder str = new StringBuilder("");
        while (!this.input.eof() && predicate.apply(this.input.peek())) {
            str.append(this.input.next());
        }
        return str.toString();
    }

    public Token readString(String end) {
        StringBuilder str = new StringBuilder("");
        this.input.next();
        while (!this.input.eof()) {
              String ch = this.input.next();
            if (ch.equals(end)) {
                break;
            } else {
                str.append(ch);
            }
        }
        return Token.createString(str.toString());
    }

    @Override
    public Token next() {
        if (this.cursor >= this.tokens.size()) {
            Token next = this.read();
            if (next != null) {
                this.tokens.add(next);
                this.cursor ++;
                return next;
            }
            this.croak("EOF");
         }
        return this.tokens.get(this.cursor ++);
    }

    @Override
    public boolean eof(int n) {
        if (this.cursor + n >= this.tokens.size()) {
            Token next = this.read();
            if (next != null) {
                this.tokens.add(next);
                return this.eof(n);
            }
            return true;

        }
        return false;
    }

    @Override
    public Token peek(int n) {
        if (this.cursor + n >= this.tokens.size()) {
            Token next = this.read();
            if (next != null) {
                this.tokens.add(next);
                if (n == 0) return next;
                return this.peek(n);
            }
            this.croak("EOF");
        }
        return this.tokens.get(this.cursor + n);
    }

    @Override
    public void croak(String msg) {
        this.input.croak(msg);
    }
}
