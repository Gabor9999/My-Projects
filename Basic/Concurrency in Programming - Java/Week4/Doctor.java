package concurent.labs.solution;

/**
 * Represents the doctor giving out the vaccines
 * The doctor can only give it to one client at a time
 */
public class Doctor {

    private static final int VACCINATION_TIME = 1000;

    private boolean free = true;

    public void giveVaccination(Client client){
        this.free = false;
        try {
            Thread.sleep(VACCINATION_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Doctor gave vaccination to " + client.getName());
        this.free = true;
        // Notify clients waiting for vaccinations that the doctor is free
        this.notifyAll();
    }

    public boolean isFree(){
        return this.free;
    }

}
