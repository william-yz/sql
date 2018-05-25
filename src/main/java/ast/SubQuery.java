package ast;

import java.util.List;

public class SubQuery extends AbstractAst {

    private Ast select;
    private String alias;

    public SubQuery(Ast select, String alias) {
        this.select = select;
        this.alias = alias;
    }

    @Override
    public String toString() {
        return String.format("SubQuery {select: %s,alias: %s}", select.toString(), this.alias);
    }

    @Override
    public AstType getType() {
        return AstType.SUB_SELECT;
    }
}
