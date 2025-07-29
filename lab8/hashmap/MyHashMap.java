package hashmap;

import jh61b.junit.In;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    private int  size;
    private int capacity;
    private double loadFactor;
    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private static int DEFAULT_CAPACITY = 16;
    private static double DEFAULT_LOAD_FACTOR = 0.75;
    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize,DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.capacity=initialSize;
        this.loadFactor=maxLoad;
        this.size=0;
        this.buckets= createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] new_buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            new_buckets[i] = createBucket();
        }
        return new_buckets;
    }


    @Override
    public void clear() {
        this.buckets = createTable(DEFAULT_CAPACITY);
        this.capacity=DEFAULT_CAPACITY;
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {

        return get(key) != null;
    }
    private int getIndexHelper(K key) {
        int hash = key.hashCode();
        int index = Math.floorMod(hash, capacity);
        return index;
    }
    @Override
    public V get(K key) {
        int index=getIndexHelper(key);
        Collection bucket = buckets[index];
        Iterator<Node> iterator = bucket.iterator();
        Node node = null;
        while(iterator.hasNext()) {
            node = iterator.next();
            if(node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        checkResize();
        int index = getIndexHelper(key);
        Iterator<Node> iterator = buckets[index].iterator();
        while(iterator.hasNext()) {
            Node node = iterator.next();
            if(node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        buckets[index].add(createNode(key,value));
        size++;

    }
    private void checkResize(){
        //1 检查是否需要扩
        if(this.size/this.capacity <= this.loadFactor){
            return;
        }
        this.capacity*=2;//扩容
        //创创建新的桶
        Collection<Node>[] new_buckets = createTable(this.capacity);
        for(int i = 0;i< this.buckets.length;i++){
            //拿到旧的bucket
            Collection<Node> bucket = this.buckets[i];
            //把里面的东西重新算一遍hash然后复制
            for(Node node : bucket){
                int index = getIndexHelper(node.key);
                new_buckets[index].add(node);
            }
        }
        this.buckets = new_buckets;
    }
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        for(int i = 0;i< this.buckets.length;i++){
            Collection<Node> bucket = this.buckets[i];
            for(Node node : bucket){
                keySet.add(node.key);
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        int index = getIndexHelper(key);
        Iterator<Node> iterator = buckets[index].iterator();
        Node node = null;
        while(iterator.hasNext()) {
            node = iterator.next();
            if(node.key.equals(key)) {
                V value = node.value;
                buckets[index].remove(node);
                return value;
            }
        }
    }

    @Override
    public V remove(K key, V value) {
        if(containsKey(key)) {
            return remove(key);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

//    public static void main(String[] args) {
//        System.out.println("--- Running MyHashMap Tests ---");
//
//        // Test 1: Basic put and get
//        System.out.println("\n--- Test 1: Basic put and get ---");
//        MyHashMap<String, Integer> map = new MyHashMapHSBuckets<>();
//        map.put("apple", 1);
//        map.put("banana", 2);
//        map.put("cherry", 3);
//
//        System.out.println("Size after 3 puts: " + map.size()); // Expected: 3
//        System.out.println("Value for 'apple': " + map.get("apple")); // Expected: 1
//        System.out.println("Value for 'banana': " + map.get("banana")); // Expected: 2
//        System.out.println("Value for 'grape' (not present): " + map.get("grape")); // Expected: null
//
//        // Test 2: Update existing key
//        System.out.println("\n--- Test 2: Update existing key ---");
//        map.put("apple", 100);
//        System.out.println("Size after updating 'apple': " + map.size()); // Expected: 3
//        System.out.println("New value for 'apple': " + map.get("apple")); // Expected: 100
//
//        // Test 3: containsKey
//        System.out.println("\n--- Test 3: containsKey ---");
//        System.out.println("Contains 'cherry'? " + map.containsKey("cherry")); // Expected: true
//        System.out.println("Contains 'date'? " + map.containsKey("date")); // Expected: false
//
//        // Test 4: keySet and iterator
//        System.out.println("\n--- Test 4: keySet and iterator ---");
//        Set<String> keys = map.keySet();
//        System.out.println("KeySet: " + keys); // Expected: [apple, banana, cherry] (order may vary)
//        System.out.println("Iterating through keys:");
//        for (String key : map) {
//            System.out.println("Key: " + key + ", Value: " + map.get(key));
//        }
//
//        // Test 5: Resizing
//        // With initial capacity 16 and load factor 0.75, resize happens when size > 12.
//        System.out.println("\n--- Test 5: Resizing ---");
//        MyHashMap<Integer, String> numberMap = new MyHashMap<>(4, 0.75);
//        System.out.println("Initial capacity: 4. Adding elements to trigger resize.");
//        numberMap.put(1, "one");
//        numberMap.put(2, "two");
//        numberMap.put(3, "three"); // size=3, capacity=4. 3/4 = 0.75. No resize yet.
//        System.out.println("Size before resize: " + numberMap.size());
//        numberMap.put(4, "four"); // size=4, capacity=4. 4/4 > 0.75. Resize should trigger.
//        System.out.println("Size after resize: " + numberMap.size()); // Expected: 4
//        System.out.println("Value for key 1 after resize: " + numberMap.get(1)); // Expected: one
//        System.out.println("Value for key 2 after resize: " + numberMap.get(2)); // Expected: two
//        System.out.println("Value for key 3 after resize: " + numberMap.get(3)); // Expected: three
//        System.out.println("Value for key 4 after resize: " + numberMap.get(4)); // Expected: four
//
//        // Add more to confirm it's working
//        numberMap.put(5, "five");
//        System.out.println("Value for key 5 after resize: " + numberMap.get(5)); // Expected: five
//
//
//        // Test 6: clear
//        System.out.println("\n--- Test 6: clear ---");
//        System.out.println("Size before clear: " + map.size()); // Expected: 3
//        map.clear();
//        System.out.println("Size after clear: " + map.size()); // Expected: 0
//        System.out.println("Contains 'apple' after clear? " + map.containsKey("apple")); // Expected: false
//        System.out.println("Value for 'banana' after clear: " + map.get("banana")); // Expected: null
//
//        // Test 7: remove (currently unsupported)
//        System.out.println("\n--- Test 7: remove (when implemented) ---");
//        System.out.println("The remove() methods are not implemented yet.");
//        /*
//        // When you implement remove, you can use these tests:
//        MyHashMap<String, Integer> removeMap = new MyHashMap<>();
//        removeMap.put("one", 1);
//        removeMap.put("two", 2);
//        System.out.println("Value for 'one' before remove: " + removeMap.get("one"));
//        removeMap.remove("one");
//        System.out.println("Value for 'one' after remove: " + removeMap.get("one")); // Expected: null
//        System.out.println("Size after remove: " + removeMap.size()); // Expected: 1
//        */
//
//        System.out.println("\n--- All Tests Finished ---");
//    }
}
