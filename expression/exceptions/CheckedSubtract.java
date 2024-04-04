package expression.exceptions;

import expression.BinaryExpression;
import expression.Expression;
import expression.ListExpression;
import expression.TripleExpression;

import java.util.List;

public class CheckedSubtract extends BinaryExpression {
    public CheckedSubtract(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    protected char getOperation() {
        return '-';
    }

    @Override
    protected int doOperation(int x) {
        int ax = a.evaluate(x);
        int bx = b.evaluate(x);
        int r = ax - bx;
        if (((ax ^ r) & (bx ^ r)) < 0) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int ax = ((TripleExpression) a).evaluate(x, y, z);
        int bx = ((TripleExpression) b).evaluate(x, y, z);
        int r = ax - bx;
        if (bx < 0 && r < ax || bx > 0 && r > ax) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int ax = ((ListExpression) a).evaluate(variables);
        int bx = ((ListExpression) b).evaluate(variables);
        int r = ax - bx;
        if (bx < 0 && r < ax || bx > 0 && r > ax) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }
}
