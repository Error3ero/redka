package markdown.markup;

import java.util.List;

public class Strong extends Mark {
    public Strong(List<Markable> list) {
        super(list,"__", "b", "strong");
    }

    public Strong(Markable value) {
        super(List.of(value),"__", "b", "strong");
    }
}
