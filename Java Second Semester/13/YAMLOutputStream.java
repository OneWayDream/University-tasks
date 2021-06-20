import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class YAMLOutputStream {

    protected OutputStreamWriter out;
    protected Charset charset;
    protected ArrayList<Student> studentArrayList;

    public YAMLOutputStream(OutputStream out, Charset charset) throws IOException{
        this.out = new OutputStreamWriter(out,charset);
        try{
            this.out.write(charset.toString());
            this.out.write("\n");
            this.out.write("\n");
        } catch (IOException ex){
            throw new IOException("Can't create new YAMLOutputStream.",ex);
        }
        this.charset = charset;
        this.out.flush();
        this.studentArrayList = new ArrayList<>();
    }

    public void writeStudent(Student student) throws IOException {
        try{
            for (int i = 0; i < studentArrayList.size();i++){
                Student stud = studentArrayList.get(i);
                if ((stud.getGroup().equals(student.getGroup()))&&(stud.getYearOfBirth()==student.getYearOfBirth())&&
                        stud.getGender().equals(student.getGender())&&(stud.getName().equals(student.getName()))){
                    throw new IOException("This student already in file");
                }
            }
            studentArrayList.add(student);
            out.write(student.getName() + ":\n"
                    + "\tgender: \"" + student.getGender() + "\"\n"
                    + "\tyearOfBirth: \"" + student.getYearOfBirth() + "\"\n"
                    + "\tgroup: \"" + student.getGroup() + "\"\n"
                    + "\n");
            out.flush();
        } catch (IOException ex){
            throw new IOException("Can't write new student in file.",ex);
        }
    }

    public String getEncoding() {
        return out.getEncoding();
    }

    public void write(int c) throws IOException {
        out.write(c);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
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
