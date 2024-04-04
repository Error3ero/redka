package expression.generic;

import expression.generic.calculator.Calculator;

public abstract class GenericBinary<T> implements GenericExpression<T> {
    protected final GenericExpression<T> a, b;
    protected final Calculator<T> calculator;
    protected GenericBinary(GenericExpression<T> a, GenericExpression<T> b, Calculator<T> calculator) {
        this.a = a;
        this.b = b;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return doOperation(x, y, z);
    }

    protected abstract T doOperation(int x, int y, int z);
}
