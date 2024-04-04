package markdown.markup;

import java.util.List;

public abstract class Mark implements Markable {
    protected final String HIGHLIGHT_CHARS;
    protected final String BBC_CHAR;
    protected final String HTML_CHAR;
    protected List<Markable> values;

    public Mark(List<Markable> values, String HIGHLIGHT_CHARS, String BBC_CHAR, String HTML_CHAR) {
        this.values = values;
        this.HIGHLIGHT_CHARS = HIGHLIGHT_CHARS;
        this.BBC_CHAR = BBC_CHAR;
        this.HTML_CHAR = HTML_CHAR;
    }

    @Override
    public String markedValuesToString() {
        StringBuilder result = new StringBuilder();
        for (Markable value : values) {
            result.append(value.markedValuesToString());
        }
        return HIGHLIGHT_CHARS + result.toString() + HIGHLIGHT_CHARS;
    }

    @Override
    public String BBCValuesToString() {
        StringBuilder result = new StringBuilder();
        for (Markable value : values) {
            result.append(value.BBCValuesToString());
        }
        return "[" + BBC_CHAR + "]" + result + "[/" + BBC_CHAR + "]";
    }

    @Override
    public String HTMLValuesToString() {
        StringBuilder result = new StringBuilder();
        for (Markable value : values) {
            result.append(value.HTMLValuesToString());
        }
        return "<" + HTML_CHAR + ">" + result + "</" + HTML_CHAR + ">";
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        if (stringBuilder.isEmpty()) {
            stringBuilder.append(this.markedValuesToString());
            return;
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(this.markedValuesToString());
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        if (stringBuilder.isEmpty()) {
            stringBuilder.append(this.BBCValuesToString());
            return;
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(this.BBCValuesToString());
    }

    @Override
    public void toHTML(StringBuilder stringBuilder) {
        if (stringBuilder.isEmpty()) {
            stringBuilder.append(this.HTMLValuesToString());
            return;
        }
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(this.HTMLValuesToString());
    }
}
