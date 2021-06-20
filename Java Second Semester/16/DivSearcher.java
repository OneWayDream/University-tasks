import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DivSearcher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        Pattern check = Pattern.compile("<div[\\w:=\\-\"\\s]*>");
        Matcher checker;
        URI uri;
        int counter = 0;
        try{
            uri = new URI(str);

            // При попытке ввести ссылку идея перебрасывает по введённой ссылке, для теста можно портить ссылку,
                                                                                //добавляя в начало какой-либо символ.
            //uri = new URI(str.substring(1,str.length()));


            BufferedReader in = new BufferedReader(new InputStreamReader(uri.toURL().openConnection().getInputStream()));
            String line;
            while((line = in.readLine())!=null){
                checker = check.matcher(line);
                while(checker.find()){
                    checker.start();
                    checker.end();
                    counter++;
                }
             }
        } catch (URISyntaxException e) {
            System.out.println("Неверный URI формат.");
        } catch (MalformedURLException ex){
            System.out.println("Невозможно получить URL адрес по введенному URI.");
        } catch (IOException io){
            System.out.println("Ошибка ввода/вывода.");
        } catch (IllegalArgumentException ex){
            System.out.println("URI не является абсолютным");
        }
        System.out.println(counter);
    }
}
