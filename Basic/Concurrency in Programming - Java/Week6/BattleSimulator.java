package concurent.labs.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulates a battle between 2 armies - one is defending, one is attacking
 * The defender army has a castle to defend, attackers want to destroy that castle
 *
 * Always attackers are the ones taking the first move.
 * If there are defenders left, they will fight them, if none, they will attack the castle
 * Defenders are stronger, but lower in numbers, attackers are weaker (since they always make the first move)
 * but more in numbers
 */
public class BattleSimulator {

    // Constants
    private static final int MAX_NUMBER_OF_THREADS = 100;
    private static final int MAX_NUMBER_OF_ATTACKERS = 100;
    private static final int MAX_NUMBER_OF_DEFENDERS = 92;

    // Castle health and statistics
    private static final AtomicInteger CASTLE_HEALTH = new AtomicInteger(1000);
    private static final AtomicInteger defenderCounter = new AtomicInteger(0);
    private static final AtomicInteger attackerCounter = new AtomicInteger(0);

    // Executors
    private static final ExecutorService cleanupExecutor = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
    private static final ExecutorService simulationExecutor = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
    private static final ScheduledExecutorService soldierCreatorExecutor = Executors.newScheduledThreadPool(2);
    private static final ScheduledExecutorService statusWatcherExecutor = Executors.newScheduledThreadPool(2);

    // Lists for soldiers
    private static final List<Defender> defenders = Collections.synchronizedList(new ArrayList<>());
    private static final List<Attacker> attackers = Collections.synchronizedList(new ArrayList<>());

    // Controls the simulation
    private static AtomicBoolean hasStarted = new AtomicBoolean(false);

    public static void main(String[] args) {
        // Create attackers and defenders every second in the beginning
        soldierCreatorExecutor.scheduleAtFixedRate(() -> createSoldier(false), 0, 1, TimeUnit.SECONDS );
        soldierCreatorExecutor.scheduleAtFixedRate(() -> createSoldier(true), 0, 1, TimeUnit.SECONDS );

        // Start the simulation once we have a couple of soldiers (like 5 on each side)
        sleepForMsec(5000);
        hasStarted.set(true);

        // Logs the status every 5 seconds
        statusWatcherExecutor.scheduleAtFixedRate(() -> logStatus(), 0, 5, TimeUnit.SECONDS );

        // Checks if the game should end every second
        statusWatcherExecutor.scheduleAtFixedRate(() -> endGameIfNeeded(), 0, 1, TimeUnit.SECONDS );
    }

    /**
     * Creates a specific type of soldier.
     * 1, Create the specific soldier
     * 2, Add it to the dedicated list of that type
     * 3, Set the cleanup action for it
     * 4, Set the simulation via processAttack for attackers
     *
     * If the max number of soldiers is reached, executor will shut down
     *
     * @param isAttacker If the soldier should be an attacker (true) or defender (false)
     */
    public static void createSoldier(final boolean isAttacker){
        if(isAttacker){
            if(attackerCounter.get() < MAX_NUMBER_OF_ATTACKERS){
                Attacker attacker = new Attacker();
                attackers.add(attacker);
                attackerCounter.incrementAndGet();
                cleanupExecutor.submit(() -> cleanupAction(attacker));

                simulationExecutor.submit(() -> processAttack(attacker));
            }
        } else {
            if(defenderCounter.get() < MAX_NUMBER_OF_DEFENDERS){
                Defender defender = new Defender();
                defenders.add(defender);
                defenderCounter.incrementAndGet();
                cleanupExecutor.submit(() -> cleanupAction(defender));
            }
        }

        // If the max number of soldiers is reached, executor will shut down
        if( attackerCounter.get() == MAX_NUMBER_OF_ATTACKERS &&
                defenderCounter.get() == MAX_NUMBER_OF_DEFENDERS){
            soldierCreatorExecutor.shutdownNow();
            System.out.println("Shutting down soldier creator executor");
        }
    }

