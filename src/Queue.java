import java.util.Iterator;
import java.util.LinkedList;

public class Queue <T> implements Iterable<T> {

    //Implementing Queue using Java's in-build Linked List class
    private LinkedList<T> list = new LinkedList<T>();

    //To create a empty Queue
    public Queue(){}

    //To create a Queue with one element
    public Queue(T firstElement){
        offer(firstElement);
    }

    //Returns the size of the Queue
    public int size(){
        return list.size();
    }

    //Checks whether the Queue is empty or not
    public boolean isEmpty(){
        return size() == 0;
    }

    //Returns the First element of the Queue
    public T peek(){
        if(isEmpty())
            throw new RuntimeException("Empty Queue");
        return list.peekFirst();
    }

    //Returns and Removes the first element of the Queue
    public T poll(){
        if(isEmpty())
            throw new RuntimeException("Queue Empty");
        return list.removeFirst();
    }

    //Adds element to the last of the Queue
    public void offer(T val){
        list.addLast(val);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
