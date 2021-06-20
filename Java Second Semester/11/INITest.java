import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class INITest {
    public static void main(String[] args) throws IOException {
        INIOutputStream out = new INIOutputStream(
                new FileOutputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\INITestFile.txt"), StandardCharsets.UTF_16BE);
        out.writePair(new Pair<>("a", "15"));
        out.writePair(new Pair<>("c", "125"));
        out.writePair(new Pair<>("d", "1252"));
        try{
            INIInputStream in = new INIInputStream(
                    new FileInputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\INITestFile.txt"), StandardCharsets.UTF_16BE);
            Pair<String,String> pair = in.readINI();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            pair = in.readINI();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            pair = in.readINI();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
