package concurent.labs.solution;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A pipeline that connects distribution with the dealers.
 * The distributor submit the splits of the current batch
 * and the dealers will take it
 */
public class CrystalPipeline {

    private static final int NUMBER_OF_DEALERS = 3;

    private static final Queue<Integer> crystalPipeline = new LinkedList<>();

    /**
     * Used by the distributor - submits a split of the current batch to the pipeline
     * @param split
     */
    public void submit(final int split){
        synchronized (crystalPipeline){
            crystalPipeline.add(split);
            crystalPipeline.notifyAll();
        }
    }

    /**
     * Used by the dealers - blocks until they can get a split of the current batch
     * then they receive that split, removing it from the pipeline
     * @return
     */
    public int tryToTake(){
        synchronized (crystalPipeline){
            while(crystalPipeline.isEmpty()){
                try {
                    crystalPipeline.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return crystalPipeline.remove();
        }
    }

    /**
     * The number of dealers - this determines how many parts the batch needs to be split into
     * @return Number of dealers
     */
    public static int getNumberOfDealers(){
        return NUMBER_OF_DEALERS;
    }
}
