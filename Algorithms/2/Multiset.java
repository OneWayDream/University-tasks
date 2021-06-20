public class Multiset<T> {

    protected T[] data;
    protected int[] counters;
    protected int counter;

    public Multiset(T[] dat){
        this.data = (T[]) new Object[dat.length + 100];
        this.counters = new int[dat.length + 100];
        this.counter = 0;
        for (int i = 0; i<dat.length;i++){
            this.addM(dat[i]);
        }
    }

    public Multiset(){
        this.data = (T[]) new Object[100];
        this.counters = new int[100];
        this.counter = 0;
    }

    public Multiset(int size){
        this.data = (T[]) new Object[size];
        this.counters = new int[size];
        this.counter = 0;
    }

    public boolean addM(T item){
       if (counter<data.length){
           if (this.containsM(item)){
               counters[this.indexOfM(item)]++;
               return true;
           } else {
               data[counter] = item;
               counters[counter] = 1;
               return true;
           }
       } else {
           return false;
       }
    }

    public boolean containsM(T item){
        for (int i = 0; i<counter;i++){
            if (data[i].equals(item)){
                return true;
            }
        }
        return false;
    }

    public int sizeM(){
        return counter;
    }

    public boolean removeM(T item){
        int k = this.indexOfM(item);
        if (k!=-1){
            if (counters[k]>1){
                counters[k]--;
                return true;
            } else {
                for (int i = k+1;i<counter;i++){
                    data[i-1] = data[i];
                }
                data[counter-1] = null;
                for (int i = k+1;i<counter;i++){
                    counters[i-1] = counters[i];
                }
                counters[counter-1] = 0;
                counter--;
                return true;
            }
        } else {
            return false;
        }
    }

    public int indexOfM(T item){
        for (int i = 0;i<counter;i++){
            if (data[i].equals(item)){
                return i;
            }
        }
        return -1;
    }

    public T[] getArray(){
        return (T[]) data;
    }
}
