import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class YAMLInputStream {

    protected InputStreamReader in;
    protected Charset charset;

    public YAMLInputStream(InputStream in, Charset charset) throws IOException {
        try{
            this.in = new InputStreamReader(in,charset);
            this.charset = charset;
            StringBuilder str = new StringBuilder("");
            char[] ch = new char[1];
            int c = this.in.read(ch);
            if (c==-1){
                throw new IOException("File is empty.");
            }
            while ((ch[0] != '\n')&&(c != -1)){
                str.append(ch[0]);
                c = this.in.read(ch);
            }
            if (!(str.toString().equals(charset.toString()))){
                throw new IOException("Incorrect charset");
            }
            this.in.read(ch);
        } catch (IOException ex){
            throw new IOException("Can't create INIInputStream.",ex);
        }
    }

    public Student readStudent() throws IOException {
        StringBuilder name = new StringBuilder("");
        StringBuilder gender = new StringBuilder("");
        short yearOfBirth = 0;
        StringBuilder group = new StringBuilder("");
        int c;
        char[] ch;

        try{

            // read name and (\n)
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!=':')&&(c!=-1)){
                name.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
            in.read(ch);

            // read (gender: ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[9];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='g')||(ch[1]!='e')||(ch[2]!='n')||(ch[3]!='d')||(ch[4]!='e')||(ch[5]!='r')
                    ||(ch[6]!=':')||(ch[7]!=' ')||(ch[8]!='"')){
                throw new IOException("Incorrect student record format in file");
            }

            // read gender
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!='"')&&(c!=-1)){
                gender.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
            in.read(ch);

            // read (yearOfBirth: ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[14];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='y')||(ch[1]!='e')||(ch[2]!='a')||(ch[3]!='r')||(ch[4]!='O')||(ch[5]!='f')
                    ||(ch[6]!='B')||(ch[7]!='i')||(ch[8]!='r')||(ch[9]!='t')||(ch[10]!='h') ||(ch[11]!=':')
                    ||(ch[12]!=' ')||(ch[13]!='"')){
                throw new IOException("Incorrect student record format in file");
            }

            // read yearOfBirth
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!='"')&&(c!=-1)){
                yearOfBirth+=((short)ch[0]-48);
                yearOfBirth*=10;
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
            in.read(ch);

            // read (group: ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[8];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='g')||(ch[1]!='r')||(ch[2]!='o')||(ch[3]!='u')||(ch[4]!='p')||(ch[5]!=':')
                    ||(ch[6]!=' ')||(ch[7]!='"')){
                throw new IOException("Incorrect student record format in file");
            }

            // read group
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!='"')&&(c!=-1)){
                group.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
            in.read(ch);
            in.read(ch);

        } catch (IOException ex){
            throw new IOException("Can't read new student from this file.",ex);
        }

        return new Student(name.toString(),gender.toString(),(short)(yearOfBirth/10),group.toString());
    }

    public String getEncoding() {
        return in.getEncoding();
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(char[] cbuf, int offset, int length) throws IOException {
        return in.read(cbuf, offset, length);
    }

    public boolean ready() throws IOException {
        return in.ready();
    }

    public void close() throws IOException {
        in.close();
    }

    public int read(CharBuffer target) throws IOException {
        return in.read(target);
    }

    public int read(char[] cbuf) throws IOException {
        return in.read(cbuf);
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public void mark(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
    }

    public void reset() throws IOException {
        in.reset();
    }
}
