import java.util.Scanner;

public class BookSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("How many books do you want to sort:?");
        int n;
        n = sc.nextInt();
        while (n<0){
            System.out.println("Wrong number of books.");
            n = sc.nextInt();
        }
        sc.nextLine();
        Book[] books = new Book[n];
        String author;
        String name;
        for (int i = 0; i < n;i++){
            System.out.println("Enter " + (i+1) + " book.");
            author = sc.nextLine();
            name = sc.nextLine();
            books[i] = new Book(author,name);
        }
        booksSort(books);
        for (int i = 0;i<n;i++){
            System.out.println("" +
                    "" + (i+1) + ") " + books[i].toString());
        }
    }


    public static void swap(Book[] books, int i, int j){
        Book b = books[i];
        books[i] = books[j];
        books[j] = b;
    }

    public static void booksSort(Book[] books){
        boolean needToSort;
        for (int i = books.length-1; i>=0;i--){
            needToSort = false;
            for (int j = 0; j<i;j++){
                if (books[j].compareTo(books[j+1])<0){
                    swap(books,j,j+1);
                    needToSort = true;
                }
            }
            if (!needToSort){
                return;
            }
        }
        System.out.println("Sorting was successful.");
    }
}
