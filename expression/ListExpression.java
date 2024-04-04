package expression;

import java.util.List;

public interface ListExpression extends ToMiniString {
    int evaluate(List<Integer> variables);
}
