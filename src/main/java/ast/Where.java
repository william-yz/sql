package ast;

public class Where extends AbstractAst {
    @Override
    public AstType getType() {
        return AstType.WHERE;
    }
}
