import java.util.*;

public class DataMover {
    public static int[] data;
    public static ArrayList<Thread> movers = new ArrayList<>();
    private static final Object lockObject = new Object();

    public static void main(String[] args) {
        int MSEC_WAIT_TIME_ALL;
        if(args.length < 1) {
            MSEC_WAIT_TIME_ALL = 123;
            args = new String[]{"0", "111", "256", "444"};
        }
        else {
            MSEC_WAIT_TIME_ALL = Integer.parseInt(args[0]);
        }
        data = new int[args.length-1];
        for (int i = 0; i < args.length-1; i++) {
            data[i] = i * 1000;
        }

        for(int j = 1; j < args.length; j++) {
            int wait = Integer.parseInt(args[j]);
            int idx = j;
            int size = args.length;
            Thread thread = new Thread(() -> {
                    for(int l = 0; l < 10; l++) {
                        try {
                            Thread.sleep(MSEC_WAIT_TIME_ALL);
                            synchronized (lockObject) {
                                data[idx - 1] -= idx - 1;
                                System.out.println("#" + (idx-1) + ": data " + (idx - 1) + " == " + data[idx - 1]);
                                Thread.sleep(wait);
                                if (idx + 1 == size) {
                                    data[0] += idx - 1;
                                    System.out.println("#" + (idx-1) + ": data " + 0 + " -> " + data[0]);
                                } else {
                                    data[idx] += idx - 1;
                                    System.out.println("#" + (idx-1) + ": data " + idx + " -> " + data[idx]);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
            );
            movers.add(thread);
            thread.start();
        }

        for(Thread t: movers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Vegso lista: " + Arrays.toString(data));
        /*for(int num: data) {
            System.out.print(num + " ");
        }*/
    }


}