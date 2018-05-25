package ast;

import enums.Keyword;

public class Join extends AbstractAst {

    private Keyword type;
    private Ast join;
    private Ast on;

    public Join(Ast join, Ast on) {
        this.type = Keyword.LEFT;
        this.join = join;
        this.on = on;
    }

    @Override
    public AstType getType() {
        return AstType.JOIN;
    }


    @Override
    public String toString() {
        return String.format("Join {join: %s, on: %s}", this.join, this.on);
    }
}
