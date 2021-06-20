import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {

    protected String name;
    protected String gender;
    protected short yearOfBirth;
    protected String group;

    public Student(String name, String gender, short yearOfBirth, String group){
        this.name = name;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.group = group;
    }

    public String getName(){
        return this.name;
    }

    public String getGender(){
        return this.gender;
    }

    public short getYearOfBirth(){
        return this.yearOfBirth;
    }

    public String getGroup(){
        return this.group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return yearOfBirth == student.yearOfBirth &&
                Objects.equals(name, student.name) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(group, student.group);
    }

}
