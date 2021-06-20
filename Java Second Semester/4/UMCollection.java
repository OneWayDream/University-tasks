import java.util.*;

public class UMCollection<T> extends AbstractCollection<T> {

    protected T[] data;
    protected int counter;
    protected class UMCollectionIterator<T> implements Iterator<T>{

        protected int cursor;

        public UMCollectionIterator(){
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            if (cursor<counter){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() throws NoSuchElementException {
            try{
                T item =(T) data[cursor];
                cursor++;
                return item;
            } catch (IndexOutOfBoundsException ex){
                throw new NoSuchElementException("No");
            }
        }
    }

    public UMCollection(){
        this.data = (T[]) new Object[0];
        this.counter = 0;
    }

    public UMCollection(Collection<? extends T> collection){
        int k = 0;
        this.counter = collection.size();
        this.data = (T[]) new Object[this.counter];
        for (T t : collection) {
            this.data[k] = t;
            k++;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new UMCollectionIterator();
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UMCollection<?> that = (UMCollection<?>) o;
        if (this.counter!=that.counter){
            return false;
        }
        return  that.containsAll(this);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
