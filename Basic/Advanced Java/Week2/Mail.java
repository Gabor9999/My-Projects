package Week2;

public class Mail {
    private int zipCode;

    public Mail(int zip) {
        zipCode = zip;
    }

    public Mail(City city) {
        zipCode = city.getZipCode();
    }

    public int getZip() {
        return zipCode;
    }
}
