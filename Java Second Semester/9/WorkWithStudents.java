import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

public class WorkWithStudents {

    public static Collection<Student> read(File file, char separator) throws LongNameException, LongGenderException, LongGroupException{
        ByteBuffer bb;
        StringBuilder name;
        StringBuilder gender;
        short yearOfBirth;
        StringBuilder group;
        byte counter;
        int b;
        ByteBuffer ch;
        MCollection<Student> collection = new MCollection<>();
        try(FileInputStream in = new FileInputStream(file)) {
            while ((b = in.read())!=-1){
                name = new StringBuilder("");
                gender = new StringBuilder("");
                group = new StringBuilder("");

                bb = ByteBuffer.allocate(30);
                counter = 0;
                ch = ByteBuffer.allocate(2);
                ch.put((byte)b);
                ch.put((byte)in.read());
                ch.rewind();
                while ((counter<=15)&&(ch.getChar()!=separator)){
                    ch.rewind();
                    name.append(ch.getChar());
                    counter++;
                    ch = ByteBuffer.allocate(2);
                    ch.put((byte)in.read());
                    ch.put((byte)in.read());
                    ch.rewind();
                }
                if (counter==16){
                    throw new LongNameException("Name is longer than allowed");
                }

                bb = ByteBuffer.allocate(12);
                counter = 0;
                ch = ByteBuffer.allocate(2);
                ch.put((byte)in.read());
                ch.put((byte)in.read());
                ch.rewind();
                while ((counter<=6)&&(ch.getChar()!=separator)){
                    ch.rewind();
                    gender.append(ch.getChar());
                    counter++;
                    ch = ByteBuffer.allocate(2);
                    ch.put((byte)in.read());
                    ch.put((byte)in.read());
                    ch.rewind();
                }
                if (counter==7){
                    throw new LongGenderException("Gender is longer than allowed");
                }
                bb.rewind();

                bb = ByteBuffer.allocate(2);
                bb.put((byte)in.read());
                bb.put((byte)in.read());
                in.read();
                in.read();
                bb.rewind();
                yearOfBirth = bb.getShort();

                bb = ByteBuffer.allocate(21);
                counter = 0;
                while ((counter<=21)&&((b = in.read())!=13)){
                    bb.put((byte)b);
                    counter++;
                }
                if (counter==21){
                    throw new LongGroupException("Group is longer than allowed");
                }
                bb.rewind();
                for (int i = 0;i<counter/2;i++){
                    group.append(bb.getChar());
                }

                collection.add(new Student(name.toString(),gender.toString(),yearOfBirth,group.toString()));

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (Collection) collection;
    }

    public static boolean write(File file, Collection<Student> collection,char separator){
        try(FileOutputStream out = new FileOutputStream(file)) {
            Iterator<Student> iter = collection.iterator();
            Student stud;
            char ch;
            ByteBuffer bb;
            while(iter.hasNext()){
                stud = iter.next();

                for (int i = 0;i<stud.getName().length();i++){
                    bb = ByteBuffer.allocate(2);
                    bb.putChar(stud.getName().charAt(i));
                    out.write(bb.array());
                }
                bb = ByteBuffer.allocate(2);
                bb.putChar(separator);
                out.write(bb.array());

                for (int i = 0;i<stud.getGender().length();i++){
                    bb = ByteBuffer.allocate(2);
                    bb.putChar(stud.getGender().charAt(i));
                    out.write(bb.array());
                }
                bb = ByteBuffer.allocate(2);
                bb.putChar(separator);
                out.write(bb.array());

                bb = ByteBuffer.allocate(2);
                bb.putShort(stud.getYearOfBirth());
                out.write(bb.array());
                bb = ByteBuffer.allocate(2);
                bb.putChar(separator);
                out.write(bb.array());

                for (int i = 0;i<stud.getGroup().length();i++){
                    bb = ByteBuffer.allocate(2);
                    bb.putChar(stud.getGroup().charAt(i));
                    out.write(bb.array());
                }
                bb = ByteBuffer.allocate(2);
                bb.putChar((char)13);
                out.write(bb.array());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
