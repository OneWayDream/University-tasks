import java.util.Arrays;
import java.util.stream.Stream;

public class Set<T> {

    protected T[] data;
    protected int counter;

    public Set(T[] dat){
        this.data = (T[]) new Object[dat.length + 100];
        int k = 0;
        for (int i = 0; i<dat.length;i++){
            if (!this.contains(dat[i]));
            data[k] = dat[i];
            k++;
        }
        this.counter = k;
    }

    public Set(){
        this.data = (T[]) new Object[100];
        this.counter = 0;
    }

    public Set(Set<? extends T> set, int extraSize){
        this.data = (T[]) new Object[set.getArray().length + extraSize];
        for (int i = 0;i<set.getArray().length;i++){
            data[i] = set.getArray()[i];
        }
        this.counter = set.size();
    }

    public Set(int size){
        this.data = (T[]) new Object[size];
        this.counter = 0;
    }

    public boolean add(T item){
        if ((!(this.contains(item)))&&(counter<=this.data.length)){
            data[counter] = item;
            counter++;
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(T item){
        for (int i = 0; i<counter;i++){
            if (data[i].equals(item)){
                return true;
            }
        }
        return false;
    }

    public int size(){
        return counter;
    }

    public boolean remove(T item){
        if (this.contains(item)){
            int k = 0;
            for (int i = 0;i<counter;i++){
                if (data[i].equals(item)){
                    k = i;
                    break;
                }
            }
            for (int i = k+1;i<counter;i++){
                data[i-1] = data[i];
            }
            counter--;
            data[counter] = null;
            return true;
        } else {
            return false;
        }
    }

    public Set merge(Set<T> set){
        Set <T> ans = new Set<T>((this), (counter + set.size())) ;
        for (int i = 0;i<set.size();i++){
            if (!(ans.contains((T) data[i]))){
                ans.add(set.getArray()[i]);
            }
        }
        return ans;
    }

    public Stream<T> stream(){
        return Arrays.stream(this.data);
    }

    public T[] getArray(){
        T[] ans = (T[]) new Object[counter];
        for (int i = 0;i<counter;i++){
            ans[i] = this.data[i];
        }
        return ans;
    }
}
