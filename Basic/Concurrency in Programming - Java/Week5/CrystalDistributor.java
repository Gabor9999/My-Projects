package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Responsible for distributing the produced crystals.
 * Right now it does not do that, but it still needs to receive the crystals
 */
public class CrystalDistributor {

    private final String name;

    public CrystalDistributor(final String name){
        this.name = name;
    }

    /**
     * Starts distributing.
     * Actually just waits for the producer and then does nothing with it.
     * Does all that till simulation ends.
     * @param isOver Indicates if the simulation is over or not
     * @param producer The producer of the crystals
     */
    public void startDistributing(final AtomicBoolean isOver, final CrystalProducer producer,
                                  final CrystalPipeline pipeline){
        while(!isOver.get()){
            Thread t = new Thread(() -> receiveAndSubmitBatch(producer, pipeline));
            t.start();

            // wait till thread finishes
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong with distribution");
                break;
            }
        }
    }

    /**
     * Waits until a batch is ready then takes it from the producer
     * @param producer The producer with crystals
     */
    private void receiveAndSubmitBatch(final CrystalProducer producer, final CrystalPipeline pipeline){
        synchronized (producer){
            while(!producer.isBatchReady()) {
                try {
                    producer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int currentBatch = producer.takeCurrentBatch();
        System.out.println(this.name + " received a batch of " + currentBatch + " pounds");

        splitBatch(currentBatch).forEach(i -> {
            pipeline.submit(i);
        });
    }

    /**
     * Splits the current batch into (possibly) equal parts for dealers
     * @param batchSize
     * @return
     */
    private List<Integer> splitBatch(final int batchSize){
        List<Integer> result = new ArrayList<>(CrystalPipeline.getNumberOfDealers());
        int remainingBatch = batchSize;
        int split = batchSize / CrystalPipeline.getNumberOfDealers();

        for(int i = 0; i < CrystalPipeline.getNumberOfDealers(); ++i ){
            if((remainingBatch - split) >= split){
                result.add(split);
                remainingBatch -= split;
            } else {
                result.add(remainingBatch);
            }
        }

        return result;
    }
}
