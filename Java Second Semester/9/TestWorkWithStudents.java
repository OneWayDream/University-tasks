import java.io.File;
import java.util.Iterator;

public class TestWorkWithStudents {
    public static void main(String[] args) throws LongNameException, LongGenderException,LongGroupException {
        File file = new File("C:\\Users\\v\\IdeaProjects\\Hw4\\TestFile.txt");
        MCollection<Student> students = new MCollection<>();
        students.add(new Student("Oleg","male", (short) 1999,"11-902"));
        students.add(new Student("Петя","муж", (short) 1989,"11-934"));
        WorkWithStudents.write(file,students,',');
        MCollection<Student> res = (MCollection<Student>) WorkWithStudents.read(file,',');
        System.out.println(res.equals(students));;
        Iterator<Student> iter = res.iterator();
        Student stud;
        while(iter.hasNext()){
            stud = iter.next();
            System.out.println(stud.getName() + " " + stud.getGender() + " " + stud.getYearOfBirth() + " " +
                    stud.getGroup());
        }
    }
}
