package tester;
import static org.junit.Assert.*;

import afu.org.checkerframework.checker.signature.qual.SourceName;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void testStu(){
        StudentArrayDeque<Integer> sad1=new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> tester =new ArrayDequeSolution<>();
        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                tester.addLast(i);
            } else {
                sad1.addFirst(i);
                tester.addFirst(i);
            }
        }
        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                assertEquals("Should have the same value",sad1.removeFirst(), tester.removeFirst());
            } else {
                assertEquals("Should have the same value",sad1.removeLast(), tester.removeLast());

            }
        }
    }

}
