package expression;

import java.util.List;

public class And extends BinaryExpression {
    public And(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    protected char getOperation() {
        return '&';
    }

    @Override
    protected int doOperation(int x) {
        return a.evaluate(x) & b.evaluate(x);
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        return ((TripleExpression) a).evaluate(x, y, z) & ((TripleExpression) b).evaluate(x, y, z);
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        return ((ListExpression) a).evaluate(variables) & ((ListExpression) b).evaluate(variables);
    }
}
