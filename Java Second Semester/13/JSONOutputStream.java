import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JSONOutputStream {

    protected OutputStreamWriter out;
    protected Charset charset;
    protected boolean isFirst;
    protected ArrayList<Student> studentArrayList;

    public JSONOutputStream(OutputStream out,Charset charset) throws IOException {
        this.out = new OutputStreamWriter(out,charset);
        try{
            this.out.write(charset.toString());
        } catch (IOException ex){
            throw new IOException("Can't create new JSONOutputStream.",ex);
        }
        this.charset = charset;
        this.out.flush();
        this.isFirst = true;
        this.studentArrayList = new ArrayList<>();
    }

    public void writeStudent(Student student) throws IOException {
        try{
            if (isFirst){
                out.write("\n");
                isFirst=false;
            } else {
                out.write(",\n");
            }
            for (int i = 0; i < studentArrayList.size();i++){
                Student stud = studentArrayList.get(i);
                if ((stud.getGroup().equals(student.getGroup()))&&(stud.getYearOfBirth()==student.getYearOfBirth())&&
                        stud.getGender().equals(student.getGender())&&(stud.getName().equals(student.getName()))){
                    throw new IOException("This student already in file");
                }
            }
            studentArrayList.add(student);
            out.write("{\n"
                    + "\t\"name\": \"" + student.getName() + "\",\n"
                    + "\t\"gender\": \"" + student.getGender() + "\",\n"
                    + "\t\"yearOfBirth\": \"" + student.getYearOfBirth() + "\",\n"
                    + "\t\"group\": \"" + student.getGroup() + "\"\n"
                    + "}");
            out.flush();
        } catch (IOException ex){
            throw new IOException("Can't write new student in file.",ex);
        }
    }

    public void write(int b) throws IOException {
        out.write(b);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    public String getEncoding() {
        return out.getEncoding();
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }

    public void write(char[] cbuf) throws IOException {
        out.write(cbuf);
    }

    public void write(String str) throws IOException {
        out.write(str);
    }

    public Writer append(CharSequence csq) throws IOException {
        return out.append(csq);
    }

    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return out.append(csq, start, end);
    }

    public Writer append(char c) throws IOException {
        return out.append(c);
    }
}
