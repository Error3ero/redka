package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericAdd<T> extends GenericBinary<T> {
    protected GenericAdd(GenericExpression<T> a, GenericExpression<T> b, Calculator<T> calculator) {
        super(a, b, calculator);
    }
    @Override
    protected T doOperation(int x, int y, int z) {
        return calculator.add(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }
}