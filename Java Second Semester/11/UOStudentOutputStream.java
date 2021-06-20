import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UOStudentOutputStream extends OutputStream {

    protected DataOutputStream out;
    protected char separator;

    public UOStudentOutputStream(OutputStream out) throws IOException {
        this.out = new DataOutputStream(out);
        this.separator = ' ';
    }

    public UOStudentOutputStream(OutputStream out,char separator) throws IOException {
        this.out = new DataOutputStream(out);
        this.separator = separator;
    }


    public void writeStudent(Student stud) throws IOException {
        if (stud.getName()!=null){
            out.writeByte(1);
            out.writeChars(stud.getName());
            if ((stud.getGroup()!=null)||(stud.getGender()!=null)||(stud.getYearOfBirth()!=-1)){
                out.writeChar(separator);
            }
        }
        if (stud.getGender()!=null){
            out.writeByte(2);
            out.writeChars(stud.getGender());
            if ((stud.getGroup()!=null)||(stud.getYearOfBirth()!=-1)){
                out.writeChar(separator);
            }
        }
        if (stud.getYearOfBirth()!=-1){
            out.writeByte(3);
            out.writeShort(stud.getYearOfBirth());
            if (stud.getGroup()!=null){
                out.writeChar(separator);
            }
        }
        if (stud.getGroup()!=null){
            out.writeByte(4);
            out.writeChars(stud.getGroup());
        }
        out.writeChar((char)13);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
