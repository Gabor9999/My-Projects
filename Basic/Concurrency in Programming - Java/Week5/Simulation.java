package concurent.labs.solution;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A small local business is manufacturing and distributing crystal for further sale.
 * Whenever the producers manufacture a batch of crystals, the distributor receives it.
 * After the simulation is over, both of them stop what they are doing.
 *
 */
public class Simulation {

    // how long the simulation runs
    private static final int SIMULATION_DURATION_MSEC = 15000;
    // atomic boolean - thread safe boolean to let
    // producer and distributor know when the simulation ends
    private static final AtomicBoolean isOver = new AtomicBoolean(false);

    private static final List<CrystalDealer> dealers = new LinkedList<>();
    private static final List<Thread> threads = new LinkedList<>();

    public static void main(String[] args) {
        CrystalProducer producer = new CrystalProducer("Walter & Jessie");
        CrystalDistributor distributor = new CrystalDistributor("Gus");
        CrystalPipeline pipeline = new CrystalPipeline();

        for(int i = 0; i < CrystalPipeline.getNumberOfDealers(); i++){
            dealers.add(new CrystalDealer("Dealer" + (i+1), pipeline));
        }

        // Creating the threads and adding them to the list
        Thread producerThread = new Thread( () -> producer.startProducing(isOver));
        Thread distributorThread = new Thread( () -> distributor.startDistributing(isOver, producer, pipeline));
        threads.add(producerThread);
        threads.add(distributorThread);
        for(CrystalDealer cd : dealers){
            Thread t = new Thread(() -> cd.beginSelling(isOver));
            threads.add(t);
        }

        // Starting threads
        for(Thread t : threads)
            t.start();

        // start countdown for simulation to end
        new Thread(() -> {
            try {
                Thread.sleep(SIMULATION_DURATION_MSEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isOver.set(true);
        }).start();

        // Waiting for threads to finish
        for(Thread t : threads ){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
