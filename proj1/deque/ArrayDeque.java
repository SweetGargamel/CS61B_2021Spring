package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {


    private class ArryListIterator implements Iterator<T> {
        private int cursor;//
        private int time;//记录我已经循环过的次数
        public ArryListIterator() {
            time=0;
            cursor = front;
        }

        @Override
        public boolean hasNext() {
            return time < size;
        }

        @Override
        public T next() {
            cursor=(cursor+1)%capacity;
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


    public void addFirst(T item) {
        resize_bigger();
        data[front] = item;
        front= (front -1)%capacity;
        size++;
    }

    public void addLast(T item) {
        resize_bigger();

        data[last] = item;
        last =(last + 1)%capacity;
        size++;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        int p=front+1;
        for(int i=0;i<size;i++,p=(p+1)%capacity) {
            System.out.print(data[p]+" ");
        }
    }

    private void resize_bigger() {
        if(size<capacity){
            return;
        }

        int new_capacity = capacity*2;
        T tmp[]=(T[]) new Object[new_capacity];
        for(int i=0;i<last;i++){
            tmp[i]=data[i];
        }
        for(int i=capacity-1;i>front;i--){
            tmp[i+capacity]=data[i];
        }
        capacity = new_capacity;
        data=tmp;
    }
    private void resize_smaller() {
        if(size*4<capacity && capacity>=16){
            int new_capacity = capacity/4;
            T tmp[]=(T[]) new Object[capacity];
            for(int i=0;i<last;i++){
                tmp[i]=data[i];
            }
            for(int i=capacity-1;i>front;i--){
                tmp[i-new_capacity*3]=data[i];
            }
        }
    }


    public T removeFirst() {
        if(size==0){
            return null;
        }
        resize_smaller();

        T item=data[(front+1)%capacity];
        front++;
        size--;
        return item;
    }

    public T removeLast() {
      if(size==0){
          return null;
      }
      resize_smaller();
      T item=data[(last-1)%capacity];
      last--;
      size--;
      return item;
    }

    public T get(int index) {
        int p=front;

        for (int i=0;i<=index;i++,p=(p+1)%capacity);
        return data[p];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDeque.ArryListIterator();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ArrayDeque) {
            ArrayDeque<T> other = (ArrayDeque<T>) o;
            if (this.size != other.size) {
                return false;
            } else if(this.capacity != other.capacity) {
                return false;
            }else{
                int p=front+1;
                for(int i=0;i<size;i++,p=(p+1)%capacity) {
                    if (this.data[p] != other.data[p]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

}
