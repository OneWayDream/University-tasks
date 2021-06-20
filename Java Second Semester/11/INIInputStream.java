import javafx.util.Pair;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class INIInputStream {

    protected InputStreamReader in;
    protected Charset charset;

    public INIInputStream(InputStream in, Charset charset) throws IOException {
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

    public Pair<String,String> readINI() throws IOException {
        StringBuilder key = new StringBuilder("");
        StringBuilder value = new StringBuilder("");
        int c;
        char[] ch;
        try{
            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!=' ')&&(c!=-1)){
                key.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
            in.read(ch);
            in.read(ch);

            ch = new char[1];
            c = in.read(ch);
            if (c==-1){
                return null;
            }
            while ((ch[0]!='\n')&&(c!=-1)){
                value.append(ch[0]);
                c = in.read(ch);
            }
            if (c==-1){
                return null;
            }
        } catch (IOException ex){
            throw new IOException("Can't read pair.",ex);
        }

        return new Pair<>(key.toString(),value.toString());
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
