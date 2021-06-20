import sun.dc.path.PathException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ThreadFileDownloader {
    public static void main(String[] args) {
        ThreadFileDownloader app = new ThreadFileDownloader();
        app.init();
        app.start();
    }

    protected Scanner sc = new Scanner(System.in);
    protected String commands;
    protected DownloadThread currentDownloadThread;

    public void init() {
        commands = "\"downLoadFile\", \"getCurrentProgress\", \"stopFileDownload\", \"help\", \"exit\"";
        currentDownloadThread = null;
    }

    public void start() {
        boolean isWork = true;
        String command;
        while (isWork) {
            System.out.println("Enter your command.");
            command = sc.nextLine();
            switch (command) {
                case "downloadFile":
                    if ((currentDownloadThread!=null)&&(currentDownloadThread.isAlive())){
                        System.out.println("The file is already downloading, wait for the download to complete.");
                    } else {
                        System.out.println("Enter your link to the file to be downloaded.");
                        try{
                            URI uri = new URI(sc.nextLine());
                            System.out.println("Введите директорию, в которую хотите сохранить файл, а также имя (в разных строках).");
                            Path path = Paths.get(sc.nextLine());
                            if (!Files.isDirectory(path)){
                                throw new PathException("Введённый путь не является директорией.");
                            }
                            String name = sc.nextLine();
                            currentDownloadThread = new DownloadThread(uri, path, name);
                            currentDownloadThread.start();
                        } catch (URISyntaxException e) {
                            System.out.println("Incorrect link.");
                        } catch (PathException ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case "getCurrentProgress":
                    if ((currentDownloadThread==null)||(!currentDownloadThread.isAlive())){
                        System.out.println("Now nothing is installed.");
                    } else {
                        System.out.print("Installed ");
                        System.out.printf("%.1f", currentDownloadThread.getCurrentProgress());
                        System.out.println("%");
                    }
                    break;
                case "stopFileDownload":
                    if ((currentDownloadThread==null)||(!currentDownloadThread.isAlive())){
                        System.out.println("Now nothing is installed.");
                    } else {
                        currentDownloadThread.interrupt();
                        currentDownloadThread = null;
                    }
                    break;
                case "help":
                    System.out.println(commands);
                    break;
                case "exit":
                    if ((currentDownloadThread!=null)&&(currentDownloadThread.isAlive())){
                        System.out.println("The file is already downloading, wait for the download to exit.");
                    } else {
                        isWork = false;
                    }
                    break;
                default:
                    System.out.println("Unknown command. Enter command\"help\" to get list of the commands.");
                    break;
            }
        }
    }
}

