package expression.generic;

import expression.generic.calculator.Calculator;
import expression.parser.BaseParser;
import expression.parser.StringSource;

public class GenericParser<T> extends BaseParser implements TypeParser {
    private final Calculator<T> calculator;
    private final byte MAX_PRIORITY = 1;


    public GenericExpression<T> parse(String expression) {
        setSource(new StringSource(expression));
        return getExpression();
    }

    public GenericParser(Calculator<T> calculator) {
        super(new StringSource("\0"));
        this.calculator = calculator;
    }

    private GenericExpression<T> getExpression() {
        final GenericExpression<T> result = parseByPriority(0);
        if (eof()) {
            return result;
        }
        throw error("End of expression expected");
    }

    private GenericExpression<T> parseByPriority(final int priority) {
        skipWhitespace();
        final GenericExpression<T> first = priority == MAX_PRIORITY ? parseFactor() : parseByPriority(priority + 1);
        skipWhitespace();
        for (char op : operatorsOfPriority(priority)) {
            if (take(op)) {
                final GenericExpression<T> second = priority == MAX_PRIORITY ? parseFactor() : parseByPriority(priority + 1);
                skipWhitespace();
                return getBinary(op, first, second);
            }
        }
        return first;
    }

    private GenericExpression<T> parseFactor() {
        skipWhitespace();
        if (take('-')) {
            return parseUnary(true);
        }
        return parseUnary(false);
    }

    private GenericExpression<T> parseUnary(boolean isNegated) {
        skipWhitespace();
        GenericExpression<T> result;
        if (take('(')) {
            result = parseByPriority(0);
            if (!take(')')) {
                throw error("Closing parenthesis expected");
            }
        } else if (test('x') || test('y') || test('z')) {
            result = new GenericVariable<>(String.valueOf(take()), calculator);
        } else {
            StringBuilder sb = new StringBuilder();
            while (test(Character.DECIMAL_DIGIT_NUMBER)) {
                sb.append(take());
            }
            if (isNegated && sb.toString().equals("2147483648")) {
                return new GenericConst<>(Integer.MIN_VALUE, calculator);
            }
            result = new GenericConst<>(Integer.parseInt(sb.toString()), calculator);
        }
        if (isNegated) {
            return new GenericNegate<>(result, calculator);
        }
        return result;
    }

    private char[] operatorsOfPriority(final int priority) {
        return switch (priority) {
            case 0 -> new char[]{'+', '-'};
            case 1 -> new char[]{'*', '/'};
            default -> throw error("unexpected priority: " + priority);
        };
    }

    private GenericExpression<T> getBinary(final char op, final GenericExpression<T> a, final GenericExpression<T> b) {
        return switch (op) {
            case '+' -> new GenericAdd<>(a, b, calculator);
            case '-' -> new GenericSubtract<>(a, b, calculator);
            case '*' -> new GenericMultiply<>(a, b, calculator);
            case '/' -> new GenericDivide<>(a, b, calculator);
            default -> throw error("unexpected operator: " + op);
        };
    }

    private void skipWhitespace() {
        while (take(' ') || take('\r') || take('\n') || take('\t')) {
            // skip
        }
    }
}