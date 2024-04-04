package expression;

import java.util.List;
public interface TripleExpression extends ToMiniString {
    int evaluate(int x, int y, int z);
}
