package expression;

import java.util.List;
import java.util.Objects;

public abstract class UnaryExpression implements Expression, TripleExpression, ToMiniString, ListExpression {
    protected final Expression a;

    protected UnaryExpression(Expression a) {
        this.a = a;
    }

    protected abstract char getOperation();

    protected abstract int doOperation(int x);

    protected abstract int doOperation(int x, int y, int z);

    protected abstract int doOperation(List<Integer> variables);

    @Override
    public int evaluate(int x) {
            return doOperation(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
            return doOperation(x, y, z);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return doOperation(variables);
    }

    @Override
    public String toString() {
        return this.getOperation() + "(" + a.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryExpression that = (UnaryExpression) o;
        return Objects.equals(a, that.a) && getOperation() == that.getOperation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, getOperation());
    }
}
