
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DStudentInputStream extends InputStream {

    protected DataInputStream in;
    protected char separator;

    public DStudentInputStream(InputStream in){
        this.in = new DataInputStream(in);
        this.separator = ' ';
    }

    public DStudentInputStream(InputStream in,char separator){
        this.in = new DataInputStream(in);
        this.separator = separator;
    }

    public Student readStudent() throws IOException, LongNameException, LongGenderException, LongGroupException {
        StringBuilder name = new StringBuilder("");
        StringBuilder gender = new StringBuilder("");
        short yearOfBirth;
        StringBuilder group = new StringBuilder("");
        int counter = 0;
        char ch;
        try{
            while ((counter<=15)&&((ch = in.readChar())!=separator)){
                name.append(ch);
                counter++;
            }
            if (counter>15){
                throw new LongNameException();
            }
        } catch (IOException e){
            throw new IOException("Can't read next char for name.",e);
        }

        counter = 0;

        try{
            while ((counter<=6)&&((ch = in.readChar())!=separator)){
                gender.append(ch);
                counter++;
            }
            if (counter>6){
                throw new LongGenderException();
            }
        } catch (IOException e){
            throw new IOException("Can't read next char for gender.",e);
        }

        try{
            yearOfBirth = in.readShort();
            in.readChar();
        } catch (IOException e){
            throw new IOException("Can't read year of birth.",e);
        }

        counter = 0;
        try{
            while ((counter<=15)&&((ch = in.readChar())!=(char)13)){
                group.append(ch);
                counter++;
            }
            if (counter>15){
                throw new LongGroupException();
            }
        } catch (IOException e){
            throw new IOException("Can't read next char for group.",e);
        }

        return new Student(name.toString(),gender.toString(),yearOfBirth,group.toString());
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
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
