package bstmap;

import jdk.dynalink.NamedOperation;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B {
    private BSTNode root;
    private int size;
    private static class BSTNode<K extends Comparable, V> {
        public K key;
        public V value;
        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public BSTMap() {
        this.root = null;
    }

    @Override
    public void clear() {
        this.root = null;
        this.root.right = null;
        this.root.left = null;
    }

    @Override
    public boolean containsKey(Object key) {
        return getHelper(key,root)!=null;
    }


    @Override
    public Object get(Object key) {
        return getHelper(key, root);
    }

    private Object getHelper(Object key, BSTNode curr_node) {
        if (curr_node == null) {
            return null;
        }
        if(curr_node.key.compareTo(key) == 0) {
            return curr_node.value;
        }else if(curr_node.key.compareTo(key) > 0) {
            return getHelper(key, curr_node.left);
        }else {
            return getHelper(key, curr_node.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(Object key, Object value) {
        putHelper(key, value, root);
    }
    private void putHelper(Object key, Object value,BSTNode curr_node) {
        if(key == null) {
            return;
        }
        if(curr_node == null) {
            return;
        }
        if(curr_node.key.compareTo(key) == 0) {
            this.size++;
            return;
        }else if(curr_node.key.compareTo(key) > 0) {
            putHelper(key, value, curr_node.right);
        }else {
            putHelper(key, value, curr_node.left);
        }
    }
    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();

    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public Object remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();

    }
}
