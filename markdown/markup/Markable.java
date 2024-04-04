package markdown.markup;

public interface Markable {
    default void toMarkdown(StringBuilder stringBuilder) {}

    default void toBBCode(StringBuilder stringBuilder) {}

    default void toHTML(StringBuilder stringBuilder) {}

    default String markedValuesToString() {return null;}

    default String BBCValuesToString() {return null;}

    default String HTMLValuesToString() {return null;}
}
