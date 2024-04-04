package markdown.markup;

import java.util.List;

public class Code extends Mark {
    public Code(List<Markable> list) {
        super(list, null, null, "code");
    }

    public Code(Markable value) {
        super(List.of(value), null, null, "code");
    }

}
