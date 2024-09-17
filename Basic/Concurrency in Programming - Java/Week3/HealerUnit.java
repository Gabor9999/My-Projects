package concurent.labs.solution;

import java.util.List;
import java.util.Optional;

/**
 * Implementation for healer unit
 * Healers heal their allies (dps and tank) using mana which regenerates during the fight
 * They first heal tanks, then anyone who doesn't have max health
 */
public class HealerUnit extends Unit {

    private static final int MAX_HEALTH = 250;
    private static final int MAX_MANA = 150;
    private static final int MAX_DMG = 60;
    private static final int MIN_DMG = 40;
    private static final int ACTION_INTERVAL = 70;

    private static final int MANA_REGEN_INTERVAL = 35;
    private static final int MANA_REGEN_AMOUNT = 5;
    private static final int HEAL_MANA_COST = 10;

    public HealerUnit(final String name) {
        super(name, Role.HEALER, MAX_HEALTH, MAX_MANA, MAX_DMG, MIN_DMG, ACTION_INTERVAL);
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
            if (!target.isDead()) {
                if(this.getMana() > HEAL_MANA_COST) {
                    this.loseMana(HEAL_MANA_COST);
                    target.gainHealth(this.getDamage());
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
                    .filter(u -> u.getRole() == Role.TANK && !u.isDead()).findFirst();
            // if the tank is dead then heal a hero not on full health
            if(!target.isPresent()){
                target = units.stream()
                        .filter(u -> u.getRole() != Role.BOSS && !u.isDead() && !u.isMaxHealth()).findFirst();
            }
            return target.isPresent() ? target.get() : null;
        }
    }
}
