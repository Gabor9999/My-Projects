package concurent.labs.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulates a spartan race.
 * Multiple contenders attempt to get through multiple obstacles.
 * More info in Obstacle and Contender classes.
 *
 * ***This task should be completed via Futures***
 */
public class SpartanRaceSimulator {

    // I don't know, some random people from various sports
    private static final List<String> CONTENDER_NAMES =
            Arrays.asList("Thierry Henry", "Tom Brady", "Usain Bolt", "Serena Williams", "Ronda Rousey", "Alex Morgan");
    private static final int NUMBER_OF_CONTENDERS = CONTENDER_NAMES.size();
    private static final int NUMBER_OF_THREADS = NUMBER_OF_CONTENDERS * 2;
    private static final ExecutorService executor =Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final int DELAY_BEFORE_SHUTTING_DOWN_MSEC = 500;

    private static final List<Contender> contenders = new ArrayList<>();
    private static final List<Obstacle> obstacles = Collections.synchronizedList( new ArrayList<>());

    private static final AtomicInteger currentPlacement = new AtomicInteger(1);
    private static final AtomicBoolean hasStarted = new AtomicBoolean(false);

    public static void main(String[] args) {
        // Creating the contenders
        for(String name : CONTENDER_NAMES){
            contenders.add(new Contender(name));
        }

        // Creating the obstacles
        obstacles.add(new Obstacle("Uphill Running", 4, c -> c.getStamina()));
        obstacles.add(new Obstacle("Balance", 5/* this is very hard */, c -> c.getAgility()));
        obstacles.add(new Obstacle("Long Forest Path", 2, c -> c.getRunSpeed()));
        obstacles.add(new Obstacle("Sandbag Carry", 3, c -> c.getStrength()));
        obstacles.add(new Obstacle("Lake", 2, c -> c.getSwimSpeed()));

        // Setting contenders up at the start line
        startContenders();

        // Starting the race
        hasStarted.set(true);
        System.out.println("Race is starting");
        // The contenders are waiting for the notification whether they should start the race or not
        for(Contender contender : contenders){
            synchronized (contender){
                contender.notify();
            }
        }

        // Shuts down executor when race is over
        startStatusCheckerThread();

    }

    /**
     * Starting each contender via a future.
     * The future returns with a string (startRace method),
     * which will contain the contender's name and place.
     */
    private static void startContenders(){
        for(Contender contender : contenders){
            executor.submit(() -> {
                try {
                    System.out.println(executor.submit(() -> startRace(contender)).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Starts the race for a contender
     * While hasStarted is false, they will keep waiting at the line.
     * Once hasStarted is flipped to true, they will get notified about the start
     * and keep attempting the obstacles
     * @param contender The contender
     * @return Message with contender name and placement
     */
    private static String startRace(final Contender contender){

        System.out.println(contender.getName() + " is waiting at the start line");
        synchronized (contender){
            // Avoid spurious wake ups with this while(condition) check
            while(!hasStarted.get()){
                try {
                    // Waiting for notification that happens after hasStarted gets set to true
                    contender.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(contender.getName() + " started the race");

        // Going over the obstacles
        // Note that here we are not iterating through the collection
        // because that would mean that we need to acquire a lock for it
        // Reminder: Even if a collection is declared as thread safe, iterating always needs locking
        // If we were to lock here, one contender would just complete everything while others are
        // waiting for the lock
        for(int i = 0; i < obstacles.size(); ++i){
            contender.attemptObstacle(obstacles.get(i));
        }

        synchronized (currentPlacement) {
            int placement = currentPlacement.getAndIncrement();
            // Sending a notification for the status watcher thread
            // to check if everyone has finished the race or not
            currentPlacement.notify();
            return contender.getName() + " finished the race at " + placement + ". place";
        }
    }

    /**
     * Everytime a contender finishes a race, currentPlacement gets a notification.
     * In this method a new thread is started where we are waiting on currentPlacement
     * and keep waiting on it until all the contenders finished the race.
     * After that we are shutting down the executor
     */
    private static void startStatusCheckerThread(){
        new Thread(() -> {
            synchronized (currentPlacement){
                // Avoid spurious wake up issues and to only proceed once everyone finished the race
                while(currentPlacement.get() != NUMBER_OF_CONTENDERS + 1){
                    try {
                        // notify() counterpart is in startRace() when someone finishes
                        currentPlacement.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                System.out.println("Shutting down simulation");
                // Give some time for the last message to be printed out before shutting down the executor
                executor.awaitTermination(DELAY_BEFORE_SHUTTING_DOWN_MSEC, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Patience ran out, forcefully shutting down executor
                executor.shutdownNow();
            }

        }).start();
    }

}
