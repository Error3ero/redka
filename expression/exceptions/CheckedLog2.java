package expression.exceptions;

import expression.Expression;
import expression.ListExpression;
import expression.TripleExpression;
import expression.UnaryExpression;

import java.util.List;

public class CheckedLog2 extends UnaryExpression {
    public CheckedLog2(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return 'l';
    }

    @Override
    protected int doOperation(int x) {
        int ax = a.evaluate(x);
        if (ax <= 0) {
            throw new ArithmeticException("illegal logarithm argument");
        }
        int result = 0;
        while (ax > 0) {
            result++;
            ax /= 2;
        }
        return result;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int ax = ((TripleExpression)a).evaluate(x, y, z);
        if (ax <= 0) {
            throw new ArithmeticException("illegal logarithm argument");
        }
        int result = 0;
        while (ax > 0) {
            result++;
            ax /= 2;
        }
        return result - 1;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int ax = ((ListExpression)a).evaluate(variables);
        if (ax <= 0) {
            throw new ArithmeticException("illegal logarithm argument");
        }
        int result = 0;
        while (ax > 0) {
            result++;
            ax /= 2;
        }
        return result - 1;
    }

    @Override
    public String toString() {
        return "log2(" + a + ")";
    }
}
