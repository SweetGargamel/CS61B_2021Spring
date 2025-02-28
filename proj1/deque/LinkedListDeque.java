package deque;

import java.util.Iterator;
//import java.util.List;
//
public class LinkedListDeque<T> implements  Iterable<T> {


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

    public void addFirst(T item) {
        sentinel.next = new Node<>(item, sentinel.next, sentinel);
        this.size += 1;
    }

    public void addLast(T item) {
        sentinel.prev.next = new Node<>(item, sentinel.prev, sentinel);
        this.size += 1;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        Node<T> curr = sentinel.next;
        for (int i = 0; i < this.size - 1; i++) {
            System.out.print(curr + " ");
            curr = curr.next;
        }
        System.out.println(curr);
    }

    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        Node<T> curr = sentinel.next;
        T item = curr.data;
        sentinel.next = sentinel.next.next;
        curr = null;
        return item;
    }

    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        }
        Node<T> curr = sentinel.prev;
        T item = curr.data;
        sentinel.prev = sentinel.prev.prev;
        curr = null;
        return item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }else{
            Node<T> curr = sentinel.next;
            for(int i=0;i<=index;i++){
                curr = curr.next;
            }
            return curr.data;
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
        if(o instanceof LinkedListDeque other){
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