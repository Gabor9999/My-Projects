package concurent.labs.solution;

/**
 * The implementation of the attacker
 * More in Soldier class
 */
public class Attacker extends Soldier {

    // Since attackers always hit first, they have a bit of disadvantage
    public Attacker(){
        this.maxAttackPoint = 17;
        this.minAttackPoint = 12;
    }
}