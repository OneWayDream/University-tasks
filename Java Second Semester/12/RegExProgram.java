import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExProgram {
    public static void main(String[] args) {

        //Exercise 1.

        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        Pattern p1 = Pattern.compile("@(\\w)+");
        Matcher m1 = p1.matcher(text);
        Pattern p2 = Pattern.compile("\\.\\w+$");
        Matcher m2 = p2.matcher(text);
        boolean b = text.matches("[\\w\\-]+@([\\w\\-]+\\.)+([\\w\\-]+)$");
        if (b){
            while (m1.find()){
                System.out.print(text.substring(m1.start()+1,m1.end()));
            }
            System.out.print(" and ");
            while (m2.find()){
                System.out.println(text.substring(m2.start()+1,m2.end()));
            }
        } else {
            System.out.println("NoT mail.");
        }

        //Exercise 2.

        StringBuilder str = new StringBuilder(sc.nextLine());
        Pattern pattern = Pattern.compile("@[\\w\\-]+\\.?");
        Matcher matcher = pattern.matcher(str.toString());
        while (matcher.find()){
            str.replace(matcher.start()+1,matcher.end(),"");
            matcher = pattern.matcher(str.toString());
        }
        System.out.println(str.toString());
    }
}
