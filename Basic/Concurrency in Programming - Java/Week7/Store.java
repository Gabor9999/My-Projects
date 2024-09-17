package concurent.labs.solution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represent a store that sells shirts.
 * Each store will put their profit onto the original price and sell it for that amount.
 *
 */
public class Store {

    private final String name;
    private final int profit;
    private final TeamShirtFactory factory;
    private final Map<String, TeamShirt> inventory = new ConcurrentHashMap<>();

    public Store(String name, int profit, TeamShirtFactory factory, String... interestedShirts){
        this.name = name;
        this.profit = profit;
        this.factory = factory;

        for(String team : interestedShirts){
            inventory.put(team, factory.getShirt(team));
        }

        factory.subscribe(this::reactToMarketChange);

    }

    /**
     * Runs every time a price change happens in the factory.
     * EVERY. TIME.
     * So make sure to only do anything when that change affects any of the sold shirts.
     *
     * @param teamName
     */
    public void reactToMarketChange(String teamName){
        if(inventory.containsKey(teamName)){
            TeamShirt newShirt = factory.getShirt(teamName);
            newShirt.updatePrice(profit);
            inventory.put(teamName, newShirt);
            System.out.println(name + " is now selling " + newShirt.getProductName()
                    + " shirts for " + newShirt.getPrice());
        }
    }
}
