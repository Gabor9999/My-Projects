package Week2;

public enum City {
    BUDAPEST(1000), DEBRECEN(4000), SZEGED(6700), MISKOLC(3500), PÉCS(7600), GYŐR(9000), NYÍREGYHÁZA(4400),
    KECCSKEMÉT(6000), SZÉKESFEHÉRVÁR(8000), SZOMBATHELY(9700);


    private final int zipCode;

    private City(int zip) {
        zipCode = zip;
    }

    private City(City other) {
        zipCode = other.zipCode;
    }

    public int getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        String allCaps = super.toString();
        return allCaps.substring(0, 1) + allCaps.substring(1).toLowerCase();
    }
}
