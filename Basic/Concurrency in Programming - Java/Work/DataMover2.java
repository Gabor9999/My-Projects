import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.*;
public class DataMover2 {
    private static final Integer MAX_SIZEOF_QUEUES = 100;
    public static AtomicInteger arrivalCount = new AtomicInteger(0);
    public static AtomicInteger totalSent = new AtomicInteger(0);
    public static AtomicInteger totalArrived = new AtomicInteger(0);
    public static List<BlockingQueue<Integer>> queues = new ArrayList<>();
    public static ExecutorService pool;
    public static List<Integer> discards = new ArrayList<>();
    public static List<Future<DataMover2Result>> moverResults = new ArrayList<>();


    public static class DataMover2Result {
        public Integer count = 0;
        public Integer data = 0;
        public Integer forwarded = 0;
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            args = new String[]{"123", "111", "256", "444"};
        }
        final int size = args.length;
        pool = Executors.newFixedThreadPool(size);
        for(int i = 0; i < size; i++) {
            queues.add(new LinkedBlockingQueue<>(100));
        }
        for(int j = 0; j < size; j++) {
            final int idx = j;
            int wait = Integer.parseInt(args[idx]);
            moverResults.add(pool.submit(() -> {
                DataMover2Result result = new DataMover2Result();
                Random random = new Random();
                int nextJ = 0;
                while (arrivalCount.get() < 5 * size) {
                        if (idx + 1 == size) {
                            nextJ = 0;
                        } else {
                            nextJ = idx + 1;
                        }
                        try {
                            int x = random.nextInt(10001);
                            queues.get(nextJ).put(x);
                            totalSent.addAndGet(x);
                            System.out.println("total " + arrivalCount + "/" + 5 * size + " |#" + idx + " sends " + x);
                            int waittimer = random.nextInt(701) + 300;
                            Integer out = queues.get(idx).poll(waittimer, TimeUnit.MILLISECONDS);
                            if (out == null) {
                                System.out.println("total " + arrivalCount + "/" + 5 * size + " |#" + idx + " got nothing...");
                                continue;
                            } else if (out % size == idx) {
                                arrivalCount.incrementAndGet();
                                result.count++;
                                result.data += out;
                                System.out.println("total " + arrivalCount + "/" + 5 * size + " |#" + idx + " got " + out);
                            } else {
                                result.forwarded++;
                                queues.get(nextJ).put(out - 1);
                                System.out.println("total " + arrivalCount + "/" + 5 * size + " |#" + idx + " forwards " + out + " [" + nextJ + "]");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(wait);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
                return result;
            }));
        }
        try {
            pool.shutdown();
            boolean finished = pool.awaitTermination(30, TimeUnit.SECONDS);
            if(!finished){
                System.out.println("Executor couldnt finish the tasks before termination");
            }
        } catch (InterruptedException e) {
            System.err.println("Await interrupted");
        } finally {
            pool.shutdownNow();
        }

        for(Future<DataMover2Result> d: moverResults) {
            try {
                DataMover2Result res = d.get();
                totalArrived.addAndGet(res.data + res.forwarded);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }
        for(BlockingQueue<Integer> b: queues) {
            Integer queueSum = 0;
            while (b.peek() != null) {
                try {
                    Integer value = b.poll(100, TimeUnit.MILLISECONDS);
                    queueSum += value;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            discards.add(queueSum);
        }
        Integer sumD = 0;
        for (Integer i: discards) {
            sumD += i;
        }
        String output = discards.toString();
        System.out.println("discarded " + output + " = " + sumD);
        if(totalSent.get() != totalArrived.get() + sumD) {
            System.out.println("WRONG sent " + totalSent.get() + " !== got " + (totalArrived.get() + sumD) + " = " + totalArrived + " discarded " + sumD);
        }
        else {
            System.out.println("sent " + totalSent.get() + " === got " + (totalArrived.get() + sumD) + " = " + totalArrived + " discarded " + sumD);
        }
    }
}