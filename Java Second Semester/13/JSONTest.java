import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.io.IOException;

public class JSONTest {
    public static void main(String[] args) throws IOException {
        JSONOutputStream jout = new JSONOutputStream(
                new FileOutputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\JSONTestFile.txt"), StandardCharsets.UTF_16BE);
        jout.writeStudent(new Student("Valera","male",(short)1999,"A"));
        jout.writeStudent(new Student("Masha","female",(short)2000,"B"));
        jout.writeStudent(new Student("Max","male",(short)2001,"C"));

        JSONInputStream jin = new JSONInputStream(
                new FileInputStream("C:\\Users\\v\\IdeaProjects\\Hw5\\JSONTestFile.txt"), StandardCharsets.UTF_16BE);
        for (int i = 0;i<3;i++){
            Student student = jin.readStudent();
                System.out.println(",\n");
            System.out.println("{\n"
                    + "\t\"name\": \"" + student.getName() + "\",\n"
                    + "\t\"gender\": \"" + student.getGender() + "\",\n"
                    + "\t\"yearOfBirth\": \"" + student.getYearOfBirth() + "\",\n"
                    + "\t\"group\": \"" + student.getGroup() + "\"\n"
                    + "}");
        }
    }
}
