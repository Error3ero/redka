package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericConst<T> implements GenericExpression<T> {
    protected final int value;
    protected final Calculator<T> calculator;

    public GenericConst(int value, Calculator<T> calculator) {
        this.value = value;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return calculator.getValue(value);
    }
}