    /**
     * Handles the attack sequences.
     * 1, If the simulation has started
     * 1a, And there are still defenders left, choose one and set each other as target
     * 1b, If there are no defenders left, attack the castle
     * 2, If the attacker is free of any defenders, and there are none left, attack castle
     *    otherwise attack his opponent
     * 3, Wait 100 msec
     * 4, Repeat till the attacker is dead or the castle is destroyed
     *
     * @param attacker The attacker who begins the attack
     */
    public static void processAttack(Attacker attacker){
        // Still alive and castle is standing still
        while(!attacker.isDead() && CASTLE_HEALTH.get() > 0) {
            // Simulation has started
            if(hasStarted.get()) {
                synchronized (defenders) {
                    // Find the first free defender if the attacker is free and there are defenders still
                    if (!defenders.isEmpty() && attacker.isFree()) {
                        // Using stream and optional to make it easier
                        // Stream: filters the list of defenders based on availability (isFree)
                        //         and finds the first one it applies to
                        // Optional: The stream's findFirst returns with an Optional.
                        //           An optional is basically stating that there might or might
                        //           not be a value of a given type (Defender)
                        //           This is a more elegant way to avoid constant null checks in the code
                        Optional<Defender> def = defenders.stream().filter(Defender::isFree).findFirst();
                        // Optionals have multiple methods to make our lives easier to deal with these
                        // types.
                        // Right now is ifPresent, which requires a function that takes a Defender type
                        // since the Optional was created for Defender types.
                        // If the Optional holds a non-null Defender, the function will run, otherwise
                        // nothing happens
                        def.ifPresent(d -> {
                            d.setOpponent(attacker);
                            attacker.setOpponent(d);
                        });


                        // Couple more useful Optional methods
                        // def.isPresent() : returns a boolean, true if it has a non-null Defender
                        //                   false otherwise
                        // def.orElse(Defender d) : if the Optional does not hold a Defender value
                        //                          in itself, this method gives us the opportunity
                        //                          to create a new Defender and return that one
                        //                          instead of null
                        // def.ifPresentOrElse(d -> { something with the defender}, () -> { otherwise }) :
                        //                   If the defender is present in the Optional, it runs the first
                        //                   parameter, which is a function that requires a defender type
                        //                   parameter (d).
                        //                   If the defender is not present (or null) ins the Optional
                        //                   it runs the second parameter, which is a runnable, more
                        //                   precisely a function that requires no parameters.
                        //                   This will be executed if there are no Defenders in the
                        //                   Optional to work with (so it's not present, aka null)
                    }
                }

                // If the attacker has no opponent, attack castle, otherwise the opponent
                if (attacker.isFree()) {
                    CASTLE_HEALTH.set(CASTLE_HEALTH.get() - attacker.getAttackPoint());
                } else {
                    attacker.attackOpponent();
                }
            }
            sleepForMsec(100);
        }
    }

    /**
     * Cleanup action for the Attacker type Soldiers.
     * In the Soldier class whenever isDead is set to true, it sends a notify on itself
     * This method waits for that notify and once it receives it, removes the
     * Attacker from the list of attackers
     * @param attacker The attacker being monitored
     */
    private static void cleanupAction(Attacker attacker){
        try {
            synchronized (attacker) {
                attacker.wait();
                attackers.remove(attacker);
//                            System.out.println("Attacker " + attacker.getId() + " has died");
            }
        } catch (InterruptedException e) {
            // Not best practice to leave it empty
        }
    }

    /**
     * Cleanup action for the Defender type Soldiers.
     * In the Soldier class whenever isDead is set to true, it sends a notify on itself
     * This method waits for that notify and once it receives it, removes the
     * Defender from the list of defenders
     * @param defender The defender being monitored
     */
    private static void cleanupAction(Defender defender){
        try {
            synchronized (defender) {
                defender.wait();
                defenders.remove(defender);
//                            System.out.println("Defender " + defender.getId() + " has died");
            }
        } catch (InterruptedException e) {
            // Not best practice to leave it empty
        }
    }

    /**
     * Ends the if the requirements all check out.
     * 1, If castle health is <= 0, attackers won
     * 2, If there are no attackers and all the attackers have been produced, defenders won
     * 3, Either way, logs final status and shuts down the executors
     */
    private static void endGameIfNeeded(){
        boolean finished = false;
        if(CASTLE_HEALTH.get() <= 0){
            System.out.println("Attackers won!");
            finished = true;
        }
        if(attackers.isEmpty() && attackerCounter.get() == MAX_NUMBER_OF_ATTACKERS){
            System.out.println("Defenders won!");
            finished = true;
        }
        if(finished){
            logStatus();
            cleanupExecutor.shutdownNow();
            simulationExecutor.shutdownNow();
            statusWatcherExecutor.shutdownNow();
            soldierCreatorExecutor.shutdownNow();
        }
    }

    /**
     * Logs the current status of the game
     */
    private static void logStatus(){
        System.out.println("Castle health is at " + CASTLE_HEALTH.get());
        System.out.println("Number of attackers " + attackers.size());
        System.out.println("Number of defenders " + defenders.size());
        System.out.println("Created " + defenderCounter.get() + " defenders and " + attackerCounter.get() + " attackers");
        System.out.println("*********************************************");
    }

    /**
     * Sleeps for a given millisec
     * @param msec The amount of millisec to sleep
     */
    private static void sleepForMsec(int msec) {
        try {
            TimeUnit.MILLISECONDS.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}