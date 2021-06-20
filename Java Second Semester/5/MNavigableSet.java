import java.util.*;

/**
 * The class implements SortedSet and extends navigation methods. NavigableSet cannot have duplicates, like its parent
 * AbstractSet. Have fun!
 * @param <T> - type of stored data.
 */
public class MNavigableSet<T> extends AbstractSet<T> implements SortedSet<T> {

    protected ArrayList<T> data;
    protected Comparator<T> comparator;
    protected boolean isReverse;

    protected class MNavigableSetIterator<T> implements Iterator<T> {
        protected int cursor;
        protected int calc;

        public MNavigableSetIterator(int calc){
            this.calc = calc;
            if (calc>0){
                this.cursor = 0;
            } else {
                this.cursor = data.size()-1;
            }
        }

        @Override
        public boolean hasNext() {
            if ((cursor < data.size())&&(cursor>=0)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() throws NoSuchElementException {
            try {
                T item = (T) data.get(cursor);
                cursor = cursor + calc;
                return item;
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException("No");
            }
        }

        public T peek(){
            try {
                return (T) data.get(cursor);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException("No");
            }
        }
    }


    public MNavigableSet(Comparator<T> comparator){
        this.comparator = comparator;
        this.data = new ArrayList<>();
        this.isReverse = false;
    }

    public MNavigableSet(ArrayList<T> data, Comparator<T> comparator){
        this.comparator = comparator;
        this.isReverse = false;
        this.data = new ArrayList<>();
        for (int i = 0;i<data.size();i++){
            this.add(data.get(i));
        }
    }

    private MNavigableSet(Comparator<T> comparator, boolean isReverse){
        this.data = new ArrayList<T>();
        this.isReverse = isReverse;
        this.comparator = comparator;
    }

    /**
     * Adds an element, if unique, to the current NavigableSet.
     */
    @Override
    public boolean add(T item) {
        int number = 0;
        MNavigableSetIterator<T> iter= this.descendingIterator();
        while (iter.hasNext()){
            if (comparator.compare(iter.peek(),item)==0){
                return false;
            }
            if (comparator.compare(iter.peek(),item)<0){
                break;
            }
            number++;
            iter.next();
        }
        data.add(number,item);
        return true;
    }

    /**
     * Adds each element (if unique) from the resulting array in the current NavigableSet.
     */
    public boolean addAll(Collection<? extends T> list){
        boolean ans = false;
        boolean b = false;
        Iterator<? extends T> iter = list.iterator();
        while(iter.hasNext()){
            b = this.add(iter.next());
            if (b){
                ans = b;
            }
        }
        return ans;
    }

    /**
     * Returns the least element in this set greater than or equal to the given element, or null if there is no such element.
     */
    public T ceiling(T item){
        if (this.comparator.compare(item,this.first())>0){
            return null;
        } else {
            for (int i = 0;i<this.size();i++){
                if (this.comparator.compare(item,data.get(i))>0){
                    return data.get(i-1);
                }
            }
            return this.last();
        }
    }

    /**
     * Removes all items from the current NavigableSet.
     */
    public void clear(){
        this.data = new ArrayList<T>();
    }

    /**
     * Returns the comparator used to compare items in the current NavigableSet.
     */
    @Override
    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    /**
     * Returns the result of checking whether the item is current NavigableSet.
     */
    @Override
    public boolean contains(Object o) {
        Iterator<T> iter= this.iterator();
        while (iter.hasNext()){
            if (iter.next().equals(o)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the result of checking whether the array of items is current NavigableSet.
     */
    public boolean containsAll(Collection<?> list){
        Iterator<?> iter = list.iterator();
        while(iter.hasNext()){
            if (!this.contains(iter.next())){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an iterator over the elements in this set, in descending order.
     */
    public MNavigableSetIterator<T> descendingIterator(){
        int k;
        if (isReverse){
            k = -1;
        } else {
            k = 1;
        }
        return new MNavigableSetIterator<T>(k);
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     */
    public MNavigableSet<T> descendingSet(){
        MNavigableSet<T> ans = new MNavigableSet<>(this.comparator,(!isReverse));
        for (int i = 0;i<this.size();i++){
            ans.data.add(null);
        }
        for (int i = 0;i<this.size();i++){
            ans.data.set(i,this.data.get(this.size()-1-i));
        }
        return ans;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     */
    @Override
    public T first() throws NoSuchElementException {
        if (this.size()!=0){
            if (!this.isReverse){
                return data.get(this.size()-1);
            } else {
                return data.get(0);
            }
        } else {
            throw new NoSuchElementException("This set is empty.");
        }
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element, or null if there is no such element.
     */
    public T floor(T item){
        if (this.comparator.compare(item,this.last())<0){
            return null;
        } else {
            for (int i = this.size() - 1; i >= 0; i--) {
                if (this.comparator.compare(item, data.get(i)) < 0) {
                    return data.get(i+1);
                }
            }
            return this.first();
        }
    }

    /**
     * Return hashcode of the current NavigableSet.
     */
    public int hashCode(){
        return super.hashCode();
    }

    /**
     * Returns a view of the portion of this set whose elements are strictly less than toElement.
     */
    @Override
    public SortedSet<T> headSet(T toElement) {
        MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
        for (int i = this.size()-1;i>=0;i--){
            if (comparator.compare(this.data.get(i),toElement)<0){
                ans.add(this.data.get(i));
            }
            if (comparator.compare(this.data.get(i),toElement)>=0){
                return ans;
            }
        }
        return ans;
    }

    /**
     * Returns a view of the portion of this set whose elements are less than (or equal to, if inclusive is true) toElement.
     */
    public MNavigableSet<T> headSet(T toElement,boolean inclusive){
        if (!inclusive){
            return (MNavigableSet<T>) this.headSet(toElement);
        } else {
            MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
            for (int i = this.size()-1;i>=0;i--){
                if (comparator.compare(this.data.get(i),toElement)<=0){
                    ans.add(this.data.get(i));
                }
                if (comparator.compare(this.data.get(i),toElement)>0){
                    return ans;
                }
            }
            return ans;
        }
    }

    /**
     * Returns the least element in this set strictly greater than the given element, or null if there is no such element.
     */
    public T higher(T item){
        if (this.comparator.compare(item,this.first())>=0){
            return null;
        }
        for (int i = 0;i<this.size();i++){
            if (this.comparator.compare(item,this.data.get(i))>=0){
                return this.data.get(i-1);
            }
        }
        return this.last();
    }

    /**
     * Returns an iterator over the elements in this set, in ascending order.
     */
    @Override
    public MNavigableSetIterator<T> iterator() {
        int k;
        if (isReverse){
            k = 1;
        } else {
            k = -1;
        }
        return new MNavigableSetIterator<T>(k);
    }

    /**
     * Returns true if the current Navigable set is empty.
     */
    public boolean isEmpty(){
        if (this.size()!=0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns the last (highest) element currently in this set.
     */
    @Override
    public T last() throws NoSuchElementException {
        if (this.size()!=0){
            if (!this.isReverse){
                return data.get(0);
            } else{
                return data.get(this.size()-1);
            }
        } else {
            throw new NoSuchElementException("This set is empty.");
        }
    }

    /**
     * Returns the greatest element in this set strictly less than the given element, or null if there is no such element.
     */
    public T lower(T item){
        if (this.comparator.compare(item,this.last())<=0){
            return null;
        }
        for (int i = this.size()-1;i>=0;i--){
            if (this.comparator.compare(item,this.data.get(i))<=0){
                return this.data.get(i+1);
            }
        }
        return this.first();
    }

    /**
     * Retrieves and removes the first (lowest) element, or returns null if this set is empty.
     */
    public T poolFirst() {
        T ans= this.first();
        this.remove(this.first());
        return ans;
    }

    /**
     * Retrieves and removes the last (highest) element, or returns null if this set is empty.
     */
    public T poolLast(){
        T ans= this.last();
        this.remove(this.last());
        return ans;
    }

    /**
     * Removes element from current NavigableSet.
     */
    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    /**
     * Removes each element of input array from current NavigableSet.
     */
    public boolean removeAll(Collection<?> list){
        boolean ans = false;
        boolean b = false;
        Iterator<?> iter = list.iterator();
        while(iter.hasNext()){
            b = this.remove(iter.next());
            if (b){
                ans = b;
            }
        }
        return ans;
    }

    /**
     * Removes from this set all of its elements that are contained in the specified collection.
     */
    public boolean retainAll(Collection<?> list){
        boolean ans = false;
        for (int i = 0;i<this.size();i++){
            if (!list.contains(this.data.get(i))){
                this.remove(this.data.get(i));
                ans = true;
            }
        }
        return ans;
    }

    /**
     * Returns the number of items in the current NavigableSet.
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * Returns a view of the portion of this set whose elements range from fromElement, inclusive, to toElement, exclusive.
     */
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
        for (int i = 0;i<this.size();i++){
            if ((comparator.compare(this.data.get(i),toElement)<0)&&(comparator.compare(this.data.get(i),fromElement)>=0)){
                ans.add(this.data.get(i));
            }
            if (!this.isReverse){
                if (comparator.compare(this.data.get(i),fromElement)<0){
                    return ans;
                }
            } else {
                if (comparator.compare(this.data.get(i),toElement)>=0){
                    return ans;
                }
            }
        }
        return ans;
    }

    /**
     * Returns a view of the portion of this set whose elements range from fromElement to toElement.
     */
    public MNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive){
        if (fromInclusive){
            if (toInclusive){
                MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
                for (int i = 0;i<this.size();i++){
                    if ((comparator.compare(this.data.get(i),toElement)<=0)&&(comparator.compare(this.data.get(i),fromElement)>=0)){
                        ans.add(this.data.get(i));
                    }
                    if (!this.isReverse){
                        if (comparator.compare(this.data.get(i),fromElement)<0){
                            return ans;
                        }
                    } else {
                        if (comparator.compare(this.data.get(i),toElement)>0){
                            return ans;
                        }
                    }
                }
                return ans;
            } else {
                return (MNavigableSet<T>) this.subSet(fromElement,toElement);
            }
        } else {
            if(toInclusive){
                MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
                for (int i = 0;i<this.size();i++){
                    if ((comparator.compare(this.data.get(i),toElement)<=0)&&(comparator.compare(this.data.get(i),fromElement)>0)){
                        ans.add(this.data.get(i));
                    }
                    if (!this.isReverse){
                        if (comparator.compare(this.data.get(i),fromElement)<=0){
                            return ans;
                        }
                    } else {
                        if (comparator.compare(this.data.get(i),toElement)>0){
                            return ans;
                        }
                    }
                }
                return ans;
            } else{
                MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
                for (int i = 0;i<this.size();i++){
                    if ((comparator.compare(this.data.get(i),toElement)<0)&&(comparator.compare(this.data.get(i),fromElement)>0)){
                        ans.add(this.data.get(i));
                    }
                    if (!this.isReverse){
                        if (comparator.compare(this.data.get(i),fromElement)<=0){
                            return ans;
                        }
                    } else {
                        if (comparator.compare(this.data.get(i),toElement)>=0){
                            return ans;
                        }
                    }
                }
                return ans;
            }
        }
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
     */
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
        for (int i = 0;i<this.size();i++){
            if (comparator.compare(this.data.get(i),fromElement)>=0){
                ans.add(this.data.get(i));
            }
            if (comparator.compare(this.data.get(i),fromElement)<0){
                return ans;
            }
        }
        return ans;
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than (or equal to, if inclusive is true) fromElement.
     */
    public MNavigableSet<T> tailSet (T fromElement, boolean inclusive){
        if (inclusive){
            return (MNavigableSet<T>) this.tailSet(fromElement);
        } else {
            MNavigableSet<T> ans = new MNavigableSet<T>(this.comparator);
            for (int i = 0;i<this.size();i++){
                if (comparator.compare(this.data.get(i),fromElement)>0){
                    ans.add(this.data.get(i));
                }
                if (comparator.compare(this.data.get(i),fromElement)<=0){
                    return ans;
                }
            }
            return ans;
        }
    }

    /**
     * Returns an array containing all of the elements in this set.
     */
    public Object[] toArray(){
        return this.data.toArray();
    }

    /**
     * Returns an array containing all of the elements in this set; the runtime type of the returned array is that of the specified array.
     */
    public<T> T[] toArray(T[] a){
        if (a.length<this.size()){
            a = (T[]) new Object[this.size()];
        }
        int i = 0;
        MNavigableSetIterator<T> iter = (MNavigableSetIterator<T>) this.iterator();
        while(iter.hasNext()){
            a[i] = iter.next();
            i++;
        }
        return a;
    }


    private ArrayList<T> getData(){
        return data;
    }

}