import java.util.*;
import java.util.Set;
import java.util.stream.Stream;

public class MMap<K,V> extends AbstractMap<K,V> {

    protected K[] keys;
    protected V[] values;
    protected int counter;

    public MMap(){
        this.keys = (K[]) new Object[100];
        this.values = (V[]) new Object[100];
        this.counter = 0;
    }

    public MMap(K[] keys, V[] values){
        this.keys = (K[]) new Object[(int) (keys.length*1.5) + 1];
        this.values = (V[]) new Object[(int) (keys.length*1.5) + 1];
        this.counter = 0;
        int count;
        if (keys.length<values.length){
            count = keys.length;
        } else {
            count = values.length;
        }
        for (int i = 0;i<count;i++){
            this.put(keys[i], values[i]);
        }
    }

    public MMap(MMap<K,V> mmap){
        this.keys = mmap.getKeys();
        this.values = mmap.getValues();
        this.counter = mmap.size();
    }

    public void clear(){
        this.keys = (K[]) new Object[100];
        this.values = (V[]) new Object[100];
        this.counter = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0;i<this.counter;i++){
            if (this.values[i].equals(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Pair<K,V>> res = new HashSet<>();
        Pair<K,V> pair;
        for (int i = 0;i<this.counter;i++){
            pair = new Pair<>(keys[i],values[i]);
            res.add(pair);
        }
        return  res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MMap<?, ?> mMap = (MMap<?, ?>) o;
        if (this.counter!=mMap.size()){
            return false;
        }
        for (int i = 0;i<this.counter;i++){
            if (!mMap.containsKey(this.keys[i])){
                return false;
            } else if (!this.values[i].equals(mMap.get(this.keys[i]))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), counter);
        result = 31 * result + Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public V get(Object key) {
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                return values[i];
            }
        }
        return null;
    }

    public V getOrDefault(Object key, V defaultValue){
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                return values[i];
            }
        }
        return defaultValue;
    }

    @Override
    public V put(K key, V value) {
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                V res = this.values[i];
                this.values[i] = value;
                return res;
            }
        }
        if (counter<this.keys.length){
            this.keys[counter] = key;
            this.values[counter] = value;
        } else {
            K[] keyss = this.keys;
            V[] valuess = this.values;
            this.keys = (K[]) new Object[(int) (keys.length*1.5) + 1];
            this.values = (V[]) new Object[(int) (keys.length*1.5) + 1];
            for (int i = 0;i<this.keys.length;i++){
                this.put(keyss[i], valuess[i]);
            }
        }
        counter++;
        return null;
    }

    public V putIfAbsent(K key, V value){
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                if (this.values[i]==null){
                    V res = this.values[i];
                    this.values[i] = value;
                    return res;
                } else {
                    return this.values[i];
                }
            }
        }
        if (counter<this.keys.length){
            this.keys[counter] = key;
            this.values[counter] = value;
        } else {
            K[] keyss = this.keys;
            V[] valuess = this.values;
            this.keys = (K[]) new Object[(int) (keys.length*1.5) + 1];
            this.values = (V[]) new Object[(int) (keys.length*1.5) + 1];
            for (int i = 0;i<this.keys.length;i++){
                this.put(keyss[i], valuess[i]);
            }
        }
        counter++;
        return null;
    }

    @Override
    public boolean isEmpty() {
        if (this.counter!=0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public V remove(Object key) {
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                V res = this.values[i];
                this.values[i] = null;
                return res;
            }
        }
        return null;
    }

    public boolean remove(Object key, Object value){
        for (int i = 0;i<this.counter;i++){
            if (this.keys[i].equals(key)){
                if (this.values[i].equals(value)){
                    V res = this.values[i];
                    this.values[i] = null;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public V replace(K key, V value){
        for (int i = 0;i<counter;i++){
            if (this.keys[i].equals(key)){
                V res = this.values[i];
                this.values[i] = value;
                return res;
            }
        }
        return null;
    }
    public boolean replace (K key, V oldValue, V newValue){
        for (int i = 0;i<counter;i++){
            if (this.keys[i].equals(key)){
                if ((oldValue==null)&&(this.values[i]==null)||(oldValue.equals(this.values[i]))){
                    V res = this.values[i];
                    this.values[i] = newValue;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return counter;
    }

    public Stream<Pair<K,V>> stream(){
        Pair<K,V>[] ans = (Pair<K,V>[]) new Object[counter];
        for (int i = 0;i<counter;i++){
            ans[i] = new Pair<>(keys[i],values[i]);
        }
        return Arrays.stream(ans);
    }


    public K[] getKeys(){
        K[] ans = (K[]) new Object[this.counter];
        for (int i = 0;i<this.counter;i++){
            ans[i] = this.keys[i];
        }
        return ans;
    }

    public V[] getValues(){
        return values;
    }
}
