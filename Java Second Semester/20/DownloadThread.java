import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.ArrayList;

public class DownloadThread extends Thread {

    protected URI uri;
    protected Path path;
    protected String fileName;
    protected long size;
    protected File file;

    public DownloadThread(URI uri, Path path, String fileName){
        super();
        this.uri = uri;
        this.path = path;
        this.fileName = fileName;
        this.file = null;
        this.size = 0;
    }

    @Override
    public void run() {
        try{
            URLConnection urlConnection = uri.toURL().openConnection();
            this.size = urlConnection.getContentLengthLong();
            InputStream in = urlConnection.getInputStream();
            String type = (uri.toURL().openConnection().getContentType());
            String s = type.split("/")[1];
            type = s.split(";")[0];
            ArrayList<String> names = new ArrayList<>();
            Files.walk(path,1).forEach(x->names.add(x.getFileName().toString()));
            names.remove(0);
            StringBuilder newName = new StringBuilder("");
            if (names.contains(fileName + "." + type)){
                int i = 1;
                newName = new StringBuilder("installation(" + i + ")") ;
                while (names.contains(newName.toString() + "." + type)) {
                    newName.replace(13,newName.length()-1,"" + i);
                    i++;
                }
                System.out.println("Данный файл уже есть в текущей директории. Будет использовано имя " + newName);
            }
            if (!newName.toString().equals("")){
                fileName = newName.toString();
            }
            this.file = new File(path.toString() + "\\" + fileName +"." +  type);
            Files.copy(in, this.file.toPath());
            System.out.println("Загрузка завершена.");
        } catch (MalformedURLException ex) {
            System.out.println("Невозможно получить URL адрес по введенному URI.");
        } catch (AccessDeniedException ex){
            System.out.println("Отказано в доступе");
        } catch (IOException io){
            System.out.println("Ошибка ввода/вывода.");
        } catch (InvalidPathException ex){
            System.out.println("Введённая строка не может быть преобразована в путь");
        } catch (SecurityException ex){
            System.out.println("Невозможно создать новый файл в указанной директории.");
        }
    }



    public double getCurrentProgress(){
        if ((this.file!=null)&&(this.size!=0)){
            return (double)(this.file.length()*100/this.size);
        } else {
            return 0;
        }
    }
}
