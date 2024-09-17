package concurent.labs.solution;

import java.util.List;
import java.util.Optional;

/**
 * Implementation for the Boss role
 * Bosses have large health and deal big damage
 * They first target tanks then healers then dps
 */
public class BossUnit extends Unit {

    private static final int MAX_HEALTH = 5000;
    private static final int MAX_MANA = 0;
    private static final int MAX_DMG = 70;
    private static final int MIN_DMG = 50;
    private static final int ACTION_INTERVAL = 100;

    public BossUnit(final String name) {
        super(name, Role.BOSS, MAX_HEALTH, MAX_MANA, MAX_DMG, MIN_DMG, ACTION_INTERVAL);
    }

    @Override
    public void performAction(List<Unit> units) {
        Unit target = chooseTarget(units);
        while(target != null && !this.isDead()) {
            if(!target.isDead()) {
                target.loseHealth(this.getDamage());
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
            // first it will attack the tank
            Optional<Unit> target = units.stream()
                    .filter(u -> u.getRole() == Role.TANK && !u.isDead()).findFirst();
            // if the tank is dead then attack the healer
            if(!target.isPresent()){
                target = units.stream()
                        .filter(u -> u.getRole() == Role.HEALER && !u.isDead()).findFirst();
            }
            // if the healer is dead then attack a dps
            if(!target.isPresent()){
                target = units.stream()
                        .filter(u -> u.getRole() == Role.DPS && !u.isDead()).findFirst();
            }
            return target.isPresent() ? target.get() : null;
        }
    }
}
