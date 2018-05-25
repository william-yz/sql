import ast.Ast;
import enums.Keyword;
import enums.Operator;
import enums.Punctuation;
import enums.TokenType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Parser {
    private Tokenizer tokenizer;
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Ast parse() {
        return this.parseQuery();
    }



    public Ast parseQuery() {
        Ast select = Ast.createSelect(this.delimited(Keyword.SELECT, Keyword.FROM, Punctuation.COMMA, this::parseToken));
        Ast from = Ast.createFrom(this.delimited(Keyword.FROM, Keyword.WHERE, Punctuation.COMMA, this::parseFrom));
        return Ast.createQuery(select, from, null);
    }

    public Ast parseFrom() {
        Ast token = this.parseToken();
        return Ast.createTable(token, this.maybeJoin());
    }
    public Ast maybeJoin() {
        if (this.isKeyword(Keyword.LEFT)) {
            this.tokenizer.next();
            this.skipKeyword(Keyword.JOIN);
            Ast join = this.parseToken();
            this.skipKeyword(Keyword.ON);
            Ast on = this.parseWhere(this.parseAtom());
            return Ast.createJoin(join, on);
        }
        return null;
    }

    public Ast parseWhere(Ast left) {
        if (this.isOperator()) {
            String operator = this.tokenizer.next().getValue();
            return Ast.createCondition(left, operator, this.parseAtom());
        }
        if (this.isKeyword(Keyword.AND) || this.isKeyword(Keyword.OR)) {
            String connect = this.tokenizer.next().getValue();
            return Ast.createCondition(left, connect, this.parseWhere(this.parseAtom()));
        }
        return null;
    }

    public Ast parseAtom() {
        Token next = this.tokenizer.peek();
        if (next.getType() == TokenType.NUMBER) {
            this.tokenizer.next();
            return Ast.createAtom(Double.valueOf(next.getValue()));
        }
        if (next.getType() == TokenType.STRING) {
            this.tokenizer.next();
            return Ast.createAtom(next.getValue());
        }
        String tokenName = this.skipToken();
        String subToken = this.maybeSubToken();
        String alias = this.maybeAlias();
        return Ast.createToken(tokenName, subToken, alias);
    }
    public Ast parseToken() {
        if (this.isPunctuation(Punctuation.LEFT_BRACKET)) {
            this.skipPunctuation(Punctuation.LEFT_BRACKET);
            Ast subSelect = this.parseQuery();
            if (this.isKeyword(Keyword.FROM)) {
                this.tokenizer.next();
            }
            this.skipPunctuation(Punctuation.RIGHT_BRACKET);
            String alias = this.maybeAlias();
            return Ast.createSubQuery(subSelect, alias);
        } else {
            String tokenName = this.skipToken();
            String subToken = this.maybeSubToken();
            String alias = this.maybeAlias();
            return Ast.createToken(tokenName, subToken, alias);
        }
    }

    public String maybeSubToken() {
        String subToken = null;
        if (isOperator(Operator.DOT)) {
            this.tokenizer.next();
            subToken = this.tokenizer.next().getValue();
        }
        return subToken;
    }

    public String maybeAlias() {
        String alias = null;
        if (isKeyword(Keyword.AS)) {
            this.skipKeyword();
        }
        if (isToken()) {
            alias = this.skipToken();
        }
        return alias;
    }
    public List<Ast> delimited(Keyword start, Keyword stop, Punctuation separator, Supplier<Ast> parser) {
        return delimited(start, stop, separator, parser, this::skipKeyword, this::skipKeyword, this::skipPunctuation, this::isKeyword);
    }

    private <T, S, P> List<Ast> delimited(
            T start,
            S stop,
            P separator,
            Supplier<Ast> parser,
            Consumer<T> startSkipper,
            Consumer<S> stopSkipper,
            Consumer<P> separatorSkipper,
            Function<S, Boolean> stopJudger) {
        List asts = new ArrayList();
        boolean first = true;
        startSkipper.accept(start);
        while (!this.tokenizer.eof()) {
            if (stopJudger.apply(stop)) break;
            if (first) {
                first = false;
            } else {
                if (this.isPunctuation(Punctuation.RIGHT_BRACKET)) {
                    break;
                }
                separatorSkipper.accept(separator);
            }
            if (stopJudger.apply(stop)) break;
            asts.add(parser.get());
        }
//        stopSkipper.accept(stop);
        return asts;
    }

    private boolean isPunctuation(Punctuation punctuation) {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        return next.getType() == TokenType.PUNCTUATION && next.getValue().equals(punctuation.toString());
    }

    private boolean isPunctuation(String str) {
        if (Punctuation.is(str)) {
            return this.isPunctuation(Punctuation.valueOf(str));
        }
        return false;
    }

    private boolean isPunctuation() {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        return next.getType() == TokenType.PUNCTUATION;
    }

    private void skipPunctuation(String str) {
        if (Punctuation.is(str)) {
            this.skipPunctuation(Punctuation.valueOf(str));
            return;
        }
        this.tokenizer.croak(String.format("Expect punctuation: %s", str));
    }

    private void skipPunctuation(Punctuation punctuation) {
        if (isPunctuation(punctuation)) {
            this.tokenizer.next();
            return;
        }
        this.tokenizer.croak(String.format("Expect punctuation: %s", punctuation));
    }

    private boolean isKeyword(Keyword keyword) {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        if (keyword == null) {
            return next.getType() == TokenType.KEYWORD;
        }
        return next.getType() == TokenType.KEYWORD && next.getValue().equalsIgnoreCase(keyword.toString());
    }
    private boolean isKeyword() {
        return this.isKeyword(null);
    }

    private void skipKeyword(Keyword keyword) {
        if (this.isKeyword(keyword)) {
            this.tokenizer.next();
            return;
        }
        this.tokenizer.croak(String.format("Expect keyword: %s", keyword));
    }

    private void skipKeyword() {
        this.skipKeyword(null);
    }

    private boolean isToken(String str) {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        if (str == null) {
            return next.getType() == TokenType.TOKEN;
        }
        return next.getType() == TokenType.KEYWORD && next.getValue().equals(str);
    }

    private boolean isToken() {
        return this.isToken(null);
    }

    private String skipToken() {
        if (this.isToken()) {
            return this.tokenizer.next().getValue();
        }
        this.tokenizer.croak(String.format("Expect token."));
        return null;
    }

    private boolean isOperator(Operator operator) {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        return next.getType() == TokenType.OPERATOR && next.getValue().equals(operator.toString());
    }

    private boolean isOperator() {
        if (this.tokenizer.eof()) return false;
        Token next = this.tokenizer.peek();
        return next.getType() == TokenType.OPERATOR;
    }
}
