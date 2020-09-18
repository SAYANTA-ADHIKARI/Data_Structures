import java.util.*;

public class PQueue <T extends Comparable<T>> {

    //Number of elements currently inside Heap
    private int heapSize = 0;
    //The Capacity of the heap
    private int heapCapacity = 0;
    //Dynamic List to track the elements of the Heap
    private List<T> heap = null;
    // Using TreeMap to keep track of possible indices a particular node value is found in the heap
    private Map<T, TreeSet<Integer>> map = new HashMap<>();

    //Constructors
    //Make a Priority Queue of size 1
    public PQueue() {
        this(1);
    }

    //Make a Priority Queue of size provided by the user
    public PQueue(int sz) {
        heap = new ArrayList<>(sz);
    }

    //Make a Priority Queue from the elements of an Array
    public PQueue(T[] elements) {
        heapSize = heapCapacity = elements.length;
        heap = new ArrayList<T>(heapCapacity);

        for (int i = 0; i < heapSize; i++) {
            mapAdd(elements[i], i);
            heap.add(elements[i]);
        }

        //Heapify process
        for (int i = Math.max(0, (heapSize / 2) - 1); i >= 0; i--) {
            sink(i);
        }
    }

    //Makes a Priority Queue from a Collection
    public PQueue(Collection<T> elements) {
        this(elements.size());
        for (T element : elements)
            add(element);
    }

    //Checks whether the PQueue is empty or not
    public boolean isEmpty() {
        return heapSize == 0;
    }

    //Returns the size of the PQueue
    public int size() {
        return heapSize;
    }

    //Clears the PQueue
    public void clear() {
        for (int i = 0; i < heapCapacity; i++) {
            heap.set(i, null);
        }
        heapSize = 0;
        map.clear();
    }

    //Returns the top element of the PQueue
    public T peek() {
        if (isEmpty())
            return null;
        return heap.get(0);
    }

    //Removes and returns the top element
    public T poll() {
        return removeAt(0);
    }

    //Checks whether the element is present or not
    public boolean contains(T element) {
        if (isEmpty())
            return false;
        return map.containsKey(element);
    }

    //Adds an element to the Priority Queue
    public void add(T element) {
        if (element == null)
            throw new IllegalArgumentException();
        if (heapSize < heapCapacity)
            heap.set(heapSize, element);
        else {
            heap.add(element);
            heapCapacity++;
        }
        mapAdd(element, heapSize);
        swim(heapSize);
        heapSize++;
    }

    //Compares two nodes ; returns true if node i is less than node j
    private boolean less(int i, int j) {
        T node1 = heap.get(i);
        T node2 = heap.get(j);

        return node1.compareTo(node2) <= 0;
    }

    //Moves the element at k to its correct position where the heap invariant satisfies; Bottom Up Method
    private void swim(int k) {
        int parent = (k - 1) / 2;
        while (k > 0 && less(k, parent)) {
            swap(parent, k);
            k = parent;
            parent = (k - 1) / 2;
        }
    }

    //Sink is similar to swim just does work in reverse order; Top Down Method
    private void sink(int k) {
        while (true) {
            int left = 2 * k + 1;
            int right = 2 * k + 2;
            int smallest = left;
            if (right < heapSize && less(right, left))
                smallest = right;
            if (left >= heapSize && less(k, smallest))
                break;
            swap(smallest, k);
            k = smallest;
        }
    }

    //Swapping the elements at index i and j
    private void swap(int i, int j) {
        T element_i = heap.get(i);
        T element_j = heap.get(j);

        heap.set(i, element_j);
        heap.set(j, element_i);

        mapSwap(element_i, element_j, i, j);
    }

    //Removes element from the heap
    public boolean remove(T element){
        if(element == null)
            return false;
        Integer index = mapGet(element);
        if(index != null)
            removeAt(index);
            return index != null;
    }

    //Removes element at the provided index
    private T removeAt(int i){
        if(isEmpty())
            return null;
        heapSize--;
        T remove_data = heap.get(i);
        swap(i, heapSize);

        heap.set(heapSize, null);
        mapRemove(remove_data, heapSize);

        T element = heap.get(i);

        sink(i);

        if(heap.get(i).equals(element))
            swim(i);

        return remove_data;
    }

    //Recursively checks whether the heap satisfies the Min Heap invariant
    public boolean isMinHeap(int k){
        if(k >= heapSize)
            return true;
        int left = 2 * k + 1;
        int right = 2 * k + 2;

        if(left < heapSize && !less(k, left))
            return false;
        if(right < heapSize && !less(k, right))
            return false;

        return isMinHeap(left) && isMinHeap(right);
    }

    private void mapAdd(T value, int index){
        TreeSet<Integer> set = map.get(value);
        if(set == null){
            set = new TreeSet<>();
            set.add(index);
            map.put(value, set);
        }
        else
            set.add(index);
    }

    private void mapRemove(T value, int index){
        TreeSet<Integer> set = map.get(value);
        set.remove(index);
        if(set.size() == 0)
            map.remove(value);
    }

    private Integer mapGet(T value){
        TreeSet<Integer> set = map.get(value);
        if(set != null)
            return set.last();
        return null;
    }

    private void mapSwap(T val1, T val2, int val1Index, int val2Index){
        Set<Integer> set1 = map.get(val1);
        Set<Integer> set2 = map.get(val2);

        set1.remove(val1Index);
        set2.remove(val2Index);

        set1.add(val2Index);
        set2.add(val1Index);
    }

    @Override
    public String toString(){
        return heap.toString();
    }
}
