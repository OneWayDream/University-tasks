import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
public class UOStudentInputStream extends InputStream {

    protected DataInputStream in;
    protected char separator;

    public UOStudentInputStream(InputStream in) throws IOException {
        this.in = new DataInputStream(in);
        this.separator = ' ';
    }

    public UOStudentInputStream(InputStream in,char separator) throws IOException {
        this.in = new DataInputStream(in);
        this.separator = separator;
    }

    public Student readStudent() throws IOException, LongNameException, LongGenderException, LongGroupException {
        StringBuilder name = new StringBuilder("");
        StringBuilder gender = new StringBuilder("");
        StringBuilder group = new StringBuilder("");
        String nameans,genderans,groupans;
        short yearOfBirth = -1;
        int counter;
        char ch = 0;
        byte b;
        boolean end = true;
        while((end)&&((b = (byte) in.read())!=-1)){
            counter = 0;
            switch (b){
                case 1:
                    while ((counter<=15)&&((ch = in.readChar())!=separator)&&(ch!=(char)13)){
                        name.append(ch);
                        counter++;
                    }
                    if (counter>15){
                        throw new LongNameException();
                    }
                    break;
                case 2:
                    while ((counter<=6)&&((ch = in.readChar())!=separator)&&(ch!=(char)13)){
                        gender.append(ch);
                        counter++;
                    }
                    if (counter>6){
                        throw new LongGenderException();
                    }
                    break;
                case 3:
                    yearOfBirth = in.readShort();
                    ch = in.readChar();
                    break;
                case 4:
                    while ((counter<=15)&&((ch = in.readChar())!=(char)13)&&(ch!=(char)13)){
                        group.append(ch);
                        counter++;
                    }
                    if (counter>15){
                        throw new LongGroupException();
                    }
                    break;
            }
            if (ch==(char)13){
                end = false;
            }
        }
        nameans = name.toString();
        genderans = gender.toString();
        groupans = group.toString();
        if (name.toString().equals("")){
            nameans = null;
        }
        if (gender.toString().equals("")){
            genderans = null;
        }
        if (group.toString().equals("")){
            groupans = null;
        }
        return new Student(nameans,genderans,yearOfBirth,groupans);
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
