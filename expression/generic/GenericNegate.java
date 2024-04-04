package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericNegate<T> extends GenericUnary<T> {
    public GenericNegate(GenericExpression<T> a, Calculator<T> calculator) {
        super(a, calculator);
    }

    @Override
    protected T doOperation(int x, int y, int z) {
        return calculator.negate(a.evaluate(x, y, z));
    }
}