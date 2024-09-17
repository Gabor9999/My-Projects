package concurent.labs.solution;

import java.util.List;
import java.util.Optional;

/**
 * Implementation for tank role
 * Tanks attack the boss
 */
public class TankUnit extends Unit {

    private static final int MAX_HEALTH = 500;
    private static final int MAX_MANA = 0;
    private static final int MAX_DMG = 20;
    private static final int MIN_DMG = 10;
    private static final int ACTION_INTERVAL = 100;

    public TankUnit(final String name) {
        super(name, Role.TANK, MAX_HEALTH, MAX_MANA, MAX_DMG, MIN_DMG, ACTION_INTERVAL);
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
            Optional<Unit> target = units.stream()
                    .filter(u -> u.getRole() == Role.BOSS && !u.isDead()).findFirst();
            return target.isPresent() ? target.get() : null;
        }
    }
}
