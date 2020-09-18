import java.util.Iterator;

public class DoublyLinkedList <T> implements Iterable <T>{

    int size = 0; // Tracks the size of the linked list
    Node <T> head = null;  //It points towards the head of the link list
    Node <T> tail = null;  //It points towards the tail/end of the link list

    //Internal Node class to represent data
    private class Node <T>{
        T data;
        Node <T> prev, next;

        public Node(T data, Node <T> prev, Node <T> next){
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString(){
            return data.toString();
        }
    }

    //Clears the linked list
    public void clear(){
        Node <T> temp = head;
        while(temp != null){
            Node<T> next = temp.next;
            temp.next = temp.prev = null;
            temp.data = null;
            temp = next;

        }
        head = tail = temp = null;
        size = 0;
    }

    //Returns the size of the Linked List
    public int size(){
        return size;
    }

    //Returns whether the linked list is empty or not
    public boolean isEmpty(){
        return size() == 0;
    }

    //Adds the element to the First of the Linked List
    public void addFirst(T val){
        if(isEmpty()){
            head = tail = new Node<T>(val, null, null);
        }
        else{
            head.prev = new Node<T>(val, null, head);
            head = head.prev;
        }
        size++;
    }

    //Adds the element to the end of the Linked List
    public void addLast(T val){
        if(isEmpty()){
            head = tail = new Node<T>(val, null, null);
        }
        else {
            tail.next = new Node<T>(val, tail, null);
            tail = tail.next;
        }
        size++;
    }

    //Adds element at the provided location
    public void add(T val, int idx){
        if(idx >= size + 1 || idx < 0){
            throw new IndexOutOfBoundsException();
        }
        else if(idx == 0){
            head = tail = new Node<T>(val, null, null);
            size++;
        }
        else {
            if (idx < size/2) {
                Node<T> t;
                t = head;
                int count = 0;
                while (count < idx-1){
                    t = t.next;
                    count++;
                }
                Node<T>new_node = new Node<T>(val, t, t.next);
                t.next.prev = new_node;
                t.next = new_node;

            }
            else {
                Node<T> t;
                t = tail;
                int count = size - 1;
                while (count > idx){
                    t = t.prev;
                    count--;
                }
                Node<T>new_node = new Node<T>(val, t.prev, t);
                t.prev.next = new_node;
                t.prev = new_node;
            }
            size++;
        }

    }

    //Checks the value of first node if it exists
    public T peekFirst(){
        if(isEmpty())
            throw new RuntimeException("Empty List");
        return head.data;
    }

    //Checks the value of last node if it exists
    public T peekLast(){
        if(isEmpty())
            throw new RuntimeException("Empty List");
        return tail.data;
    }

    //Remove element from the first
    public T removeFirst(){
        if(isEmpty())
            throw new RuntimeException("Empty List");
        T data = head.data;
        head = head.next;
        --size;

        if(isEmpty())
            tail = null;
        else
            head.prev = null;

        return data;
    }

    //Remove element from the last
    public T removeLast(){
        if(isEmpty())
            throw new RuntimeException("Empty List");
        T data = tail.data;
        tail = tail.prev;
        --size;

        if(isEmpty())
            head = null;
        else
            tail.next = null;

        return data;
    }

    //Remove arbitrary node
    private T remove(Node<T> node){
        T data;
        if(node.prev == null){
            data = removeFirst();
            return data;
        }
        if(node.next == null){
            data = removeLast();
            return data;
        }

        data = node.data;
        node.prev.next = node.next;
        node.next.prev = node.prev;

        node.data = null;
        node = node.next = node.prev = null;

        --size;

        return data;
    }

    //Remove element at the provided index
    public T removeAt(int idx){
        int i;
        Node<T> temp;
        if(idx < size/2){
            for(i = 0, temp = head; i != idx; i++ )
                temp = temp.next;
        }
        else {
            for(i = 0, temp = tail; i != idx; i-- )
                temp = temp.prev;
        }

        return remove(temp);
    }

    //Remove element which has its data equal to the provided data
    public boolean remove(Object obj){
        Node<T> temp;

        if(obj == null){
            for(temp = head; temp != null; temp = temp.next){
                if(temp.data == null){
                    remove(temp);
                    return true;
                }
            }
        }
        else{
            for(temp = head; temp != null; temp = temp.next){
                if(obj.equals(temp.data)){
                    remove(temp);
                    return true;
                }
            }
        }
        return false;
    }

    //Returns the index of the the object provided
    public int indexOf(Object obj){
        int index = 0;
        Node<T> temp;

        if(obj == null){
            for(temp = head; temp != null; temp = temp.next, index++){
                if(temp.data == null){
                    return index;
                }
            }
        }
        else{
            for(temp = head; temp != null; temp = temp.next, index++){
                if(obj.equals(temp.data)){
                    return index;
                }
            }
        }
        return -1;
    }

    //Checks whether the object is present or not
    public boolean contains(Object obj){
        return indexOf(obj) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> temp = head;
            @Override
            public boolean hasNext() {
                return temp != null;
            }

            @Override
            public T next() {
                T data = temp.data;
                temp = temp.next;
                return data;
            }
        };
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder().append('[');
        Node<T> temp = head;
        while (temp.next != null){
            sb.append(temp.data + ", ");
            temp = temp.next;
        }
        sb.append(temp.data + "]");
        return sb.toString();
    }
}
