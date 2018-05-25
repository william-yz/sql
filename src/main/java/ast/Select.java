package ast;

import java.util.List;

public class Select extends AbstractAst implements Ast {

    protected List<Ast> tokens;

    public Select(List<Ast> tokens) {
        this.tokens = tokens;
    }

    @Override
    public AstType getType() {
        return AstType.SELECT;
    }

    @Override
    public String toString() {
        return String.format("Select {tokens: [%s]}", AbstractAst.astListToString(this.tokens));
    }
}
