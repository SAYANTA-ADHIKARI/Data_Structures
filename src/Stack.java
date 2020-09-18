import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;

public class Stack<T> implements Iterable<T>{

    //Implementing Stack using Java's Linked List
    private LinkedList<T> list = new LinkedList<T>();

    //Initialise a empty Stack
    public Stack(){}

    //Initialise a Stack with one element
    public Stack(T firstElement){
        push(firstElement);
    }

    //Returns the size of the Stack
    public int size(){
        return list.size();
    }

    //Checks whether the stack is empty or not
    public boolean isEmpty(){
        return size() == 0;
     }

    // Pushs a element to the top of the Stack
    public void push(T val){
       list.addLast(val);
    }

    //Returns and Removes the top element of the Stack
    public T pop(){
       if(isEmpty())
           throw new EmptyStackException();
       return list.removeLast();
    }

     //Returns the top element of the Stack
    public T peek(){
       if(isEmpty())
           throw new EmptyStackException();
       return list.peekLast();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
