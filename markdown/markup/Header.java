package markdown.markup;

import java.util.List;

public class Header extends Mark {
    public Header(List<Markable> list, int header_level) {
        super(list, null, null, "h" + header_level);
    }

    public Header(Markable value, int header_level) {
        super(List.of(value), null, null, "h" + header_level);
    }
}