package concurent.labs.solution;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a dealer who sells crystals.
 * The dealer will get notified by the pipeline when it can take the next batch to sell
 * then it will be busy selling it.
 */
public class CrystalDealer {

    private static final int DEALING_TIME_MS = 500;

    private final CrystalPipeline pipeline;
    private final String name;

    public CrystalDealer(final String name, final CrystalPipeline pipeline){
        this.name = name;
        this.pipeline = pipeline;
    }

    /**
     * While the simulation is running, dealers will try to get something to sell
     * @param isOver Whether the simulation is over or not
     */
    public void beginSelling(AtomicBoolean isOver){
        while(!isOver.get()){
            Thread t = new Thread( () -> this.sell());
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong with dealing for " + this.name);
                break;
            }
        }
    }

    /**
     * First dealers wait for a batch to sell,
     * then they start selling it
     */
    private void sell(){
        int batch = pipeline.tryToTake();

        System.out.println(this.name + " received " + batch + " pounds of crystal, starting to sell");

        try {
            Thread.sleep(DEALING_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.name + " sold " + batch + " pounds of crystal");
    }
}
