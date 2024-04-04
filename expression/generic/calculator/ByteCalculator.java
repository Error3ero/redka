package expression.generic.calculator;

public class ByteCalculator implements Calculator<Byte> {
    @Override
    public Byte add(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte subtract(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte divide(Byte a, Byte b) {
        return (byte) (a / b);
    }

    @Override
    public Byte multiply(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte negate(Byte a) {
        return (byte) (-a);
    }

    @Override
    public Byte getValue(int a) {
        return (byte) a;
    }
}
