package deque;

import java.util.Iterator;
//import java.util.List;
//
public class LinkedListDeque<T> implements  Iterable<T> ,Deque<T>{


    private class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private class ListIterator implements Iterator<T> {
        private int cursor;
        private Node<T> current;
        public ListIterator() {
            cursor = 0;
            current = sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return cursor <size;
        }

        @Override
        public T next() {
            T data= current.data;
            current = current.next;
            return data;
        }
    }


    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<T>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
    }
    @Override
    public void addFirst(T item) {
        Node<T> new_node = new Node<>(item, sentinel.next, sentinel);
        sentinel.next.prev = new_node;
        sentinel.next = new_node;
        this.size += 1;
    }
    @Override
    public void addLast(T item) {
        Node<T> new_node= new Node<>(item, sentinel, sentinel.prev);
        sentinel.prev.next =new_node;
        sentinel.prev = new_node;
        this.size += 1;
    }
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    @Override
    public int size() {
        return this.size;
    }
    @Override
    public void printDeque() {
        Node<T> curr = sentinel.next;
        for (int i = 0; i < this.size - 1; i++) {
            System.out.print(curr + " ");
            curr = curr.next;
        }
        System.out.println(curr);
    }
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        Node<T> tmp = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        this.size-=1;
        T item = tmp.data;
        tmp=null;
        return item;
    }
    @Override
    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        }
        Node<T> curr = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        T item = curr.data;
        curr = null;
        this.size -= 1;
        return item;
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }else{
            Node<T> curr = sentinel.next;
            for(int i=0;i<index;i++){
                curr = curr.next;
            }
            return curr.data;
        }
    }

    public T getRecrusive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }
    private T getRecursiveHelper(Node<T> node, int index){
        if(index == 0){
            return node.data;
        }else{
            return getRecursiveHelper(node.next, index);
        }

    }


    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }


    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o instanceof LinkedListDeque){
            LinkedListDeque<T> other = (LinkedListDeque<T>) o;
            if(this.size != other.size){
                return false;
            }else{
                Node<T> curr_this = this.sentinel.next;
                Node<T> curr_other = other.sentinel.next;
                for(int i=0;i<this.size;i++){
                    if(curr_this.data!=curr_other.data){
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }


}