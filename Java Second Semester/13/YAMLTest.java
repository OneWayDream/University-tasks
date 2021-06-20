import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class YAMLTest {
    public static void main(String[] args) throws IOException {
        YAMLOutputStream yout = new YAMLOutputStream(
                new FileOutputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\YAMLTestFile.txt"), StandardCharsets.UTF_16BE);
        yout.writeStudent(new Student("Valera","male",(short)1999,"A"));
        yout.writeStudent(new Student("Masha","female",(short)2000,"B"));
        yout.writeStudent(new Student("Max","male",(short)2001,"C"));

        YAMLInputStream jin = new YAMLInputStream(
                new FileInputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\YAMLTestFile.txt"), StandardCharsets.UTF_16BE);
        for (int i = 0;i<3;i++){
            Student student = jin.readStudent();
            System.out.print(student.getName() + ":\n"
                    + "\tgender: \"" + student.getGender() + "\"\n"
                    + "\tyearOfBirth: \"" + student.getYearOfBirth() + "\"\n"
                    + "\tgroup: \"" + student.getGroup() + "\"\n"
                    + "\n");
        }
    }
}
