package concurent.labs.solution;

/**
 * Class representing a shirt sold by merchants.
 *
 * This can be made immutable, or be mutable. Depends on the implementation for other classes.
 *
 * Now it is mutable for this solution
 */
public class TeamShirt {

    private final String productName;
    private int price; // in HUF for example

    public TeamShirt(String productName, int price){
        this.productName = productName;
        this.price = price;
    }

    public void updatePrice(int modifier){
        this.price += modifier;
    }

    public String getProductName(){
        return this.productName;
    }

    public Integer getPrice(){
        return this.price;
    }

}
