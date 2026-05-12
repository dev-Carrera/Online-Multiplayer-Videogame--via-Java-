package Data_Structures;
import java.util.Iterator;
public class MyArrayList<E> implements Iterable<E> {
    
    private int capacity;
    private Object[] array;
    private int size;

    public MyArrayList(){
        capacity = 10;
        array = new Object[capacity];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public E get(int index){
        return (E) array[index];
    }

    public void add(E data){
        if (size >= capacity) {
            capacity *= capacity;
            Object[] temp = new Object[capacity];

            for (int i = 0; i < array.length; i++) {
                temp[i] = array[i];
            }
            array = temp;
        }
        array[size] = data;
        size++;
    }

    public void set(int index, E data){
        array[index] = data;
    }

    public void remove(int index){
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null;
        size--;
    }


    @SuppressWarnings("unchecked")
    public boolean contains(E data){
        for (Object object : array) {
            E target = (E)object;
            if (data.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) array[currentIndex++];
            }
        };
    }
}