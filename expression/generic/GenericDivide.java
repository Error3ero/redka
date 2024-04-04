package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericDivide<T> extends GenericBinary<T> {
    protected GenericDivide(GenericExpression<T> a, GenericExpression<T> b, Calculator<T> calculator) {
        super(a, b, calculator);
    }

    @Override
    protected T doOperation(int x, int y, int z) {
        return calculator.divide(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }
}

