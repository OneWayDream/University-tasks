import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UMCollectionProgram {
    public static void main(String[] args) {

        ArrayList<String> l= new ArrayList<>();
        l.add("1");
        l.add("2");
        l.add("3");
        UMCollection<String> umc = new UMCollection<>(l);

        umc.size();
        Iterator<String> iter= umc.iterator();
        System.out.println(iter.hasNext());
        System.out.println(iter.next());
        System.out.println(iter.next());
        System.out.println(iter.next());
        System.out.println(iter.hasNext());

        System.out.println("");

        System.out.println(umc.equals((1)));
        UMCollection<String> um = new UMCollection<>(l);
        System.out.println(umc.equals(um));
    }
}
