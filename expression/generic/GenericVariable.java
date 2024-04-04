package expression.generic;

import expression.generic.calculator.Calculator;

public class GenericVariable<T> implements GenericExpression<T> {
    private final String name;
    private final Calculator<T> calculator;

    public GenericVariable(String name, Calculator<T> calculator) {
        this.name = name;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        int val = switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException("Unexpected variable name: " + name);
        };
        return getValue(val);
    }

    protected T getValue(int v) {
        return calculator.getValue(v);
    }
}