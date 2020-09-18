import java.util.*;

class Entry <K,V> {
    int hash;
    K key;
    V value;

    public Entry(K key, V value){
        this.key = key;
        this.value = value;
        this.hash = key.hashCode();
    }
    public boolean equals(Entry <K,V> other){
        if(hash != other.hash)
            return false;
        return key.equals(other.key);
    }
    @Override
    public String toString(){
        return key + " => " + value;
    }
}

@SuppressWarnings("unchecked")
public class HashTableSeparateChaining <K,V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private double maxLoadFactor;
    private int capacity, threshold, size = 0;
    private LinkedList <Entry<K,V>> [] table;

    public HashTableSeparateChaining(){
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity){
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity, double maxLoadFactor){
        if(capacity < 0)
            throw new IllegalArgumentException("Illegal capacity");
        if(maxLoadFactor <= 0 || Double.isNaN(maxLoadFactor) || Double.isInfinite(maxLoadFactor))
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
        threshold = (int)(this.capacity * maxLoadFactor);
        table = new LinkedList[this.capacity];
    }

    //Returns the size of the HashTable
    public int size(){
        return size;
    }

    //Returns true when the HashTable is empty
    public boolean isEmpty(){
        return size == 0;
    }

    //This function strips the negative sign and brings the hash in the range of [0, capacity]
    private int normalizeIndex(int keyHash){
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    //Clears the HashTable
    public void clear(){
        Arrays.fill(table, null);
        size = 0;
    }

    //Returns true if the HashTable contains the key
    public boolean containsKey(K key){
        return hasKey(key);
    }
    private boolean hasKey(K key){
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketSeekEntry(bucketIndex, key) != null;
    }

    //Adds element to the Hash Table
    public V put(K key, V value){
        return insert(key, value);
    }
    public V add(K key, V value){
        return insert(key, value);
    }
    public V insert(K key, V value){
        if (key == null)
            throw new IllegalArgumentException("Null Key");
        Entry<K,V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketInsertEntry(bucketIndex, newEntry);
    }

    //Returns the value corresponding to the provided key
    public V get(K key){
        if(key == null)
            return null;
        int bucketIndex = normalizeIndex(key.hashCode());
        Entry<K,V> entry = bucketSeekEntry(bucketIndex, key);
        if(entry != null)
            return entry.value;
        return null;
    }

    //Removes the element which has the provided key
    public V remove(K key){
        if(key == null)
            return null;
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketRemoveEntry(bucketIndex, key);
    }

    //Private methods used to complete the above tasks
    private V bucketRemoveEntry(int bucketIndex, K key){
        Entry<K,V> entry = bucketSeekEntry(bucketIndex, key);
        if(entry == null)
            return null;
        else {
            LinkedList<Entry<K,V>> list = table[bucketIndex];
            list.remove(entry);
            --size;
            return entry.value;
        }
    }
    private V bucketInsertEntry(int bucketIndex, Entry<K,V> entry){
        LinkedList <Entry<K,V>> bucket = table[bucketIndex];
        if(bucket == null)
            table[bucketIndex] = bucket = new LinkedList<>();

        Entry<K,V> existentEntry = bucketSeekEntry(bucketIndex, entry.key);
        if(existentEntry == null){
            bucket.add(entry);
            if(++size > threshold)
                resizeTable();
            return null;
        }
        else {
            V oldVal = existentEntry.value;
            existentEntry.value = entry.value;
            return oldVal;
        }
    }
    private Entry<K,V> bucketSeekEntry(int bucketIndex, K key){
        if(key == null)
            return null;
        LinkedList<Entry<K,V>> bucket = table[bucketIndex];
        if(bucket == null)
            return null;
        for (Entry<K,V> entry: bucket)
            if(entry.key.equals(key))
                return entry;
        return null;
    }

    //Resizes the table when the size of the table exceeds the threshold
    private void resizeTable(){
        capacity *= 2;
        threshold = (int) (capacity * maxLoadFactor);

        LinkedList<Entry<K,V>> [] newTable = new LinkedList[capacity];

        for(int i = 0; i < table.length; i++){
            if(table[i] != null){
                for(Entry<K,V> entry: table[i]){
                    int bucketIndex = normalizeIndex(entry.hash);
                    LinkedList<Entry<K,V>> bucket = newTable[bucketIndex];
                    if(bucket == null)
                        newTable[bucketIndex] = bucket = new LinkedList<>();
                    bucket.add(entry);
                }
                table[i].clear();
                table[i] = null;
            }
        }
        table = newTable;
    }

    //Returns the list of keys in the Hashtable
    public List <K> keys(){
        List<K> keys = new ArrayList<>(size());
        for(LinkedList<Entry<K,V>> bucket: table){
            if(bucket != null){
                for(Entry<K,V> entry: bucket){
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }

    //Return the list of values in the hashtable
    public List <V> values(){
        List<V> values = new ArrayList<>(size());
        for(LinkedList<Entry<K,V>> bucket: table){
            if(bucket != null){
                for(Entry<K,V> entry: bucket){
                    values.add(entry.value);
                }
            }
        }
        return values;
    }

    @Override
    public Iterator<K> iterator() {
        final int elementCount = size();
        return new java.util.Iterator<K>() {
            int bucketIndex = 0;
            java.util.Iterator<Entry<K, V>> bucketIter = (table[0] == null) ? null : table[0].iterator();

            @Override
            public boolean hasNext() {

                // An item was added or removed while iterating
                if (elementCount != size) throw new java.util.ConcurrentModificationException();

                // No iterator or the current iterator is empty
                if (bucketIter == null || !bucketIter.hasNext()) {

                    // Search next buckets until a valid iterator is found
                    while (++bucketIndex < capacity) {
                        if (table[bucketIndex] != null) {

                            // Make sure this iterator actually has elements -_-
                            java.util.Iterator<Entry<K, V>> nextIter = table[bucketIndex].iterator();
                            if (nextIter.hasNext()) {
                                bucketIter = nextIter;
                                break;
                            }
                        }
                    }
                }
                return bucketIndex < capacity;
            }

            @Override
            public K next() {
                return bucketIter.next().key;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < capacity; i++) {
            if (table[i] == null) continue;
            for (Entry<K, V> entry : table[i]) sb.append(entry + ", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
