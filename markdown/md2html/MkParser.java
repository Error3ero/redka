package markdown.md2html;

import markdown.markup.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MkParser implements Closeable, AutoCloseable {
    private final BufferedReader reader;
    private String read;
    private char[] buffer;
    private int pos;

    public MkParser(String fileName) throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName)
                )
        );
        safeRead();
    }

    public List<Markable> paragraphs() throws IOException {
        LinkedList<Markable> paragraphs = new LinkedList<>();
        do {
            paragraphs.add(this.getParagraph());
        } while (read != null);
        return paragraphs;
    }

    private Markable getParagraph() throws IOException {
        LinkedList<Markable> strings = new LinkedList<>();
        while (Objects.equals(read, "")) {
            safeRead();
        }
        int type = 0;
        while (buffer[pos] == '#') {
            type++;
            pos++;
        }
        if (buffer[pos] != ' ') {
            type = 0;
            pos = 0;
        } else if (type != 0) {
            pos++;
        }
        if (type > 0) {
            do {
                strings.add(parseParagraph());
                safeRead();
                if (read == null) {
                    break;
                }
            } while (!Objects.equals(read, ""));
            return new Header(strings, type);
        } else {
            do {
                strings.add(parseParagraph());
                safeRead();
                if (read == null) {
                    break;
                }
            } while (!Objects.equals(read, ""));
            return new Paragraph(strings);
        }
    }


    private Text parseParagraph() throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if (buffer[pos] == '*' | buffer[pos] == '_') {
                if (buffer[pos + 1] == buffer[pos]) {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    pos++;
                    list.add(getStrong(buffer[pos - 1]));
                    start = pos--;
                } else if (buffer[pos + 1] != ' ' && Character.getType(buffer[pos + 1]) != Character.CONTROL) {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    list.add(getEmphasis(buffer[pos - 1]));
                    start = pos--;
                }
            } else if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrikeout());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            } else if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getCode());
                start = pos--;
            } else if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getVariable());
                start = pos--;
            }
            if (pos == buffer.length - 1) {
                break;
            }
        }
        if (pos == buffer.length - 1) {
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
            } else {
                list.add(new Text(new String(buffer, start, pos - start + 1)));
            }
        }
        return new Text(list);
    }

    private Emphasis getEmphasis(char edge) throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == edge && buffer[pos + 1] != buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                return new Emphasis(list);
            }
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if ((buffer[pos] == '*' || buffer[pos] == '_') && buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrong(buffer[pos - 1]));
                start = pos--;
            } else if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrikeout());
                start = pos--;
            } else if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getVariable());
                start = pos--;
            } else if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getCode());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            }
        }
        if (buffer[pos] == edge) {
            list.add(new Text(new String(buffer, start, pos++ - start)));
            pos++;
            return new Emphasis(list);
        } else {
            throw new IOException("Emphasis unclosed highlight");
        }
    }

    private Strong getStrong(char edge) throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == edge & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                return new Strong(list);
            }
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if (buffer[pos] == '*' || buffer[pos] == '_') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getEmphasis(buffer[pos - 1]));
                start = pos--;
            } else if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrikeout());
                start = pos--;
            } else if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getCode());
                start = pos--;
            } else if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getVariable());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            }
        }
        throw new IOException("Strong unclosed highlight");
    }

    private Strikeout getStrikeout() throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                return new Strikeout(list);
            }
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if (buffer[pos] == '*' | buffer[pos] == '_') {
                if (buffer[pos + 1] == buffer[pos]) {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    pos++;
                    list.add(getStrong(buffer[pos - 1]));
                    start = pos--;
                } else if (buffer[pos + 1] != ' ') {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    list.add(getEmphasis(buffer[pos - 1]));
                    start = pos--;
                }
            } else if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getCode());
                start = pos--;
            } else if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getVariable());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            }
        }
        throw new IOException("Strikeout unclosed highlight");
    }

    private Code getCode() throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                return new Code(list);
            }
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if (buffer[pos] == '*' | buffer[pos] == '_') {
                if (buffer[pos + 1] == buffer[pos]) {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    pos++;
                    list.add(getStrong(buffer[pos - 1]));
                    start = pos--;
                } else if (buffer[pos + 1] != ' ') {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    list.add(getEmphasis(buffer[pos - 1]));
                    start = pos--;
                }
            } else if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrikeout());
                start = pos--;
            } else if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getVariable());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            }
        }
        throw new IOException("Code unclosed highlight");
    }

    private Variable getVariable() throws IOException {
        LinkedList<Markable> list = new LinkedList<>();
        int start = pos;
        for (; pos < buffer.length - 1; pos++) {
            if (buffer[pos] == '%') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                return new Variable(list);
            }
            if (buffer[pos] == '<') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&lt;"));
                start = pos + 1;
            } else if (buffer[pos] == '>') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&gt;"));
                start = pos + 1;
            } else if (buffer[pos] == '&') {
                list.add(new Text(new String(buffer, start, pos - start)));
                list.add(new Text("&amp;"));
                start = pos + 1;
            } else if (buffer[pos] == '*' | buffer[pos] == '_') {
                if (buffer[pos + 1] == buffer[pos]) {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    pos++;
                    list.add(getStrong(buffer[pos - 1]));
                    start = pos--;
                } else if (buffer[pos + 1] != ' ') {
                    list.add(new Text(new String(buffer, start, pos++ - start)));
                    list.add(getEmphasis(buffer[pos - 1]));
                    start = pos--;
                }
            } else if (buffer[pos] == '-' & buffer[pos + 1] == buffer[pos]) {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                pos++;
                list.add(getStrikeout());
                start = pos--;
            } else if (buffer[pos] == '`') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(getCode());
                start = pos--;
            } else if (buffer[pos] == '\\') {
                list.add(new Text(new String(buffer, start, pos++ - start)));
                list.add(new Text(new String(buffer, pos++, 1)));
                start = pos;
            }
        }
        if (buffer[pos] == '%') {
            list.add(new Text(new String(buffer, start, pos++ - start)));
            return new Variable(list);
        }
        throw new IOException("Variable unclosed highlight");
    }


    private void safeRead() {
        try {
            String intermediate;
            read = "";
            do {
                reader.mark(1 << 20);
                intermediate = reader.readLine();
                if (intermediate == null || intermediate.isEmpty()) {
                    if (!read.isEmpty()) {
                        reader.reset();
                        break;
                    } else if (intermediate == null) {
                        read = null;
                        return;
                    } else {
                        break;
                    }
                }
                if (!read.isEmpty()) {
                    read += System.lineSeparator();
                }
                read += intermediate;
            } while (true);

            if (read != null) {
                buffer = read.toCharArray();
                pos = 0;
            }
        } catch (IOException e) {
            System.err.println("Reader exception: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
