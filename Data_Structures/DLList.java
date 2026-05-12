package Data_Structures;
import java.io.Serializable;

public class DLList<E> implements Serializable{

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DLList(){
        head = new Node<E>(null);
        tail = new Node<E>(null);
        size = 0;

        head.setNext(tail);
        tail.setPrev(head);
    }

    public boolean add(E e){
        Node<E> element = new Node<E>(e);


        Node<E> before = tail.prev();
        Node<E> after = tail;

        before.setNext(element);
        element.setPrev(before);
        after.setPrev(element);
        element.setNext(after);



        size++;
        return true;
    }

    public E get(int index){

        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index out of bound");
        }
        
        Node<E> element = getNode(index);

        return element.get();
    }


    public E remove(int index){

        Node<E> element = getNode(index);
        Node<E> before = element.prev();
        Node<E> after = element.next();

        before.setNext(after);
        after.setPrev(before);

        size--;
        return element.get();
    }

    public int size(){
        return size;
    }

    public void clear(){
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> current = head.next();
        while (current != tail) {
            sb.append(current.get());
            if (current.next() != tail) {
                sb.append(", ");
            }
            current = current.next();
        }

        sb.append("]");
        return sb.toString();
    }

    public void set(int index, E e){
        getNode(index).set(e);
    }

    private Node<E> getNode(int index){
        int mid = size / 2;

        if (index <= mid){
            Node<E> current = head.next();
            for(int i = 0; i < index; i++){
                current = current.next();
            }
            return current;
        }
        // Higher than mid
        else{
            Node<E> current = tail.prev();
            for(int i = size - 1; i > index; i--){
                current = current.prev();
            }
            return current;
        }
    }



}