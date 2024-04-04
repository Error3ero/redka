package expression;

import org.w3c.dom.ls.LSInput;

import java.util.List;

public class Not extends UnaryExpression {
    public Not(Expression a) {
        super(a);
    }

    @Override
    protected char getOperation() {
        return '~';
    }

    @Override
    protected int doOperation(int x) {
        return ~a.evaluate(x);
    }

    @Override
    protected int doOperation(int x, int y, int z) {
        return ~((TripleExpression) a).evaluate(x, y, z);
    }

    @Override
    protected int doOperation(List<Integer> variables) {
        return ~((ListExpression) a).evaluate(variables);
    }
}
