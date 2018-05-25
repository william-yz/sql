package ast;

public class Query extends AbstractAst {

    private Ast select;
    private Ast from;
    private Ast where;

    public Query(Ast select, Ast from, Ast where) {
        this.select = select;
        this.from = from;
        this.where = where;
    }

    @Override
    public AstType getType() {
        return AstType.QUERY;
    }

    @Override
    public String toString() {
        return String.format("Query {select: %s, from: %s, where: %s}", this.select, this.from, this.where);
    }
}
