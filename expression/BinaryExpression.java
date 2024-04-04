package expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BinaryExpression implements Expression, TripleExpression, ToMiniString, ListExpression {
    protected final Expression a, b;

    protected BinaryExpression(Expression a, Expression b) {
        this.a = a;
        this.b = b;
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
        return "(" + a.toString() + " " + getOperation() + " " + b.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b)
                && getOperation() == that.getOperation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, getOperation());
    }
}
