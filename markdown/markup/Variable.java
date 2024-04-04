package markdown.markup;

import java.util.List;

public class Variable extends Mark{
    public Variable(List<Markable> list) {
        super(list, null, null, "var");
    }
}
