package concurent.labs.solution;

/**
 * The bartender in the pub.
 * Main responsibility is to create beers and receive payments.
 *
 */
public class Bartender {

    // Parameters as constants
    private static final int BEER_TAP_TIME_MSEC = 500;
    private static final int PAYMENT_TIME_MSEC = 350;

    // For self-check
    private Integer profit = 0;
    private Integer beersCreated = 0;

    /**
     * Bartender begins to prepare the beer.
     * This can happen parallel with receiving payment.
     *
     * @param customerName Name of the customer ordering the beer
     */
    public void createBeer(final String customerName){
        System.out.println("Bartender is tapping the beer for " + customerName);

        try {
            Thread.sleep(BEER_TAP_TIME_MSEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This is called a synchronized block;
        // It creates a lock on a specific object (beersCreated)
        // If there is a lock on an object, only one thread can access it at a time
        // This is needed because we have multiple threads, all of which will try
        // to modify the variable
        // If a thread moves out of the block, the object will be unlocked again
        synchronized (beersCreated){
            beersCreated++;
        }
    }

    /**
     * Bartender receives payment from a customer.
     * This can happen parallel to creating beer.
     *
     * @param customerName Name of the paying customer
     * @param payment Amount of payment
     */
    public void receivePayment(final String customerName, final int payment){
        try {
            Thread.sleep(PAYMENT_TIME_MSEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This is called a synchronized block;
        // It creates a lock on a specific object (profit)
        // If there is a lock on an object, only one thread can access it at a time
        // This is needed because we have multiple threads, all of which will try
        // to modify the variable
        // If a thread moves out of the block, the object will be unlocked again
        synchronized (profit){
            profit += payment;
        }

        System.out.println("Bartender received a payment of " + payment + " from " + customerName);
    }

    /**
     * Gets how many beers were created during the day
     * @return Number of beers created
     */
    public int getBeersCreated() {
        // Since we only invoke it once all the threads finished working,
        // this synchronized block (GOTO line 27) is not needed here
        // Im just showing that you can return in it
        synchronized (beersCreated){
            return beersCreated;
        }
    }

    /**
     * Gets the profit made during the day
     * @return The profit
     */
    public int getProfit() {
        // Since we only invoke it once all the threads finished working,
        // this synchronized block (GOTO line 45) is not needed here
        // Im just showing that you can return in it
        synchronized (profit){
            return profit;
        }
    }


}
