import java.io.*;

public class DStudentOutputStream extends OutputStream {

    protected DataOutputStream out;
    protected char separator;

    public DStudentOutputStream(OutputStream out){
        this.out = new DataOutputStream(out);
        this.separator = ' ';
    }

    public DStudentOutputStream(OutputStream out,char separator){
        this.out = new DataOutputStream(out);
        this.separator = separator;
    }

    public void writeStudent(Student stud) throws IOException {
        try{
            out.writeChars(stud.getName());
            out.writeChar(separator);
            out.writeChars(stud.getGender());
            out.writeChar(separator);
            out.writeShort(stud.getYearOfBirth());
            out.writeChar(separator);
            out.writeChars(stud.getGroup());
            out.writeChar((char)13);
        } catch (IOException e){
            throw new IOException("Can't write student in the file.",e);
        }
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
    public void close() throws IOException {
        out.close();
    }

}
