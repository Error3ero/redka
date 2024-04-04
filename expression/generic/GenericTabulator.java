package expression.generic;

import expression.generic.calculator.*;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression,
                                 int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        GenericExpression<?> ex = getExpression(mode, expression);
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        res[i][j][k] = ex.evaluate(x1 + i, y1 + j, z1 + k);
                    } catch (ArithmeticException e) {
                        res[i][j][k] = null;
                    }
                }
            }
        }
        return res;
    }

    private static GenericExpression<?> getExpression(String mode, String expression) {
        TypeParser parser = switch (mapMode(mode)) {
            case BYTE -> new GenericParser<>(new ByteCalculator());
            case UNCHECKED -> new GenericParser<>(new UncheckedIntegerCalculator());
            case BOOLEAN -> new GenericParser<>(new BooleanCalculator());
            case DOUBLE -> new GenericParser<>(new DoubleCalculator());
            case BIG_INT -> new GenericParser<>(new BigIntegerCalculator());
            case INT -> new GenericParser<>(new IntegerCalculator());
        };
        return parser.parse(expression);
    }

    private static ExpressionMode mapMode(String mode) {
        return switch (mode) {
            case "i" -> ExpressionMode.INT;
            case "d" -> ExpressionMode.DOUBLE;
            case "bi" -> ExpressionMode.BIG_INT;
            case "b" -> ExpressionMode.BYTE;
            case "u" -> ExpressionMode.UNCHECKED;
            case "bool" -> ExpressionMode.BOOLEAN;
            default -> throw new IllegalArgumentException("Unsupported mode: " + mode);
        };
    }
}
