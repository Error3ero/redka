package expression.generic.calculator;

public class IntegerCalculator implements Calculator<Integer> {

    @Override
    public Integer add(Integer a, Integer b) {
        int r = a + b;
        if (((a ^ r) & (b ^ r)) < 0) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        Integer r = a - b;
        if (b < 0 && r < a || b > 0 && r > a) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new ArithmeticException("overflow");
        }
        return a / b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (a != 0 && b != 0 && (a > 0 && b > 0 && a > Integer.MAX_VALUE / b || a < 0 && b < 0 && a <
                Integer.MAX_VALUE / b || a > 0 && b < 0 &&
                b < Integer.MIN_VALUE / a || a < 0 && b > 0 && a < Integer.MIN_VALUE / b)) {
            throw new ArithmeticException("overflow");
        }
        return a * b;
    }

    @Override
    public Integer negate(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -a;
    }

    @Override
    public Integer getValue(int a) {
        return a;
    }
}
