package concurent.labs.solution;

import java.util.function.Function;

/**
 * Represents an obstacle on the race track.
 * Has a name, and a complexity.
 * Each obstacle requires a specific attribute of the contender,
 * the higher the needed attribute, the faster the contender
 * will be able to complete the obstacle
 *
 */
public class Obstacle {

    private static final int BASE_COMPLEXITY = 15000;

    private final String name;
    private final int complexityMultiplier;
    private final Function<Contender, Integer> contenderAttributeGetter;


    /**
     * Constructor for an obstacle
     * @param name Name of the obstacle
     * @param complexityMultiplier Applied on the BASE_COMPLEXITY to determine actual complexity
     * @param contenderAttributeGetter Function to determine which attribute is needed for completion
     */
    public Obstacle(final String name, final int complexityMultiplier,
                    final Function<Contender, Integer> contenderAttributeGetter){
        this.name = name;
        this.complexityMultiplier = complexityMultiplier;
        this.contenderAttributeGetter = contenderAttributeGetter;
    }

    /**
     * Calculates the time the contender needs to overcome this obstacle
     * @param contender The contender attempting the obstacle
     * @return Time needed for the contender to complete the obstacle
     */
    public synchronized int getCompletionTime(final Contender contender){
        // Note that this method is synchronized - it's possible that multiple contenders
        // will try to get the completion time at once
        int divider = contenderAttributeGetter.apply(contender);
        return (BASE_COMPLEXITY * complexityMultiplier) / divider;
    }

    /**
     * Returns name of the obstacle
     * @return The name
     */
    public String getName(){
        return this.name;
    }
}
