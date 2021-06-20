import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public class MNavigableSetProgram {
    public static void main(String[] args) {
        System.out.println("Well, let's start a great test!");

        System.out.println("");

        MNavigableSet<Integer> t1 = new MNavigableSet<>(new IntegerComparator<>());
        System.out.println(t1.add(40));
        System.out.println(t1.add(100));
        System.out.println(t1.add(1));
        System.out.println(t1.add(60));
        System.out.println(t1.add(60));

        System.out.println("");

        System.out.println(t1.contains(1));
        System.out.println(t1.contains(40));
        System.out.println(t1.contains(60));
        System.out.println(t1.contains(100));
        System.out.println(t1.contains(9));

        System.out.println("");

        System.out.println(t1.first());
        System.out.println(t1.last());

        System.out.println("");

        Iterator<Integer> iter1 = t1.descendingIterator();
        for (int i = 0;i<4;i++){
            System.out.println(iter1.next());
        }
        System.out.println(iter1.hasNext());

        System.out.println("");

        iter1 = t1.iterator();
        for (int i = 0;i<4;i++){
            System.out.println(iter1.next());
        }
        System.out.println(iter1.hasNext());

        System.out.println("");

        MNavigableSet<Integer> reservet1 = t1.descendingSet();
        System.out.println(reservet1.first());
        System.out.println(reservet1.last());

        System.out.println("");

        iter1 = reservet1.descendingIterator();
        for (int i = 0;i<4;i++){
            System.out.println(iter1.next());
        }
        System.out.println(iter1.hasNext());

        System.out.println("");

        iter1 = reservet1.iterator();
        for (int i = 0;i<4;i++){
            System.out.println(iter1.next());
        }
        System.out.println(iter1.hasNext());

        System.out.println("");

        System.out.println(t1.higher(101));
        System.out.println(t1.higher(100));
        System.out.println(t1.higher(99));
        System.out.println(t1.higher(50));

        System.out.println("");

        System.out.println(t1.lower(0));
        System.out.println(t1.lower(1));
        System.out.println(t1.lower(2));
        System.out.println(t1.lower(50));

        System.out.println("");

        System.out.println(t1.size());
        System.out.println(t1.poolFirst());
        System.out.println(t1.poolLast());
        System.out.println(t1.size());
        System.out.println(t1.first());
        System.out.println(t1.last());
        System.out.println(t1.remove(100));
        System.out.println(t1.remove(60));
        System.out.println(t1.size());
        System.out.println(t1.first());
        System.out.println(t1.last());

        System.out.println("");

        ArrayList<Integer> l = new ArrayList<>();
        for (int i = 10;i>0;i--){
            l.add(i);
        }
        t1 = new MNavigableSet<>(l,new IntegerComparator<>());
        System.out.println(t1.first());
        System.out.println(t1.last());

        System.out.println("");

        System.out.println(t1.ceiling(11));
        System.out.println(t1.ceiling(10));
        System.out.println(t1.ceiling(5));
        System.out.println(t1.ceiling(-10));
        System.out.println(t1.floor(0));
        System.out.println(t1.floor(1));
        System.out.println(t1.floor(5));
        System.out.println(t1.floor(10000));

        System.out.println("");

        SortedSet<Integer> subset = t1.subSet(-5, 7);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
        subset = t1.subSet(7,100);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
        subset = t1.subSet(5,7);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");

        System.out.println("");

        SortedSet<Integer> headset = t1.headSet(7);
        for (int i = 0;i<11;i++){
            System.out.print(headset.contains(i) + " ");
        }
        System.out.println("");
        headset = t1.headSet(10);
        for (int i = 0;i<11;i++){
            System.out.print(headset.contains(i) + " ");
        }
        System.out.println("");
        headset = t1.headSet(1);
        for (int i = 0;i<11;i++){
            System.out.print(headset.contains(i) + " ");
        }
        System.out.println("");

        System.out.println("");

        SortedSet<Integer> tailset = t1.tailSet(10);
        for (int i = 0;i<11;i++){
            System.out.print(tailset.contains(i) + " ");
        }
        System.out.println("");
        tailset = t1.tailSet(1);
        for (int i = 0;i<11;i++){
            System.out.print(tailset.contains(i) + " ");
        }
        System.out.println("");
        tailset = t1.tailSet(5);
        for (int i = 0;i<11;i++){
            System.out.print(tailset.contains(i) + " ");
        }
        System.out.println("");

        System.out.println("");

        tailset = t1.tailSet(5,false);
        for (int i = 0;i<11;i++){
            System.out.print(tailset.contains(i) + " ");
        }
        System.out.println("");
        headset = t1.headSet(5,true);
        for (int i = 0;i<11;i++){
            System.out.print(headset.contains(i) + " ");
        }
        System.out.println("");

        System.out.println("");

        subset = t1.subSet(5,true,7,true);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
        subset = t1.subSet(5,true,7,false);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
        subset = t1.subSet(5,false,7,true);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
        subset = t1.subSet(5,false,7,false);
        for (int i = 0;i<11;i++){
            System.out.print(subset.contains(i) + " ");
        }
        System.out.println("");
    }
}
