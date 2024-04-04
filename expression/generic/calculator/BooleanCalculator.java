package expression.generic.calculator;

public class BooleanCalculator implements Calculator<Boolean> {
    @Override
    public Boolean add(Boolean a, Boolean b) {
        return a | b;
    }

    @Override
    public Boolean subtract(Boolean a, Boolean b) {
        return a ^ b;
    }

    @Override
    public Boolean divide(Boolean a, Boolean b) {
        if (!b) {
            throw new ArithmeticException("division by zero(false)");
        }
        return a;
    }

    @Override
    public Boolean multiply(Boolean a, Boolean b) {
        return a & b;
    }

    @Override
    public Boolean negate(Boolean a) {
        return a;
    }

    @Override
    public Boolean getValue(int a) {
        return a != 0;
    }
}
