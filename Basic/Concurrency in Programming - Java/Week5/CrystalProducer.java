package concurent.labs.solution;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Produces a random amount of crystals at a fix time rate.
 *
 */
public class CrystalProducer {

    private static final int PRODUCE_TIME = 1000;
    private static final int CRYSTAL_MAX_POUND = 300;
    private static final int CRYSTAL_MIN_POUND = 100;

    private final String name;
    // lock for currentBatch
    private final Object batchLock = new Object();
    private Integer currentBatch;

    public CrystalProducer(final String name){
        this.name = name;
    }

    /**
     * While the simulation is not over starts producing on a separate thread.
     *
     * @param isOver Indicates if the simulation is over
     */
    public void startProducing(final AtomicBoolean isOver){
        while(!isOver.get()){
            Thread t = new Thread(this::produce);
            t.start();

            // wait till its over
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong with production");
                break;
            }
        }
    }

    /**
     * Returns the amount of the current batch.
     * Then sets it to null
     * @return The actual batch
     */
    public Integer takeCurrentBatch(){
        synchronized (batchLock) {
            final Integer current = this.currentBatch;
            this.currentBatch = null;
            return current;
        }
    }

    /**
     * Indicates if the current batch has already been made
     * @return If the current batch is usable or not
     */
    public boolean isBatchReady(){
        synchronized (batchLock){
            return this.currentBatch != null;
        }
    }

    /**
     * Waits until the produce time then set currentBatch to a random value
     * within its range
     */
    private void produce(){
        try {
            Thread.sleep(PRODUCE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (batchLock) {
            currentBatch = ThreadLocalRandom.current().nextInt(CRYSTAL_MIN_POUND, CRYSTAL_MAX_POUND);
        }

        System.out.println(this.name + " produced " + this.currentBatch + " pounds" );

        synchronized (this){
            this.notifyAll();
        }
    }
}
