package ast;

public class Condition extends AbstractAst {
    private String operator;
    private Ast left;
    private Ast right;

    public Condition(String operator, Ast left, Ast right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public AstType getType() {
        return AstType.CONDITION;
    }

    @Override
    public String toString() {
        return String.format("Condition {operator: %s, left: %s, right: %s}", operator, left, right);
    }
}
