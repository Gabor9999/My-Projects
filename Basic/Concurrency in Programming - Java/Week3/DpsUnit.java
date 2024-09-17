package concurent.labs.solution;

import java.util.List;
import java.util.Optional;

/**
 * Implementation for Dps role.
 * Dps' use mana to attack which regenerates for them during the fight
 * They only attack the boss
 */
public class DpsUnit extends Unit {

    private static final int MAX_HEALTH = 300;
    private static final int MAX_MANA = 200;
    private static final int MAX_DMG = 70;
    private static final int MIN_DMG = 60;
    private static final int ACTION_INTERVAL = 80;

    private static final int MANA_REGEN_INTERVAL = 60;
    private static final int MANA_REGEN_AMOUNT = 5;
    private static final int ATTACK_MANA_COST = 10;

    public DpsUnit(final String name) {
        super(name, Role.DPS, MAX_HEALTH, MAX_MANA, MAX_DMG, MIN_DMG, ACTION_INTERVAL);
        // starts a new thread for mana regeneration until fight is over or hero is dead
        new Thread(() -> {
            while(!this.isCombatOver && !this.isDead()){
                this.gainMana(MANA_REGEN_AMOUNT);
                sleepForMsec(MANA_REGEN_INTERVAL);
            }
        }).start();
    }

    @Override
    public void performAction(List<Unit> units) {
        Unit target = chooseTarget(units);
        while(target != null && !this.isDead()) {
            if(!target.isDead()) {
                if(this.getMana() >= ATTACK_MANA_COST) {
                    this.loseMana(ATTACK_MANA_COST);
                    target.loseHealth(this.getDamage());
                }
            } else {
                target = chooseTarget(units);
            }
            sleepForMsec(ACTION_INTERVAL);
        }
        stopCombat();
    }

    @Override
    protected Unit chooseTarget(List<Unit> units) {
        synchronized ( units ){
            Optional<Unit> target = units.stream()
                    .filter(u -> u.getRole() == Role.BOSS && !u.isDead()).findFirst();
            return target.isPresent() ? target.get() : null;
        }
    }
}
