import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class OStudentOutputStream extends OutputStream {

    protected ObjectOutputStream out;
    protected char separator;

    public OStudentOutputStream(OutputStream out) throws IOException {
        this.out = new ObjectOutputStream(out);
        this.separator = ' ';
    }

    public OStudentOutputStream(OutputStream out,char separator) throws IOException {
        this.out = new ObjectOutputStream(out);
        this.separator = separator;
    }

    public void writeStudent(Student student) throws IOException {
         try {
            out.writeObject(student);
        } catch (IOException e){
            throw new IOException("Can't write student.",e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] buf) throws IOException {
        out.write(buf);
    }

    @Override
    public void write(byte[] buf, int off, int len) throws IOException {
        out.write(buf, off, len);
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
