import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class UnicodeReader {
    public static void main(String[] args) {
        System.out.println("Enter your path to the text file.");
        Scanner sc = new Scanner(System.in);
        Path path = Paths.get(sc.nextLine()).normalize();
        path.toAbsolutePath();

        while (!Files.exists(path)){
            System.out.println("This file is not exists");
            System.out.println("Enter your path to the text file.");
            path = Paths.get(sc.nextLine()).normalize();
            path.toAbsolutePath();
        }
        List<String> list;
        String timing;
        try{
            list = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String str:list){
                timing = new String(str.getBytes(StandardCharsets.UTF_8), Charset.forName("Cp866"));
                System.out.println(timing);
            }
        } catch (IOException io){
            System.out.println("Can't read this file.");
        }
    }
}
