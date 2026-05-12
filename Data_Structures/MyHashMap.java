package Data_Structures;

import java.io.Serializable;
import java.util.HashSet;

public class MyHashMap<K, V> implements Serializable{

    private final int WIDTH = 850;
    private final int HEIGHT = 650;

    private Object[] map;
    private int size;
    private HashSet<K> keySet;

    public MyHashMap() {
        map = new Object[WIDTH * HEIGHT];
        size = 0;
        keySet = new HashSet<>();
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        int index = key.hashCode();

        V previous = (V) map[index];
        if (map[index] == null) {
            size++;
            keySet.add(key);
        }
        map[index] = value;
        return previous;
    }

    @SuppressWarnings("unchecked")
    public V get(Object obj) {
        int index = obj.hashCode();
        return (V) map[index];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object obj) {
        int index = obj.hashCode();

        V removed = null;
        if (map[index] != null) {
            removed  = (V) map[index];
            map[index] = null;
            size--;
            keySet.remove(obj);
        }
        return removed;
    }

    public boolean containsKey(K k) {
        int index = k.hashCode();
        return (map[index] != null) ? true : false;
    }

    public boolean containsValue(V v) {
        for (Object o : map) {
            if (o != null && o.equals(v)) return true;
        }
        return false;
    }

    public int size(){
        return size;
    }

    public HashSet<K> keySet(){
        return keySet;
    }

}