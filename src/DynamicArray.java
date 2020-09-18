import java.util.Iterator;

@SuppressWarnings("unchecked")
public class DynamicArray <T> implements Iterable <T> {

    private T[] arr;
    private  int len=0; //Length of the Array as per user
    private int capacity = 0;  //Actual length of the Array

    public DynamicArray(){
        this(16);
    }

    public DynamicArray(int capacity){
        if(capacity < 0)
            throw new IllegalArgumentException("Illegal Argument " + capacity);
        this.capacity = capacity;
        arr =(T[]) new Object[capacity];
    }

//Returns the Size of the Array
    public int size(){
        return len;
    }

//Checks whether the Array is Empty or not
    public boolean isEmpty(){
        return size() == 0;
    }

//Returns the value at a given index
    public T get(int index){
        return arr[index];
    }
//Set the value at given index to val
    public void set(int index, T val){
        arr[index] = val;
    }

//Clears the array
    public void clear(){
        for (int i =0; i < capacity; i++){
            arr[i] = null;
        }
        len = 0;
    }

//Adds New Element at the end
    public void addToEnd(T val){
        if (len+1 >= capacity){
            if (capacity == 0)
                capacity = 1;
            else
                capacity *= 2;
            T[] new_arr =(T[]) new Object[capacity];
            for(int i = 0; i < len; i++){
                new_arr[i] = arr[i];
            }
            arr = new_arr;
        }
        arr[len++] = val;
    }

//Adds element ot the array at the provided index
    public void add(int idx, T val){
        if(idx > len && idx < 0)
            throw new IndexOutOfBoundsException();
        if (len+1 >= capacity) {
            if (capacity == 0) {
                capacity = 1;
            } else
                capacity *= 2;
        }
        T[] new_arr = (T[]) new Object[capacity];
        for (int i = 0; i < len + 1; i++) {
            int index;
            if (i >= idx)
                index = i + 1;
            else
                index = i;
            new_arr[index] = arr[i];
        }
        arr = new_arr;
        arr[idx] = val;
    }

//Remove element from the provided index
    public T removeAt(int rm_idx){
        if (rm_idx >= len && rm_idx < 0)
            throw new IndexOutOfBoundsException();
        T data = arr[rm_idx];
        T[] new_arr =(T[]) new Object[len-1];
        for(int i = 0, j=0; i < len; i++, j++){
            if(i == rm_idx)
                j--;
            else
                new_arr[j] = arr[i];
        }
        arr = new_arr;
        capacity = --len;
        return data;
    }

//Remove the provided element
    public boolean remove(Object obj){
        for(int i = 0; i < len; i++){
            if(arr[i].equals(obj)){
                removeAt(i);
                return true;
            }
        }
        return false;
    }

//Returns the index of the provided element
    public int indexOf(Object obj){
        for(int i = 0; i < len; i++){
            if(arr[i].equals(obj)){
                return i;
            }
        }
        return -1;
    }

//Checks whether the element is present in the array or not
    public boolean contains(Object obj){
        return indexOf(obj) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < len;
            }

            @Override
            public T next() {
                return arr[index++];
            }
        };
    }

    @Override
    public String toString(){
        if(len == 0){
            return "[]";
        }
        else{
            StringBuilder sb = new StringBuilder(len).append("[");
            for(int i = 0; i < len-1; i++){
                sb.append(arr[i] + ", ");
            }
            return sb.append(arr[len-1] + "]").toString();
        }
    }
}
