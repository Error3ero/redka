package expression;

import java.util.List;

public class Negate extends UnaryExpression {
    public Negate(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return '-';
    }

    @Override
    protected int doOperation(int x) {
        return -1 * a.evaluate(x);
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        return -1 * ((TripleExpression) a).evaluate(x, y, z);
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        return -1 * ((ListExpression) a).evaluate(variables);
    }
}
