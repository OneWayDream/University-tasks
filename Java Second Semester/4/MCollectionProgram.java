import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MCollectionProgram {
    public static void main(String[] args) {

        ArrayList<String> ll= new ArrayList<>();
        ll.add("1");
        ll.add("2");
        ll.add("3");
        MCollection<String> mcc = new MCollection<>(ll);

        System.out.println(mcc.size());
        Iterator<String> iter= mcc.iterator();
        System.out.println(iter.hasNext());
        iter.remove();
        System.out.println(iter.next());
        System.out.println(iter.next());
        System.out.println(iter.hasNext());
        System.out.println("");

        mcc.add("3");
        mcc.add("2");
        mcc.add("1");
        iter = mcc.iterator();
        System.out.println(iter.next());
        System.out.println(iter.next());
        System.out.println(iter.hasNext());
        System.out.println(iter.next());
        System.out.println(iter.hasNext());
        System.out.println(iter.next());
        System.out.println(iter.next());
        System.out.println(iter.hasNext());
        System.out.println("");

        ArrayList<String> l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        MCollection<String> mc = new MCollection<String>(l);
        System.out.println(mc.remove("2"));
        Iterator<String> iterr = mc.iterator();
        System.out.println(iterr.next());
        System.out.println(iterr.next());
        System.out.println(iterr.hasNext());
        System.out.println(mc.equals(mcc));
        l.remove("2");
        System.out.println(mc.equals(new MCollection<>(l)));
    }
}
