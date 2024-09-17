package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulating a boss fight - there is one boss and five players
 * will try to take him down.
 * Among the five players there is one healer, who heals all
 * who has lost health, a tank, who mainly takes damage and
 * three "dps", who will provide additional damage to the boss.
 *
 * You know, like in WoW.
 *
 */
public class Simulation {

    private static final List<Unit> unitList = new ArrayList<>();
    private static final List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {

        // create units
        unitList.add(UnitFactory.createBoss("Lich King"));
        unitList.add(UnitFactory.createTank("Varian"));
        unitList.add(UnitFactory.createHealer("Anduin"));
        unitList.add(UnitFactory.createDps("Thrall"));
        unitList.add(UnitFactory.createDps("Sylvanas"));
        unitList.add(UnitFactory.createDps("Jaina"));

        // start thread for each unit
        unitList.forEach(u -> {
            Thread t = new Thread(() -> {
                u.performAction(unitList);
            });
            threads.add(t);
            t.start();
        });

        // wait until threads stop
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Simulation over - Unit stats:");
        unitList.forEach(u ->
                System.out.println(u.getName() + " : " + u.getRole() + " with " + u.getHealth() + " health"));
    }

}
