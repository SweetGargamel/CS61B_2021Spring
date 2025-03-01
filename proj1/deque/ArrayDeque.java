package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>,Deque<T> {

    private class ArryListIterator implements Iterator<T> {
        private int cursor;//
        private int time;//记录我已经循环过的次数

        public ArryListIterator() {
            time = 0;
            cursor = front;
        }

        @Override
        public boolean hasNext() {
            return time < size;
        }

        @Override
        public T next() {
            cursor = move_pointer(cursor,1);
            this.time+=1;
            return (data[cursor]);
        }
    }


    private int size;
    private int capacity;
    private int front;
    private int last;
    private T[] data;

    public ArrayDeque() {
        this.size = 0;
        this.capacity = 8;
        this.front = capacity - 1;
        this.last = 0;
        data = (T[]) new Object[capacity];
    }

    private int move_pointer(int curr, int forward) {
        return (curr + forward + capacity) % capacity;
    }
    @Override
    public void addFirst(T item) {
        check_Mul();
        data[front] = item;
        front = move_pointer(front, -1);
        size++;
    }
    @Override

    public void addLast(T item) {
        check_Mul();
        data[last] = item;
        last = move_pointer(last, 1);
        size++;
    }


    @Override
    public int size() {
        return size;
    }

    @Override

    public void printDeque() {
        int p = front + 1;
        for (int i = 0; i < size; i++, p = move_pointer(p, 1)) {
            System.out.print(data[p] + " ");
        }
        System.out.println();
    }

    private void check_Mul() {
        if (size == capacity) {
            resize(capacity * 2);
        }
    }

    private void check_Div() {
        if (size * 4 < capacity && capacity >= 16) {
            resize(capacity / 4);
        }
    }
    private void resize(int newcapacity) {
        T [] tmp= (T[]) new Object[newcapacity];
        int index=move_pointer(front, 1);
        for (int i = 0; i < size; i++) {
            tmp[i]=data[index];
            index = move_pointer(index, 1);
        }
        data=tmp;
        front = newcapacity-1;
        capacity=newcapacity;
        last=size;
    }

    @Override

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        check_Div();
        front = move_pointer(front, +1);
        T item = data[front];
        data[front] = null;
        size--;
        return item;
    }
    @Override

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        check_Div();


        last = move_pointer(last, -1);
        T item = data[last];
        data[last] = null;
        size--;
        return item;
    }
    @Override

    public T get(int index) {
//        int p = front;
//        p=  move_pointer(p, 1);
//        for (int i = 0; i <= index; i++, p = move_pointer(p, 1)) {} ;
        return data[(front + 1 + index) % data.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDeque.ArryListIterator();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque)) return false;
        Deque<?> other = (Deque<?>) o;
        if (size != other.size()) return false;

        for(int i=0;i<size;i++){
            if(!this.get(i).equals(other.get(i))) return false;
        }
        return true;
    }

}
