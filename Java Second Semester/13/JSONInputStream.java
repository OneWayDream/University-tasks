import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class JSONInputStream {

    protected InputStreamReader in;
    protected Charset charset;

    public JSONInputStream(InputStream in,Charset charset) throws IOException{
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
        } catch (IOException ex){
            throw new IOException("Can't create INIInputStream.",ex);
        }
    }

    public Student readStudent() throws IOException{
        StringBuilder name = new StringBuilder("");
        StringBuilder gender = new StringBuilder("");
        short yearOfBirth = 0;
        StringBuilder group = new StringBuilder("");
        int c;
        char[] ch;

        try{

            // read ({\n)
            ch = new char[2];
            c = in.read(ch);
            if (c==-1){
                return null;
            } else if ((ch[0]!='{')||(ch[1]!='\n')){
                throw new IOException("Incorrect student record format in file");
            }

            // read (\t"name": ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[9];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='"')||(ch[1]!='n')||(ch[2]!='a')||(ch[3]!='m')||(ch[4]!='e')||(ch[5]!='"')||(ch[6]!=':')
                    ||(ch[7]!=' ')||(ch[8]!='"')){
                throw new IOException("Incorrect student record format in file");
            }

            // read name
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!='"')&&(c!=-1)){
                name.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }

            // read (,\n)
            ch = new char[2];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!=',')||(ch[1]!='\n')){
                throw new IOException("Incorrect student record format in file");
            }

            // read ("gender": ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[11];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='"')||(ch[1]!='g')||(ch[2]!='e')||(ch[3]!='n')||(ch[4]!='d')||(ch[5]!='e')||(ch[6]!='r')
                    ||(ch[7]!='"')||(ch[8]!=':')||(ch[9]!=' ')||(ch[10]!='"')){
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

            // read (,\n)
            ch = new char[2];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!=',')||(ch[1]!='\n')){
                throw new IOException("Incorrect student record format in file");
            }

            // read ("yearOfBirth": ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[16];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='"')||(ch[1]!='y')||(ch[2]!='e')||(ch[3]!='a')||(ch[4]!='r')||(ch[5]!='O')||(ch[6]!='f')
                    ||(ch[7]!='B')||(ch[8]!='i')||(ch[9]!='r')||(ch[10]!='t')||(ch[11]!='h')||(ch[12]!='"')
                    ||(ch[13]!=':')||(ch[14]!=' ')||(ch[15]!='"')){
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

            // read (,\n)
            ch = new char[2];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!=',')||(ch[1]!='\n')){
                throw new IOException("Incorrect student record format in file");
            }

            // read ("group": ")
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\t')){
                throw new IOException("Incorrect student record format in file");
            }
            ch = new char[10];
            c = in.read(ch);
            if (c==-1){
                throw new IOException("Student haven't name.");
            } else if ((ch[0]!='"')||(ch[1]!='g')||(ch[2]!='r')||(ch[3]!='o')||(ch[4]!='u')||(ch[5]!='p')
                    ||(ch[6]!='"')||(ch[7]!=':')||(ch[8]!=' ')||(ch[9]!='"')){
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

            // read (\n)
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='\n')){
                throw new IOException("Incorrect student record format in file");
            }

            // read (}\n) or (},\n) or ({)
            ch = new char[1];
            c = in.read(ch);
            if ((c==-1)||(ch[0]!='}')){
                throw new IOException("Incorrect student record format in file");
            }
            c = in.read(ch);
            if (c!=-1){
                if ((ch[0]!=',')&&(ch[0]!='\n')){
                    throw new IOException("Incorrect student record format in file");
                }
                if (ch[0]==','){
                    ch = new char[1];
                    c = in.read(ch);
                    if ((c==-1)||(ch[0]!='\n')){
                        throw new IOException("Incorrect student record format in file");
                    }
                }
            }

        } catch (IOException ex){
            throw new IOException("Can't read new student from this file.",ex);
        }

        return new Student(name.toString(),gender.toString(),(short)(yearOfBirth/10),group.toString());
    }

    public int read() throws IOException {
        return in.read();
    }


    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public void close() throws IOException {
        in.close();
    }

    public void reset() throws IOException {
        in.reset();
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public String getEncoding() {
        return in.getEncoding();
    }

    public int read(char[] cbuf, int offset, int length) throws IOException {
        return in.read(cbuf, offset, length);
    }

    public boolean ready() throws IOException {
        return in.ready();
    }

    public int read(CharBuffer target) throws IOException {
        return in.read(target);
    }

    public int read(char[] cbuf) throws IOException {
        return in.read(cbuf);
    }

    public void mark(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
    }
}
