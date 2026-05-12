package Data_Structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class MyHashSet<E> implements Iterable<E>, Serializable{
    
    private final int WIDTH = 850;
    private final int HEIGHT = 650;
    private Object[] set;
    int size;

    public MyHashSet(){
        set = new Object[WIDTH * HEIGHT];
        size = 0;
    }

    public boolean add(E e){
        int index = Math.abs(e.hashCode());
        
        if (contains(e) != true) {
            set[index] = (Object) e;
            size++;
            return true;
        }
        return false;
    }

    public boolean contains(Object obj){
        
        int index = Math.abs(obj.hashCode());
        if (set[index] != null) {
            return true;
        }
        return false;
    }

    public boolean remove(Object obj){
        int index = Math.abs(obj.hashCode());
        if (set[index].equals(obj)) {
            set[index] = null;
            size--;
            return true;
        }
        return false;
    }

    public int size(){
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index < set.length && set[index] == null) {
                    index++;
                }
                return index < set.length;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) set[index++];
            }
        };
    }
}
