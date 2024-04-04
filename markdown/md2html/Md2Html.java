package markdown.md2html;

import markdown.markup.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Md2Html {
    public static void main(String[] args) {
        LinkedList<Markable> parsedList = new LinkedList<>();
        try (MkParser parser = new MkParser(args[0])){
            List<Markable> list = parser.paragraphs();
            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1])))) {
                for (Markable paragraph: list) {
                    wr.write(paragraph.HTMLValuesToString() + System.lineSeparator());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }
}


