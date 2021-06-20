import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class OStudentInputStream extends InputStream {

    protected ObjectInputStream in;
    protected char separator;

    public OStudentInputStream(InputStream in) throws IOException {
        this.in = new ObjectInputStream(in);
        this.separator = ' ';
    }

    public OStudentInputStream(InputStream in, char separator) throws IOException {
        this.in = new ObjectInputStream(new DataInputStream(in));
        this.separator = separator;
    }

    public Student readStudent() throws IOException, ClassNotFoundException {
        try{
            return (Student) in.readObject();
        } catch (IOException e){
            throw new IOException("Can't read student.",e);
        }
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return in.read(buf, off, len);
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    @Override
    public void mark(int readlimit) {
        in.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        in.reset();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }
}
