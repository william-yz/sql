package ast;

public class Table extends AbstractAst {

    private Ast token;
    private Ast join;

    public Table(Ast token, Ast join) {
        this.token = token;
        this.join = join;
    }

    @Override
    public AstType getType() {
        return AstType.TABLE;
    }

    @Override
    public String toString() {
        return String.format("Table {token: %s, join: %s}", this.token, this.join);
    }
}
