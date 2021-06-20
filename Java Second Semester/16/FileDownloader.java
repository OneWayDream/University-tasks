
import sun.dc.path.PathException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;


public class FileDownloader {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        URI uri;
        try {
            uri = new URI(str);

            // При попытке ввести ссылку идея перебрасывает по введённой ссылке, для теста можно портить ссылку,
                                                                                //добавляя в начало какой-либо символ.
            //uri = new URI(str.substring(1,str.length()));

            InputStream in = uri.toURL().openConnection().getInputStream();

            String type = (uri.toURL().openConnection().getContentType());


            String s = type.split("/")[1];

            type = s.split(";")[0];

            System.out.println("Введите директорию, в которую хотите сохранить файл, а также имя (в разных строках).");
            Path path = Paths.get(sc.nextLine());
            if (!Files.isDirectory(path)){
                throw new PathException("Введённый путь не является директорией.");
            }
            ArrayList<String> names = new ArrayList<>();
            Files.walk(path,1).forEach(x->names.add(x.getFileName().toString()));
            names.remove(0);
            String name = sc.nextLine();
            StringBuilder newName = new StringBuilder("");
            if (names.contains(name + "." + type)){
                int i = 1;
                newName = new StringBuilder("installation(" + i + ")") ;
                while (names.contains(newName.toString() + "." + type)) {
                    newName.replace(13,newName.length()-1,"" + i);
                    i++;
                }
                System.out.println("Данный файл уже есть в текущей директории. Будет использовано имя " + newName);
            }
            if (!newName.toString().equals("")){
                name = newName.toString();
            }
            File file = new File(path.toString() + "\\" + name +"." +  type);
            Files.copy(in, file.toPath());
        }catch (URISyntaxException e) {
            System.out.println("Неверный URI формат.");
        } catch (MalformedURLException ex) {
            System.out.println("Невозможно получить URL адрес по введенному URI.");
        } catch (AccessDeniedException ex){
            System.out.println("Отказано в доступе");
        } catch (IOException io){
            System.out.println("Ошибка ввода/вывода.");
        } catch (InvalidPathException ex){
            System.out.println("Введённая строка не может быть преобразована в путь");
        } catch (PathException ex){
            System.out.println(ex.getMessage());
        } catch (SecurityException ex){
            System.out.println("Невозможно создать новый файл в указанной директории.");
        }
    }


}
