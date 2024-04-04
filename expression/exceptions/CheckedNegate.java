package expression.exceptions;

import expression.Expression;
import expression.ListExpression;
import expression.TripleExpression;
import expression.UnaryExpression;

import java.util.List;

public class CheckedNegate extends UnaryExpression {
    public CheckedNegate(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return '-';
    }

    @Override
    protected int doOperation(int x) {
        int ax = a.evaluate(x);
        if (ax == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -ax;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int ax = ((TripleExpression) a).evaluate(x, y, z);
        if (ax == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -ax;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int ax = ((ListExpression) a).evaluate(variables);
        if (ax == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -ax;
    }
}
