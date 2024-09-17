package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Part 1:
 * 5 witchers are doing contracts to get more money.
 * A witcher can only do one contract at a time and no one else can do that contract
 * A contract should be generated every WAIT_TIME_BETWEEN_CONTRACTS_MSEC and there can only be CONTRACT_MAX_NUMBER
 * at the same time
 * Let the simulation run for 30 seconds and shut down the simulation after that
 *
 * Part 2:
 * The simulation should run for as long as a witcher gets GOAL_WITCHER_GOLD_AMOUNT then shut it down
 * The simulation should run for a maximum of 1 minute
 *
 * Part 3:
 * Only one witcher can be in the same zone
 * If another witcher tries to take another contract in that zone, he should wait ZONE_LOCKOUT_WAIT_TIME_MSEC
 * and try again.
 * If the original witcher hasn't finished his contract by that time, look for a new contract
 */
public class Simulation {

    private final static String[] WITCHER_NAMES = "Geralt,Letho,Eskel,Lambert,Vesemir".split(",");
    private final static int CONTRACT_MAX_NUMBER = 5;
    private final static int GOAL_WITCHER_GOLD_AMOUNT = 1000;
    private final static int WAIT_TIME_BETWEEN_CONTRACTS_MSEC = 500;
    private final static int ZONE_LOCKOUT_WAIT_TIME_MSEC = 300;

    private final static AtomicBoolean simulationOver = new AtomicBoolean(false);
    private final static List<Witcher> witchers = new ArrayList<>();
    private final static Queue<Contract> contracts = new LinkedBlockingDeque<>(CONTRACT_MAX_NUMBER);
    private final static ExecutorService witcherExecutor = Executors.newFixedThreadPool(WITCHER_NAMES.length + 1);
    private final static ScheduledExecutorService progressExecutor = Executors.newScheduledThreadPool(2);

    private static Lock[] zoneLocks = new Lock[Zone.getCount()];

    public static void main(String[] args) {

        for(int i = 0; i < Zone.getCount(); ++i){
            zoneLocks[i] = new ReentrantLock();
        }

        for(String name : WITCHER_NAMES){
            witchers.add(new Witcher(name));
        }

        for(int i = 0; i < CONTRACT_MAX_NUMBER; ++i){
            Contract contract = Contract.generateContract();
            contracts.add(contract);
            System.out.println("A new contract is up - " + contract.getInfo());
        }

        for(Witcher witcher : witchers){
            witcherExecutor.submit(() -> startWitcher(witcher));
        }
        witcherExecutor.submit(Simulation::generateContract);
        progressExecutor.scheduleAtFixedRate(Simulation::checkSimulationOver, 0, 500, TimeUnit.MILLISECONDS);

        try {
            witcherExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            simulationOver.set(true);
            witcherExecutor.shutdownNow();
            progressExecutor.shutdownNow();
        }

    }

    /**
     * Generates a contract every WAIT_TIME_BETWEEN_CONTRACTS_MSEC
     * If there are already CONTRACT_MAX_NUMBER contracts waiting to be taken, do nothing with it
     * If the limit allows, add the new contract to the contracts collection
     *
     * Should stop generating contracts if simulation is over (simulationOver flag)
     */
    private static void generateContract(){
        while (!simulationOver.get()){
            Contract contract = Contract.generateContract();
            if(contracts.offer(contract)){
                System.out.println("A new contract is up - " + contract.getInfo());
            }
            try {
                Thread.sleep(WAIT_TIME_BETWEEN_CONTRACTS_MSEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts a witcher, who will look for contracts to take
     * If a witcher takes a contract, do it immediately (witcher.takeContract)
     * After a contract is done, wait WAIT_TIME_BETWEEN_CONTRACTS_MSEC before the new one
     *
     * If the simulation is over, the witcher should not try to pick up new contracts
     * but should finish his existing one, if there is one
     *
     * Part 3:
     * Two witcher can never be in the same zone.
     * If the witcher sees a new contract, but another witcher is doing another contract in that zone, wait
     * ZONE_LOCKOUT_WAIT_TIME_MSEC and try to pick it up again
     * If by that time the other witcher hasn't finished his contract in the zone, try to take on a new one
     * @param witcher The witcher
     */
    private static void startWitcher(final Witcher witcher){
        while(!simulationOver.get()) {
            Contract contract = contracts.poll();
            Lock zoneLock = zoneLocks[contract.getZone().ordinal()];
            try {
                if (zoneLock.tryLock(ZONE_LOCKOUT_WAIT_TIME_MSEC, TimeUnit.MILLISECONDS)){
                    if (!contract.isOwned()) {
                        try {
                            witcher.takeContract(contract);
                        } finally {
                            zoneLock.unlock();
                        }
                        Thread.sleep(WAIT_TIME_BETWEEN_CONTRACTS_MSEC);
                    }
                } else {
                    System.out.println(witcher.getName() + " got tired of waiting for " + contract.getZone().name());
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Part 1:
     * The simulation should end after 30 seconds, using simulationOver flag
     *
     * Part 2:
     * If a witcher has GOAL_WITCHER_GOLD_AMOUNT or more, shut down the simulation using simulationOver flag
     *
     * This check should happen every 500 milliseconds
     */
    private static void checkSimulationOver(){
        if(!simulationOver.get()) {
            for (Witcher witcher : witchers) {
                if (witcher.getGold() >= GOAL_WITCHER_GOLD_AMOUNT) {
                    System.out.println("***** " + witcher.getName() + " reached the set amount first *****");
                    witcherExecutor.shutdown();
                    simulationOver.set(true);
                    break;
                }
            }
        }
    }
}
