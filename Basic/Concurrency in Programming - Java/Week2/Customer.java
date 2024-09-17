package concurent.labs.solution;

/**
 * Represents a customer in the Pub.
 * They have a name, they have money, they want to drink.
 *
 */
public class Customer {

    private final String name;

    /**
     * Creating a new customer.
     * No one should exist without a name.
     *
     * @param name Name of this customer
     */
    public Customer(final String name){
        this.name = name;
    }

    /**
     * Initiates payment for the ordered beer.
     *
     * @param bartender The bartender preparing the beer
     * @param price Price of the beer
     */
    public void payForBeer(final Bartender bartender, final int price){
        System.out.println(name + " is trying to pay the bartender for the beer");
        bartender.receivePayment(this.name, price);
    }

    /**
     * Returns the name of the customer.
     *
     * @return Name of this customer
     */
    public String getName(){
        return this.name;
    }

}
