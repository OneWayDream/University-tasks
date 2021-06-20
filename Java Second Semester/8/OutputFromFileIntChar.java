import java.io.*;

public class OutputFromFileIntChar {
    public static void main(String[] args) {
        int a = 0;
        int number;
        char symbol;
        try (FileInputStream in = new FileInputStream("C:\\Users\\v\\IdeaProjects\\Hw4\\TestFile.txt")) {
            a = (in.read()<<24)|a;
            a = (in.read()<<16)|a;
            a = (in.read()<<8)|a;
            a = (in.read()<<0)|a;
            number = a;
            a = 0;
            a = (in.read()<<8)|a;
            a = (in.read()<<0)|a;
            symbol = (char) a;
            System.out.println(number);
            System.out.println(symbol);
        }
        catch (FileNotFoundException e) {
            System.out.println("NO!");;
        } catch (IOException e) {
            System.out.println("No!");
        }
    }
}
