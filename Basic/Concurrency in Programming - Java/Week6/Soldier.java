package concurent.labs.solution;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represent a soldier taking place in the battle.
 * A soldier has his own id, can attack one opponent at a time,
 * has a range of damage, has health, and a flag to indicate death.
 *
 * Note that this is an abstract class.
 * All the essential methods for a soldier are implemented here,
 * but the types of the soldiers and in their respected classes (Attacker, Defender)
 *
 * Battle goes like this:
 * 1, Attacker takes up a defender to attack
 * 2, Attacker damages defender
 * 3, Defender damages attacker back (provided it's still alive)
 * 4a,  If the soldier is an attacker and the opponent died, takes up a new defender as opponent
 * 4b,  If the soldier is a defender and the opponent died, waits until an attacker target it
 * 5, Repeat (if soldier is attacker, and no defenders are left, it will attack the castle)
 */
public abstract class Soldier {

    // static, so it is shared between all soldiers - this way no 2 soldier will have the same id
    protected static int idGenerator = 0;
    protected int id = idGenerator++;
    protected Soldier opponent;
    protected int maxAttackPoint = 20;
    protected int minAttackPoint = 15;
    protected int health = 100;
    protected boolean isDead = false;

    /**
     * Attacks an opponent.
     * If this soldier is not dead, it can try to attack to opponent
     * The opponent loses health equal to a randomly generated value between attack points
     * If the opponent is dead, after the strike, this soldier will look for another opponent
     * If the opponent is not dead, the opponent will have a chance to defend/counterattack
     */
    public void attackOpponent(){
        if(!isDead) opponent.loseHealth(getAttackPoint());
        if(opponent.isDead()) {
            setOpponent(null);
        } else {
            opponent.defend();
        }
    }

    /**
     * If this soldier is not dead, attack the opponent
     * If the opponent died, sets it to null so that it can take on another soldier
     */
    public void defend(){
        if(!isDead) opponent.loseHealth(getAttackPoint());
        if(opponent.isDead()) {
            setOpponent(null);
        }
    }

    /**
     * Losing health.
     * If the soldier drops that due to the damage (health <= 0),
     * it will set isDead to true and will signal on itself via notify.
     * @param amount Health loss
     */
    public void loseHealth(int amount){
        health -= amount;
        if(health <= 0) {
            isDead = true;

            synchronized (this) {
                this.notify();
            }
        }
    }

    /**
     * Id of the soldier
     * @return ^
     */
    public int getId(){
        return id;
    }

    /**
     * Returns the current opponent of the soldier
     * @return ^
     */
    public Soldier getOpponent() {
        return opponent;
    }

    /**
     * Sets a new opponent for the soldier
     * @param opponent The new opponent
     */
    public void setOpponent(Soldier opponent) {
        this.opponent = opponent;
    }

    /**
     * Return if the soldier is free to take on another opponent
     * @return ^
     */
    public boolean isFree(){
        return Objects.isNull(opponent);
    }

    /**
     * Generates a random value between the min and max attack points of the soldier
     * @return The generated attack point
     */
    public int getAttackPoint() {
        return ThreadLocalRandom.current().nextInt(minAttackPoint, maxAttackPoint);
    }

    /**
     * If the soldier is dead
     * @return ^
     */
    public boolean isDead() {
        return isDead;
    }

}