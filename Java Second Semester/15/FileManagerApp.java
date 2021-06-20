import javafx.scene.shape.Path;
import sun.dc.path.PathException;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Scanner;

public class FileManagerApp {
    public static void main(String[] args) {
        FileManagerApp app = new FileManagerApp();
        listOfCommands = "\"cd\", \"dir\", \"help\", \"exit\"";
        app.init();
        app.start();
    }

    protected static Scanner sc = new Scanner(System.in);
    protected static FileManager fileManager;
    protected static String listOfCommands;

    public static void init(){
        System.out.println("Enter your absolute path.");
        String p;
        while (fileManager==null){
            p = sc.nextLine().trim();
            try {
                fileManager = new FileManager(p);
            } catch (PathException | InvalidPathException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("");
        }
    }

    public static void start(){
        boolean isWork = true;
        String str;
        String[] s;
        while (isWork){
            System.out.print(fileManager.getCurrentPath() + ">");
            str = sc.nextLine().trim();
            s = str.split("\\s",2);
            switch (s[0]){
                case "cd" :
                    if (s.length == 1){
                        System.out.println("Правильная форма команды имеет вид \"cd path\"");
                    } else {
                        try{
                            fileManager.cd(s[1]);
                        } catch (PathException | InvalidPathException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case "dir" :
                    try{
                        fileManager.dir();
                    } catch (IOException io){
                        System.out.println(io.getMessage());
                    }
                    break;
                case "help" :
                    System.out.println(listOfCommands);
                    break;
                case "exit" :
                    isWork = false;
                    break;
                default :
                    System.out.println("\"" + s[0] + "\" не является внутренней или внешней \n" +
                            "командой, исполняемой программой или пакетным файлом.");
                    break;
            }
            System.out.println("");
        }
    }
}
