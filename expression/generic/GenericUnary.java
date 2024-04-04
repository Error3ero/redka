package expression.generic;

import expression.generic.calculator.Calculator;

public abstract class GenericUnary<T> implements GenericExpression<T> {
    protected final GenericExpression<T> a;
    protected final Calculator<T> calculator;

    protected GenericUnary(GenericExpression<T> a, Calculator<T> calculator) {
        this.a = a;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return doOperation(x, y, z);
    }

    protected abstract T doOperation(int x, int y, int z);
}