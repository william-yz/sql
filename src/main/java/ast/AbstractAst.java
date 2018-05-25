package ast;

import java.util.List;

public abstract class AbstractAst implements Ast {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Ast) {
            if (((Ast) obj).getType() == this.getType() && this.toString().equals(((Ast) obj).toString())) {
                return true;
            }
        }
        return false;
    }

    protected static String astListToString(List<Ast> astList) {
        return astList.stream()
                .map((ast) -> ast.toString())
                .reduce("", (a, b) -> String.format("%s|%s", a, b));
    }
}
