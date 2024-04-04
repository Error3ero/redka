package markdown.markup;

import java.util.List;

public class Paragraph extends Mark {
    public Paragraph(List<Markable> list) {
        super(list,null, null, "p");
    }

    public Paragraph(Markable value) {
        super(List.of(value),null, null, "p");
    }

    @Override
    public String markedValuesToString() {
        StringBuilder result = new StringBuilder();
        for (Markable value : values) {
            result.append(value.markedValuesToString());
        }
        return result.toString();
    }

    @Override
    public String BBCValuesToString() {
        StringBuilder result = new StringBuilder();
        for (Markable value : values) {
            result.append(value.BBCValuesToString());
        }
        return result.toString();
    }
}