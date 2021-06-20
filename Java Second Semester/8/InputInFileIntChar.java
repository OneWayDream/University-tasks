import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class InputInFileIntChar {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your number.");
        int number = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter your symbol.");
        String str = sc.nextLine();
        char symbol = str.charAt(0);
        try (FileOutputStream out = new FileOutputStream("C:\\Users\\v\\IdeaProjects\\Hw4\\TestFile.txt")) {
            out.write(number>>24);
            out.write(number>>16);
            out.write(number>>8);
            out.write(number>>0);
            out.flush();
            out.write(((int) symbol)>>8);
            out.write(((int) symbol)>>0);
            out.flush();
        }
        catch (FileNotFoundException e) {
            System.out.println("NO!");;
        } catch (IOException e) {
            System.out.println("No!");
        }
    }
}
