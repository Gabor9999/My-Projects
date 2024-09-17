package concurent.labs.solution;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a unit.
 * Units can have a role, health and mana.
 * Health is used to determine if the Unit is alive or not.
 * Mana is used to determine if a Unit can cast spells or not.
 * Role is according to the enum with the same name.
 */
public abstract class Unit {

    private final String name;
    private final Role role;
    private final Integer MAX_HEALTH;
    private final Integer MAX_MANA;
    private final Integer DMG_MAX;
    private final Integer DMG_MIN;
    private final Integer ACTION_INTERVAL;
    private Integer health;
    private Integer mana;
    private boolean isDead;
    protected boolean isCombatOver;

    // used to lock health and mana individually
    private final Object manaLock = new Object();
    private final Object healthLock = new Object();

    public Unit(final String name, final Role role, final int maxHealth,
                final int maxMana, final int dmgMax, final int dmgMin, final int actionInterval){
        this.name = name;
        this.role = role;
        this.MAX_HEALTH = maxHealth;
        this.MAX_MANA = maxMana;
        this.DMG_MAX = dmgMax;
        this.DMG_MIN = dmgMin;
        this.ACTION_INTERVAL = actionInterval;
        this.isDead = false;
        this.isCombatOver = false;
        this.health = maxHealth;
        this.mana = maxMana;
    }

    /**
     * Performs the individual action of a unit
     */
    public abstract void performAction(List<Unit> units);

    /**
     * Chooses the appropriate target
     * @param units
     * @return
     */
    protected abstract Unit chooseTarget(List<Unit> units);

    /**
     * Used to remove health from the unit
     * Needs to be synchronized to avoid interruption with heal.
     * @param damage The health lost
     */
    protected void loseHealth(final int damage){
        synchronized ( healthLock ) {
            this.health -= damage;
            if (this.health <= 0) {
                this.isDead = true;
                System.out.println(this.name + " has died");
                return;
            }

            System.out.println(this.name + " suffered " + damage + " damage: " +
                    this.health + "/" + this.MAX_HEALTH);
        }

    }

    /**
     * Used to add health to the unit
     * Needs to be synchronized to avoid interruption with damage
     * @param heal The health gained
     */
    protected void gainHealth(final int heal){
        synchronized ( healthLock ) {
            // cannot heal a dead character
            if (this.health <= 0) {
                this.isDead = true;
                System.out.println(this.name + " died instead of getting healed");
                return;
            }

            // if the heal would give more health than max, stick to max
            if (this.health + heal > this.MAX_HEALTH) {
                this.health = this.MAX_HEALTH;
            }

            System.out.println(this.name + " gained " + heal + " health: " +
                    this.health + "/" + this.MAX_HEALTH);
        }

    }

    /**
     * Used to remove mana upon casting from the unit
     * @param cost The cost of the cast
     */
    protected void loseMana(final int cost){
        synchronized ( manaLock ) {
            if (this.mana - cost < 0) {
                this.mana = 0;
            } else {
                this.mana -= cost;
            }
            System.out.println(this.name + " lost " + cost + " mana: " +
                    this.mana + "/" + this.MAX_MANA);
        }

    }

    /**
     * Used to regenerate mana for the unit
     * @param regen The amount of mana regenerated
     */
    protected void gainMana(final int regen){
        synchronized ( manaLock ) {
            if(this.mana + regen > this.MAX_MANA){
                this.mana = this.MAX_MANA;
            } else {
                this.mana += regen;
            }
            System.out.println(this.name + " gained " + regen + " mana: " +
                    this.mana + "/" + this.MAX_MANA);
        }

    }

    /**
     * Name of the unit
     * @return name of the unit
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the role of the unit
     * @return Role of the unit
     */
    public Role getRole(){
        return this.role;
    }

    /**
     * Returns current health of the unit
     * @return The health of the unit
     */
    public int getHealth(){
        synchronized (healthLock){
            return this.health;
        }
    }

    /**
     * Returns if the unit is on max health or not
     * @return If max health or not
     */
    public boolean isMaxHealth(){
        synchronized (healthLock){
            return this.health == this.MAX_HEALTH;
        }
    }

    /**
     * Query if the unit is dead
     * @return If its dead or not
     */
    public boolean isDead(){
        return this.isDead;
    }

    /**
     * Sets the combat to be over
     */
    protected void stopCombat(){
        this.isCombatOver = true;
    }

    /**
     * Returns the mana of the unit
     * @return Mana of the unit
     */
    protected int getMana(){
        synchronized (manaLock){
            return this.mana;
        }
    }

    /**
     * Calculates randomly the damage to be dealt
     * This is healing in case of healers
     * @return The damage/heal
     */
    protected int getDamage(){
        return ThreadLocalRandom.current().nextInt(this.DMG_MIN, this.DMG_MAX);
    }

    /**
     * Sleeps for a given time
     * @param msec Time to sleep in msec
     */
    protected void sleepForMsec(final int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleeps between a range
     * @param upperLimit Upper limit of the range
     * @param lowerLimit Lower limit of the range
     */
    protected void sleepForRandom(final int upperLimit, final int lowerLimit){
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(lowerLimit, upperLimit));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
