package markdown.markup;

import java.util.List;

public class Text implements Markable {
    protected final String value;

    public Text(String string) {
        value = string;
    }

    public Text(List<Markable> list) { //ponos
        StringBuilder sb = new StringBuilder();
        for (Markable mark : list) {
            sb.append(mark.HTMLValuesToString());
        }
        value = sb.toString();
    }

    @Override
    public String markedValuesToString() {
        return value;
    }

    @Override
    public String BBCValuesToString() {
        return value;
    }

    @Override
    public String HTMLValuesToString() {
        return value;
    }
}
