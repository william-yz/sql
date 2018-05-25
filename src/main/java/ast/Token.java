package ast;

public class Token extends AbstractAst implements Ast {

    private String token;
    private String subToken;
    private String alias;

    public Token(String token, String subToken, String alias) {
        this.token = token;
        this.subToken = subToken;
        this.alias = alias;
    }

    @Override
    public AstType getType() {
        return AstType.FIELD;
    }

    @Override
    public String toString() {
        return String.format("Token {token: %s, subToken: %s, alias: %s}", this.token, this.subToken, this.alias);
    }
}
