package expression.exceptions;

import expression.Expression;
import expression.ListExpression;
import expression.TripleExpression;
import expression.UnaryExpression;

import java.util.List;

public class CheckedPow2 extends UnaryExpression {
    public CheckedPow2(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return 'p';
    }

    @Override
    protected int doOperation(int x) {
        int ax = a.evaluate(x);
        if (ax >= 32) {
            throw new ArithmeticException("overflow");
        }
        return 1 << ax;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int ax = ((TripleExpression) a).evaluate(x, y, z);
        if (ax >= 31) {
            throw new ArithmeticException("overflow");
        } else if (ax < 0) {
            throw new ArithmeticException("negative power");
        }
        return 1 << ax;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int ax = ((ListExpression) a).evaluate(variables);
        if (ax >= 31) {
            throw new ArithmeticException("overflow");
        } else if (ax < 0) {
            throw new ArithmeticException("negative power");
        }
        return 1 << ax;
    }

    @Override
    public String toString() {
        return "pow2(" + a + ")";
    }
}
