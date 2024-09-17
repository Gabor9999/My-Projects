package concurent.labs.solution;

/**
 * Represents a client who has a name and the will to get vaccinated
 * Clients will wait for their turn to receive vaccination
 */
public class Client {

    private final String name;

    public Client(final String name){
        this.name = name;
    }

    /**
     * Waiting for the doctor to be ready to take a new client
     * When the doctor is ready, client will receive the vaccination
     * @param doc The doctor
     */
    public void tryToGetVaccination(final Doctor doc){
        System.out.println(this.name + " is waiting for his vaccination");
        synchronized (doc){
            // condition is in while loop because of spurious wake ups
            while(!doc.isFree()) {
                try {
                    doc.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // this needs to be in the synchronized block
            // since there are a lot of clients trying to access it
            doc.giveVaccination(this);
        }
        System.out.println(this.name + " got his vaccination");
    }

    /**
     * Returns the name
     * @return Name of the client
     */
    public String getName(){
        return this.name;
    }
}
