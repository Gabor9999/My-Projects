package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Covid is in the past, everyone wants to get wasted
 * In this pub there is only one coed toilet, with enough room for 2 people to stand in line
 * A group of 8 people are attending this pub and either drinking beer or going to the toilet.
 * 3 types of beer can be served, according to BeerType enum, each has only one tap
 * When a person orders a type of beer, that tap gets occupied, "locked" for others to order from,
 * so they need to wait for the previous order (releasing the lock)
 */
public class Pub {

    private static final int CLOSING_TIME = 10000;
    private static final int TIMEOUT = 1000;
    private static final int WAIT_TIME = 5000;
    private static final int TOILET_LINE_CAP = 2;
    private static AtomicBoolean isClosed = new AtomicBoolean(false);
    private static BlockingQueue<String> toiletLine = new LinkedBlockingDeque<>(TOILET_LINE_CAP);
    private static ExecutorService customerExecutor = Executors.newFixedThreadPool(9);
    private static List<String> customers = new ArrayList<>();

    private static int[] beerCounter = {0, 0, 0}; // aszok, kobanyai, dreher

    private static final ReentrantLock aszokLock = new ReentrantLock();
    private static final ReentrantLock kobanyaiLock = new ReentrantLock();
    private static final ReentrantLock dreherLock = new ReentrantLock();

    public static void main(String[] args) {

        customers.add("Walter");
        customers.add("Jesse");
        customers.add("Saul");
        customers.add("Hank");
        customers.add("Marie");
        customers.add("Skyler");
        customers.add("Gus");
        customers.add("Mike");

        customerExecutor.submit(Pub::useToilet);
        customers.forEach(s -> customerExecutor.submit(() -> customerAction(s)));

        // wait till closing time
        sleepForMsec(CLOSING_TIME);
        isClosed.set(true);
        try {
            customerExecutor.shutdown();
            customerExecutor.awaitTermination(WAIT_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            customerExecutor.shutdownNow();
        }

        System.out.println("Aszok: " + beerCounter[0] + "\nKobanyai: " + beerCounter[1] + "\nDreher: " + beerCounter[2]);

    }

    /**
     * The given person will either go to toilet or order something
     * @param name
     */
    private static void customerAction(String name){
        while(!isClosed.get()){
            int rand = ThreadLocalRandom.current().nextInt();
            if(rand % 2 == 0){
                orderBeer(name);
            } else {
                tryToUseToilet(name);
            }
            sleepForMsec(2000);
        }
        System.out.println(name + " goes home");
    }

    /**
     * Randomly choosing a bear
     * @param name
     */
    private static void orderBeer(String name){
        BeerType type = getBeerType(name);
        System.out.println(name + " orders " + type.toString());
        if(type == BeerType.ARANY_ASZOK)
            getBeer(aszokLock, name, 0);
        if(type == BeerType.KOBANYAI)
            getBeer(kobanyaiLock, name, 1);
        if(type == BeerType.DREHER)
            getBeer(dreherLock, name, 2);
    }

    /**
     * Abstract method for locking on a beer tap
     * @param lock the appropriate lock
     * @param name name of customer
     * @param index index of beer counter
     */
    private static void getBeer(ReentrantLock lock, String name, int index){
        boolean success = true;
        try {
            System.out.println(name + " is waiting for the beer");
            // lock on tap interruptibly
            lock.lockInterruptibly();
            sleepForMsec(TIMEOUT);
            beerCounter[index]++;
        } catch (InterruptedException e){
            // if interrupted, no success
            success = false;
            e.printStackTrace();
        } finally {
            // either way, release the lock
            lock.unlock();
            System.out.println(success ? name + " got the beer" : name + " didnt get the beer");
        }
    }

    /**
     * Gets a random type of beer
     * @param name customer name
     * @return random type of beer
     */
    private static BeerType getBeerType(String name){
        int rand = ThreadLocalRandom.current().nextInt(3);
        return BeerType.values()[rand];
    }

    /**
     * The customer will try to stand in the toilet line if there is place
     * If there was an empty place in the line, waiting until he can use the toilet,
     * otherwise tries again next time
     *
     * When the customer could stand in line, he begins to wait on himself - the notification
     * pair of this wait happens in the useToilet function
     * @param name the customer
     */
    private static void tryToUseToilet(String name){
        // try to get in line for the toilet
        boolean canUse = toiletLine.offer(name);
        if(canUse){
            System.out.println(name + " is waiting for the toilet");
            // waiting on itself to get notified that the bathroom is empty
            synchronized (name){
                try {
                    name.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name + " finished using the toilet");
        } else {
            System.out.println(name + " wanted to use the toilet, but line was full");
        }
    }

    /**
     * While the pub is open and there are people waiting in the toilet line
     * it will periodically grab the first in the line and make him use the toilet
     * which is represented by the sleep
     * After the customer is done using the toilet, it will send a notification on itself
     * the tryToUseToilet method will pick up
     */
    private static void useToilet(){
        while(!isClosed.get() || !toiletLine.isEmpty()) {
            try {
                // poll - tries to grab the first in the queue, times out if queue is empty
                String name = toiletLine.poll(TIMEOUT, TimeUnit.MILLISECONDS);
                if(name == null)
                    continue;
                sleepForMsec(ThreadLocalRandom.current().nextInt(100,1000));
                // notification on the person using the toilet that he is done
                synchronized (name) {
                    name.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ZZzzzz
     * @param msec
     */
    private static void sleepForMsec(int msec) {
        try {
            TimeUnit.MILLISECONDS.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
