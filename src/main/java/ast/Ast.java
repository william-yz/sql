package ast;

import java.util.Arrays;
import java.util.List;

public interface Ast {

    AstType getType();

    static Ast createSelect(List<Ast> fields) {
        return new Select(fields);
    }

    static Ast createToken(String token, String subToken, String alias) {
        return new Token(token, subToken, alias);
    }

    static Ast createSubQuery(Ast select, String alias) {
        return new SubQuery(select, alias);
    }

    static Ast createQuery(Ast select, Ast from, Ast where) {
        return new Query(select, from, where);
    }

    static Ast createFrom(List<Ast> froms) {
        return new From(froms);
    }

    static List<Ast> createAstList(Ast... asts) {
        return Arrays.asList(asts);
    }

    static Ast createJoin(Ast join, Ast on) {
        return new Join(join, on);
    }

    static Ast createTable(Ast token, Ast join) {
        return new Table(token, join);
    }

    static <T> Ast createAtom(T value) {
        return new Atom<T>(value);
    }

    static Ast createCondition(Ast left, String operator, Ast right) {
        return new Condition(operator, left, right);
    }

}
