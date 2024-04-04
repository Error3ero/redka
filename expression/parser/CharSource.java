package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    char checkNext();
    int getPos();
    int size();
    IllegalArgumentException error(String message);
}