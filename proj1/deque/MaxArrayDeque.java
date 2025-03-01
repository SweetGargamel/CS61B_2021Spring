package deque;



import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>  {

    Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c){
        super();
        comparator = c;
    }

    private T getMax(Comparator<T> c){
        T max =get(0);
        for(int i=0;i<size;i++){
            if(c.compare(max,get(i))<0){
                max = get(i);
            }
        }
        return max;
    }

    public T max(){
        return getMax(comparator);
    }
    public T max(Comparator<T> c){
        return getMax(c);
    }


}


