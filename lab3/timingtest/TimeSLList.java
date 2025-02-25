package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void call_insert(SLList<Integer> s, int n) {
        for(int i=1;i<=n;i++) {
            s.addLast(1);
        }
    }

    public static void call_getlast(SLList<Integer> s, int opt_time) {
        for(int i=1;i<=opt_time;i++) {
            s.addLast(1);
        }
    }
    public static void timeGetLast() {
        AList<Integer> Ns =new AList<>();
        AList<Double> times=new AList<>();
        AList<Integer> opCounts=new AList<>();
        int opt_time=10000;
        int Nlist[]={1000,2000,4000,8000,16000,32000,64000,128000};
        for(int n : Nlist){
            SLList<Integer> s=new SLList<>();
            call_insert(s,n);

            Stopwatch sw = new Stopwatch();
            call_getlast(s,opt_time);
            double timeInSeconds = sw.elapsedTime();


            times.addLast(timeInSeconds);
            Ns.addLast(n);
            opCounts.addLast(opt_time);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
