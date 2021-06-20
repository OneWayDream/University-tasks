import javafx.util.Pair;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class INIOutputStream {

    protected OutputStreamWriter out;
    protected Charset charset;
    protected ArrayList<String> keyArrayList;

    public INIOutputStream(OutputStream out, Charset charset) throws IOException {
        this.out = new OutputStreamWriter(out, charset);
        try{
            this.out.write(charset.toString());
            this.out.write("\n");
        } catch (IOException ex){
            throw new IOException("Can't create new INIOutputStream.",ex);
        }
        this.charset = charset;
        this.keyArrayList = new ArrayList<>();
    }

    public void writePair(Pair<String, String> pair) throws IOException {
        try{
            for (int i = 0;i<keyArrayList.size();i++){
                if (pair.getKey().equals(keyArrayList.get(i))){
                    throw new IOException("This KEY already in file");
                }
            }
            keyArrayList.add(pair.getKey());
            out.write(pair.getKey());
            out.write(" = ");
            out.write(pair.getValue());
            out.write("\n");
            out.flush();
        } catch (IOException ex){
            throw new IOException("Can't write pair",ex);
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
