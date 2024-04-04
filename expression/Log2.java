package expression;

import java.util.List;

public class Log2 extends UnaryExpression {
    public Log2(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return 'L';
    }

    @Override
    protected int doOperation(int x) {
        int result = 0;
        int val = a.evaluate(x);
        while (val > 0) {
            result++;
            val /= 2;
        }
        return result;
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        int result = 0;
        int val = ((TripleExpression) a).evaluate(x, y, z);
        while (val > 0) {
            result++;
            val /= 2;
        }
        return result;
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        int result = 0;
        int val = ((ListExpression) a).evaluate(variables);
        while (val > 0) {
            result++;
            val /= 2;
        }
        return result;
    }
}
