package expression.generic;

public interface GenericExpression<T> {
    T evaluate(int x, int y, int z);
}