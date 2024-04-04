package expression;

import java.util.List;
import java.util.Objects;

public class Variable implements Expression, TripleExpression, ToMiniString, ListExpression {
    private final int n;
    private final boolean madeFromName;

    public Variable(int n) {
        this.n = n;
        this.madeFromName = false;
    }

    public Variable(String name) {
        this.madeFromName = true;
        this.n = switch (name) {
            case "x" -> 0;
            case "y" -> 1;
            case "z" -> 2;
            default -> throw new IllegalArgumentException("Illegal variable name: " + name);
        };
    }


    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (n) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IllegalStateException("Named variable with illegal num exists: " + n);
        };
    }

    @Override
    public String toString() {
        if (madeFromName) {
            return switch (n) {
                case 0 -> "x";
                case 1 -> "y";
                case 2 -> "z";
                default -> throw new IllegalStateException("Named variable with illegal num exists: " + n);
            };
        }
        return "$" + n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(n, variable.n);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        if (variables.size() + 1 < n) {
            throw new IllegalArgumentException("no value for this variable: " + this);
        }
        return variables.get(n);
    }
}
