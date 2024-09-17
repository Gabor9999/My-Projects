package concurent.labs.solution;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A couple of friends decided to visit their favourite pub for a couple of beers.
 * If someone leaves a bad review or someone gets out without paying, the pub will close down.
 *
 */
public class Pub {

    // Parameters of the simulation as constants
    private static final int BEER_COST = 400;
    private static final int ROUNDS = 5;
    private static final int MIN_WAIT_TIME = 700;
    private static final int MAX_WAIT_TIME = 900;

    private static final Bartender bartender = new Bartender();
    private static final List<Customer> customers = new LinkedList<>();
    private static final List<Thread> customerThreads = new LinkedList<>();

    public static void main(String[] args) {

        customers.add(new Customer("Barney"));
        customers.add(new Customer("Lily"));
        customers.add(new Customer("Marshall"));
        customers.add(new Customer("Ted"));
        customers.add(new Customer("Robin"));

        // Customers start doing customer stuff
        // They don't like waiting for each other
        // so this needs to be done separately
        for(Customer c : customers){
            Thread customerThread = new Thread(() -> getDrunk(c));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        // We wait until everyone finished drinking
        for(Thread t : customerThreads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Everyone finished drinking");

        // Self check - depending on the output you can determine whether
        // you caused the pub to shut down or be the best place in town
        if(bartender.getProfit() == (BEER_COST * customers.size() * ROUNDS) &&
           bartender.getBeersCreated() == (customers.size() * ROUNDS)){
            System.out.println("OK - Business is better than ever, everyone is happy");
        } else {
            System.out.println("NOT OK - Someone took off without getting properly drunk or " +
                    "without paying for it; business collapsed");
        }
    }

    /**
     * Each customer will order a certain amount of beers.
     *
     * @param customer The customer
     */
    private static void getDrunk(final Customer customer){
        for(int i = 0; i < ROUNDS; ++i){
            orderDrink(customer);
            sleepForRandom();
        }
    }

    /**
     * A customer orders a beer. There are two steps to do this;
     * 1, The beer needs to be prepared for serving
     * 2, The customer needs to pay for it
     *
     * The customer can pay while the bartender prepares the beer.
     * Once the beer is prepared and payment is made, we can finally
     * acknowledge that the order was successful
     *
     * @param customer The customer (obviously)
     */
    private static void orderDrink(final Customer customer){
        // Separate thread for both actions so they can happen parallel
        Thread beerCreationThread = new Thread( () -> bartender.createBeer(customer.getName()));
        Thread paymentThread = new Thread( () -> customer.payForBeer(bartender, BEER_COST));

        beerCreationThread.start();
        paymentThread.start();

        // Waiting for the actions to finish
        try {
            beerCreationThread.join();
            paymentThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(customer.getName() + " finished ordering a beer");
    }

    /**
     * Sleeping idle on the given thread for a random amount of milliseconds.
     * If something interrupts this sleep, log the exception
     */
    public static void sleepForRandom(){
        try {
            // Thread individual random number generation
            Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_WAIT_TIME, MAX_WAIT_TIME));
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " got interrupted");
            e.printStackTrace();
        }
    }
}
