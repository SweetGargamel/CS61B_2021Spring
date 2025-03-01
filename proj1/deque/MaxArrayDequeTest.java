package deque;

import org.junit.Test;

import java.util.Comparator;

public class MaxArrayDequeTest {

    public class testCom1 implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2-o1;
        }

    }



    @Test
    public void testMaxArrayDeque() {
        Comparator<Integer> comparator = new testCom1();
        MaxArrayDeque a=new MaxArrayDeque<Integer>(comparator);
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        a.addFirst(4);
        a.addLast(5);
        a.addLast(6);
        a.addLast(77);
        System.out.println(a.max());
    }
}
