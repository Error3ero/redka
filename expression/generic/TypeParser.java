package expression.generic;

public interface TypeParser {
    GenericExpression<?> parse(String expression);
}
