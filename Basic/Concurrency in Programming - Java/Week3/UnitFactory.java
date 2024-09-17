package concurent.labs.solution;

/**
 * Factory class for creating a specific unit.
 * Tanks, Dps', healers or bosses.
 */
public class UnitFactory {

    public static TankUnit createTank(final String name){
        return new TankUnit(name);
    }

    public static DpsUnit createDps(final String name){
        return new DpsUnit(name);
    }

    public static HealerUnit createHealer(final String name){
        return new HealerUnit(name);
    }

    public static BossUnit createBoss(final String name){
        return new BossUnit(name);
    }

}
