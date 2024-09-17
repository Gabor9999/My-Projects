package concurent.labs.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates vaccination - a couple of clients try to
 * get their covid vaccinations but the doctor can only
 * give it to one at a time. Others will need to wait
 * until its their turn.
 */
public class VaccinationSimulation {

    private static final List<Client> clients = new ArrayList<>();
    private static final Doctor doc = new Doctor();

    public static void main(String[] args) {
        clients.add(new Client("Stan"));
        clients.add(new Client("Kyle"));
        clients.add(new Client("Kenny"));
        clients.add(new Client("Cartman"));
        clients.add(new Client("Butters"));

        // Each client will try to get vaccination on a separate thread
        clients.forEach(c -> {
            new Thread(() -> {
                c.tryToGetVaccination(doc);
            }).start();
        });
    }
}
