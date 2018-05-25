package ast;

import java.util.List;

public class From extends AbstractAst {

    private List<Ast> froms;

    public From(List<Ast> froms) {
        this.froms = froms;
    }

    @Override
    public AstType getType() {
        return AstType.FROM;
    }

    @Override
    public String toString() {
        return String.format("From {froms: %s}", AbstractAst.astListToString(this.froms));
    }
}
