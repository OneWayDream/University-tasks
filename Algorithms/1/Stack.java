public class Stack<T> {

    private T[] array;
    private int counter;

    public Stack(T[] array) throws IndexOutOfBoundsException{
        this.array = (T[]) new Object[1000];
        if (array.length<=1000){
            for (int i = 0;i<array.length;i++){
                this.array[i]=array[i];
            }
        } else {
            throw new IndexOutOfBoundsException("");
        }
        this.counter=array.length;
    }

    public Stack(){
        this.array = (T[]) new Object[1000];
        this.counter = 0;
    }

    public void add (T ex) throws IndexOutOfBoundsException{
        if (counter<1000){
            array[counter] = ex;
            counter++;
        } else {
            throw new IndexOutOfBoundsException("");
        }
    }

    public T pop() throws IndexOutOfBoundsException{
        if (counter>0){
            T ex = (T) array[counter-1];
            array[counter-1] = null;
            counter--;
            return ex;
        } else {
            throw new IndexOutOfBoundsException("");
        }
    }

    public T peek(){
        if (counter>0){
            return (T) array[counter-1];
        } else {
            throw new IndexOutOfBoundsException("");
        }
    }

    public int size(){
        return counter;
    }
}
