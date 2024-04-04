package expression.exceptions;

import expression.ListExpression;

import java.util.List;

public interface ListParser {
    ListExpression parse(String expression, final List<String> variables) throws Exception;
}
