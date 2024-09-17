package concurent.labs.solution;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represent a contender in the spartan race.
 * Each contender has different values in the needed attributes;
 * stamina, strength, agility, run speed, swim speed.
 * Every obstacle requires a different attribute for faster completion
 */
public class Contender {

    private static final int MIN_ATTRIBUTE = 10;
    private static final int MAX_ATTRIBUTE = 50;

    private final String name;
    private final int stamina;
    private final int strength;
    private final int agility;
    private final int runSpeed;
    private final int swimSpeed;

    /**
     * Upon creation the contender gets assigned random values for attributes between
     * MIN_ATTRIBUTE and MAX_ATTRIBUTE, and the name ofc
     * @param name Name of the contender
     */
    public Contender(final String name){
        this.name = name;
        this.stamina = ThreadLocalRandom.current().nextInt(MIN_ATTRIBUTE, MAX_ATTRIBUTE);
        this.strength = ThreadLocalRandom.current().nextInt(MIN_ATTRIBUTE, MAX_ATTRIBUTE);
        this.agility = ThreadLocalRandom.current().nextInt(MIN_ATTRIBUTE, MAX_ATTRIBUTE);
        this.runSpeed = ThreadLocalRandom.current().nextInt(MIN_ATTRIBUTE, MAX_ATTRIBUTE);
        this.swimSpeed = ThreadLocalRandom.current().nextInt(MIN_ATTRIBUTE, MAX_ATTRIBUTE);
        System.out.println("Created contender: " + this.name + " with attributes:\n" +
                "Stamina: " + this.stamina + "\n" +
                "Strength: " + this.strength + "\n" +
                "Agility: " + this.agility + "\n" +
                "Run Speed: " + this.runSpeed + "\n" +
                "Swim Speed: " + this.swimSpeed + "\n");
    }

    public void attemptObstacle(final Obstacle obstacle){
        final int completionTime = obstacle.getCompletionTime(this);
        try {
            Thread.sleep(completionTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " has completed " + obstacle.getName() + " in " + completionTime + " millisecs");
    }

    public String getName() {
        return name;
    }

    public int getStamina() {
        return stamina;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getRunSpeed() {
        return runSpeed;
    }

    public int getSwimSpeed() {
        return swimSpeed;
    }
}
