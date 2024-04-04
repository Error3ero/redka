package markdown.markup;

import java.util.List;

public class Emphasis extends Mark {
    public Emphasis(List<Markable> list) {
        super(list,"*", "i", "em");
    }

    public Emphasis(Markable value) {
        super(List.of(value),"*", "i", "em");
    }
}
