package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        return null;
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
        int index = key.hashCode();
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
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
