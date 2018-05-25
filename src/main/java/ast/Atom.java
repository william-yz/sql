package ast;


public class Atom<T> extends AbstractAst{

    private T value;

    public Atom(T value) {
        this.value = value;
    }

    @Override
    public AstType getType() {
        return AstType.ATOM;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
