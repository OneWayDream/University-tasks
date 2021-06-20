import java.io.*;
import java.util.Iterator;

public class TestWorkWithStudents {
    public static void main(String[] args)  {
        File file = new File("C:\\Users\\v\\IdeaProjects\\Hw4\\TestFile.txt");
        MCollection<Student> students;
        Iterator<Student> iter;
        Student stud;
        Student stud1 = new Student("Oleg","male", (short) 1999,"11-902");
        Student stud2 = new Student("Петя","муж", (short) 1989,"11-934");

        try (DStudentOutputStream sos = new DStudentOutputStream(new FileOutputStream(file))){
            sos.writeStudent(stud1);
            sos.writeStudent(stud2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        try (DStudentInputStream sis = new DStudentInputStream (new FileInputStream(file),' ');){
            students = new MCollection<>();
            students.add(sis.readStudent());
            students.add(sis.readStudent());
            iter = students.iterator();
            while(iter.hasNext()){
                stud = iter.next();
                System.out.println(stud.getName() + " " + stud.getGender() + " " + stud.getYearOfBirth() + " " +
                        stud.getGroup());

            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        try (OStudentOutputStream sos = new OStudentOutputStream(new FileOutputStream(file))){
            sos.writeStudent(stud1);
            sos.writeStudent(stud2);
        } catch (IOException e){
            e.printStackTrace();
        }

        try(OStudentInputStream sis = new OStudentInputStream(new FileInputStream(file))){
            students = new MCollection<>();
            students.add(sis.readStudent());
            students.add(sis.readStudent());
            iter = students.iterator();
            while(iter.hasNext()){
                stud = iter.next();
                System.out.println(stud.getName() + " " + stud.getGender() + " " + stud.getYearOfBirth() + " " +
                        stud.getGroup());

            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
