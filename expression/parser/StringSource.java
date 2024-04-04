package expression.parser;

import java.util.Arrays;

public class StringSource implements CharSource {
    private final char[] source;
    private int pos = 0;

    public StringSource(String string) {
        source = string.toCharArray();
    }

    @Override
    public int size() {
        return source.length;
    }

    @Override
    public boolean hasNext() {
        return pos < source.length;
    }

    @Override
    public char next() {
        return source[pos++];
    }

    @Override
    public char checkNext() {
        if (hasNext()) {
            return source[pos];
        } else {
            return (char) -1;
        }
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return new String(source);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
