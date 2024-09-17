package concurent.labs.solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * This is the factory that handles the price changes on team shirts.
 * Each store will subscribe to the changes and change theirs accordingly.
 */
public class TeamShirtFactory {

    private static final int PRINT_IN_ROUND = 5;
    private static final int PRICING_ROUNDS = PRINT_IN_ROUND * 3;
    private static final int SHIRT_PRICE_START_VALUE = 10_000;
    private static final int MAX_PRICE_CHANGE = 500;
    private static final int MIN_WAIT_TIME_MS = 1000;
    private static final int MAX_WAIT_TIME_MS = 2000;

    private final Map<String, TeamShirt> basePrices = new ConcurrentHashMap<>();
    private final List<Consumer<String>> subscribers = new LinkedList<>();

    /**
     * Sets up the inventory
     */
    public void setupFactory(){
        Teams.listOfTeams.forEach(team -> {
            String teamName = team;
            basePrices.put(teamName, new TeamShirt(teamName, SHIRT_PRICE_START_VALUE));
        });
    }

    /**
     * This method is responsible for invoking the price updates and status output.
     * Every 5th round it will print the current prices for the shirts to help you test the simulation.
     */
    public void start(){
        new Thread(() -> {
            int round = 0;
            while(true) {
                if(round % PRINT_IN_ROUND != 0) {
                    updatePrice();
                    try {
                        Thread.sleep(ThreadLocalRandom.current()
                                .nextInt(MAX_WAIT_TIME_MS - MIN_WAIT_TIME_MS + 1) + MAX_WAIT_TIME_MS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    printCurrentPrices();
                    if(round == PRICING_ROUNDS) {
                        break;
                    }
                }
                round++;
            }
        }).start();
    }

    /**
     * Returns a shirt from the factory.
     * Note that the returned shirt is immutable by the invoking method
     *
     * @param team The team's name
     * @return The TeamShirt object which has the same name as the argument
     */
    public TeamShirt getShirt(String team){
        TeamShirt temp = basePrices.get(team);
        return temp != null ? new TeamShirt(temp.getProductName(), temp.getPrice()) : null;
    }

    /**
     * Stores use this to subscribe to a price change.
     *
     * @param subscriber A runnable method which handles the store's reaction to the price change
     */
    public void subscribe(Consumer<String> subscriber){
        this.subscribers.add(subscriber);
    }

    /**
     * Print out the state of the world
     */
    private void printCurrentPrices(){
        System.out.println("**********************************");
        basePrices.forEach((k,v) -> System.out.println(k + " shirts: " + v.getPrice()));
        System.out.println("**********************************");
    }

    /**
     * Updates price for the shirts.
     */
    private void updatePrice(){

        for(String teamName : Teams.listOfTeams){
            new Thread(() -> {
                final int priceChange = ThreadLocalRandom.current().nextInt(MAX_PRICE_CHANGE)
                        * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1);

                basePrices.get(teamName).updatePrice(priceChange);
                System.out.println("Factory updated price for " + teamName + " shirts by " + priceChange);
                for(Consumer consumer : subscribers){
                    consumer.accept(teamName);
                }
            }).start();
        }
    }
}
