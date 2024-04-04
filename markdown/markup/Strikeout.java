package markdown.markup;

import java.util.List;

public class Strikeout extends Mark {
    public Strikeout(List<Markable> list) {
        super(list,"~", "s", "s");
    }

    public Strikeout(Markable value) {
        super(List.of(value),"~", "s", "s");

    }
}
