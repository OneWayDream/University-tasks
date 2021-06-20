import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException, LongNameException, LongGenderException, LongGroupException {
        File file = new File("C:\\Users\\v\\IdeaProjects\\Hw5\\TestFile.txt");
        UOStudentOutputStream sdid = new UOStudentOutputStream(new FileOutputStream(file));
        sdid.writeStudent(new Student(null,"Male", (short) 2,null));
        sdid.writeStudent(new Student("Valera","Male", (short) 2001,""));
        UOStudentInputStream sdod = new UOStudentInputStream(new FileInputStream(file));
        Student stud = sdod.readStudent();
        System.out.print(stud.getName() + " ");
        System.out.print(stud.getGender() + " ");
        System.out.print(stud.getYearOfBirth() + " ");
        System.out.println(stud.getGroup());
        stud = sdod.readStudent();
        System.out.print(stud.getName() + " ");
        System.out.print(stud.getGender() + " ");
        System.out.print(stud.getYearOfBirth() + " ");
        System.out.print(stud.getGroup() + " ");
    }
}
