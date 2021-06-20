public class Queue<T> {

    private T[] array;
    private int end;
    private int first;

    public Queue(T[] array) throws IndexOutOfBoundsException{
        this.array = (T[]) new Object[1000];
        if (array.length<=1000){
            for (int i = 0;i<array.length;i++){
                this.array[i]=array[i];
            }
        } else {
            throw new IndexOutOfBoundsException("");
        }
        this.end=array.length;
        this.first = 0;
    }

    public Queue(){
        this.array = (T[]) new Object[1000];
        this.end = 0;
        this.first = 0;
    }

    public void add (T ex) {
        if (end==1000){
            T[] b = (T[]) new Object[1000];
            int k = 0;
            for (int i = first;i<1000;i++){
                    b[k] = (T) array[i];
                    k++;
            }
            end = k;
            first = 0;
            array=b;
        }
        array[end]= ex;
        end++;
    }

    public T pop() throws IndexOutOfBoundsException {
        if (end>first){
            T ex = (T) array[first];
            array[first] = null;
            first++;
            return ex;
        } else {
            throw new IndexOutOfBoundsException("");
        }
    }

    public T peek(){
        if (end>first){
            return (T) array[first];
        } else {
            throw new IndexOutOfBoundsException("");
        }
    }

    public int getSize(){
        return (end-first);
    }
}
