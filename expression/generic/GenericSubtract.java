package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericSubtract<T> extends GenericBinary<T> {
    public GenericSubtract(GenericExpression<T> a, GenericExpression<T> b, Calculator<T> calculator) {
        super(a, b, calculator);
    }

    @Override
    protected T doOperation(int x, int y, int z) {
        return calculator.subtract(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }
}

