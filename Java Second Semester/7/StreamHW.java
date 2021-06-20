import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class StreamHW {
    public static void main(String[] args) {

        //7.4. Можно использовать для сортировки книг. Пример 1 - сортировка книг по названию.

        MCollection<Book> bmc = new MCollection<>();
        Comparator<Book> bookNameComparator = (Book b1, Book b2) -> b1.getName().compareTo(b2.getName());
        bmc.add(new Book("Art"));
        bmc.add(new Book("Act"));
        bmc.add(new Book("Awt"));
        Book ex = new Book("Ast");
        System.out.println("Titles of books located higher than this:"
                + bmc.stream()
                     .filter((b1)->b1.getName().compareTo(ex.getName())<0)
                     .collect(Collectors.toList()));

        // Пример 2 - можно подать в метод сортировки, принимающий компаратор. P.s. Класс Book импортировал с прошлых заданий,
        // тут его не будет, но он наследовал Comparable. 
            

        ArrayList<Book> arl = new ArrayList<>();
        arl.add(new Book("John Ronald Reuel Tolkien","The Hobbit, or There and Back Again"));
        arl.add(new Book("Leo Tolstoy","War and Peace."));
        arl.sort((b1,b2)->b1.compareTo(b2));
        System.out.println(arl.stream().collect(Collectors.toList()));

        //7.5.

        ArrayList<Integer> l1 = new ArrayList<>();
        for (int i = 1;i<6;i++){
            l1.add(i);
        }
        ArrayList<Integer> l2 = new ArrayList<>();
        for (int i = 1;i<11;i++){
            l2.add(i);
        }
        Integer max = l1.stream().max(Integer::compareTo).get();
        System.out.println("Array elements larger than the maximum of the second array: "
                + l2.stream()
                    .filter((b1)->b1>max)
                    .collect(Collectors.toList()));

        //7.6

        Set<String> set = new Set<>();
        set.add("aabfg");
        set.add("urgfyi");
        set.add("bagybei");
        set.add("opeleki");
        String str = "AaEeYyUuIiOo";
        System.out.println("The number of strings satisfying the condition: "
                + Arrays.stream(set.getArray())
                        .filter(s->s.chars()
                                    .filter(ch->str.indexOf(ch)!=-1).count()>3)
                        .count());

        //7.7

        MMap<String,Integer> map = new MMap<>();
        map.put("Never ",1);
        map.put("gonna ",2);
        map.put("give ",3);
        map.put("you ",4);
        map.put("up.",5);
        System.out.println("String of keys: " + Arrays.stream(map.getKeys()).reduce((s1,s2)->s1 + s2).orElse(""));

        //7.8

        MCollection<String> mcol =  new MCollection<>();
        mcol.add("a");
        mcol.add("bbbbb");
        mcol.add("cccccc");
        mcol.add("qajelsornfpazdr");
        System.out.println("The sum of the lengths of this collection lines that are longer than 5 characters: "
                + mcol.stream()
                      .map(String::length)
                      .filter(s->s>5)
                      .reduce(Integer::sum)
                      .orElse(0));
    }
}
