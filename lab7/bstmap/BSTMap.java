package bstmap;

import jdk.dynalink.NamedOperation;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    private class BSTNode {
        public K key;
        public V value;
        public BSTNode left;
        public BSTNode right;
        public int size;

        public BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
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
        this.size = 0;
        this.root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key, root) != null;
    }

    @Override
    public V get(K key) {
        BSTNode node = getNode(key, root);
        return node == null ? null : node.value;

    }

    private BSTNode getNode(K key, BSTNode curr_node) {
        if (curr_node == null) {
            return null;
        }
        if (curr_node.key.compareTo(key) == 0) {
            return curr_node;
        } else if (curr_node.key.compareTo(key) > 0) {
            return getNode(key, curr_node.left);
        } else {
            return getNode(key, curr_node.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        this.root = putHelper(key, value, root);
    }

    private BSTNode putHelper(K key, V value, BSTNode curr_node) {
        if (curr_node == null) {
            this.size++;

            return new BSTNode(key, value, 1);
        }
        if (curr_node.key.compareTo(key) == 0) {
            curr_node.value = value;
        }else if (curr_node.key.compareTo(key) > 0) {
            curr_node.left = putHelper(key, value, curr_node.left);
        }else {
            curr_node.right = putHelper(key, value, curr_node.right);
        }
        return curr_node;
    }

    @Override
    public Set keySet() {
        Set keySet = new HashSet();
        Queue<BSTNode> queue = new LinkedList<BSTNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode curr_node = queue.remove();
            keySet.add(curr_node.key);
            if (curr_node.left != null) {
                queue.add(curr_node.left);
            }
            if (curr_node.right != null) {
                queue.add(curr_node.right);
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

    private static class BSTMapIterator implements Iterator {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode curr_node) {
        if (curr_node == null) {
            return;
        }
        printInOrder(curr_node.left);
        System.out.println(curr_node.key + " "+curr_node.value);
        printInOrder(curr_node.right);
    }

    public static void main(String[] args) {
        BSTMap<String, String> map = new BSTMap<String, String>();
        map.put("D", "E");
        map.put("C", "D");
        map.put("E", "F");
        map.put("A", "B");
        map.put("B", "C");
        map.put("F", "G");
        map.printInOrder();
    }
}
