package expression.exceptions;

import expression.BinaryExpression;
import expression.Expression;
import expression.ListExpression;
import expression.TripleExpression;
import org.w3c.dom.ls.LSInput;

import java.util.List;

public class CheckedDivide extends BinaryExpression {
    public CheckedDivide(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    protected char getOperation() {
        return '/';
    }

    @Override
    protected int doOperation(int x) {
        int ax = a.evaluate(x);
        int bx = b.evaluate(x);
        if (bx == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (ax == Integer.MIN_VALUE && bx == -1) {
            throw new ArithmeticException("overflow");
        }
        return a.evaluate(x) / bx;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int ax = ((TripleExpression) a).evaluate(x, y, z);
        int bx = ((TripleExpression) b).evaluate(x, y, z);
        if (bx == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (ax == Integer.MIN_VALUE && bx == -1) {
            throw new ArithmeticException("overflow");
        }
        return ((TripleExpression) a).evaluate(x, y, z) / bx;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int ax = ((ListExpression) a).evaluate(variables);
        int bx = ((ListExpression) b).evaluate(variables);
        if (bx == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (ax == Integer.MIN_VALUE && bx == -1) {
            throw new ArithmeticException("overflow");
        }
        return ax / bx;
    }
}
