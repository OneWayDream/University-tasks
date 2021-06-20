import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MCollection<T> extends AbstractCollection<T> {

    protected T[] data;
    protected int counter;
    protected class MCollectionIterator<T> implements Iterator<T> {

        protected int cursor;

        public MCollectionIterator() {
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            if (cursor < counter) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() throws NoSuchElementException {
            try {
                T item = (T) data[cursor];
                cursor++;
                return item;
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException("No");
            }
        }

        public T peek(){
            try {
                return (T) data[cursor];
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException("No");
            }
        }

        public void remove() {
            for (int i = cursor;i<data.length - 1;i++){
               data[i] = data[i + 1];
            }
            data[data.length-1] = null;
            counter--;
        }
    }

    public MCollection(){
        this.data = (T[]) new Object[0];
        this.counter = 0;
    }

    public MCollection(Collection<? extends T> collection){
        int k = 0;
        this.counter = collection.size();
        this.data = (T[]) new Object[this.counter];
        Iterator<? extends T> iter = collection.iterator();
        while (iter.hasNext()){
            this.data[k] = iter.next();
            k++;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MCollectionIterator<T>();
    }

    @Override
    public int size() {
        return counter;
    }

    public boolean add(T item) {
        if (counter >= data.length) {
            long b = data.length;
            if (b != 0) {
                if ((b * 2) <= 2147483646) {
                    b = b*2;
                } else {
                    if (b<2147483647){
                        b = 2147483647;
                    } else {
                        return false;
                    }
                }
            } else {
                b = 100;
            }
            T[] array = (T[]) new Object[Math.round(b)];
            for (int i = 0; i < counter; i++) {
                array[i] = data[i];
            }
            this.data = array;
        }
        data[counter] = item;
        counter++;
        return true;
    }

    public boolean remove(Object item){
        MCollectionIterator<T> iter = new MCollectionIterator<>();
        while (iter.hasNext()){
            if (iter.peek().equals(item)){
                iter.remove();
                return true;
            }
            iter.next();
        }
        return false;
    }
    
     public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MCollection<?> that = (MCollection<?>) o;
        if (this.counter!=that.counter){
            return false;
        }
        return  that.containsAll(this);
    }
}
