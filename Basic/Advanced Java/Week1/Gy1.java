import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gy1 {
    public enum City {
        DEBRECEN(1234),
        BUDAPEST(1089),
        GYOR(2100);
        private final int zipCode;

        private double getZipCode() { return zipCode; }

        City(int zipCode) {
            this.zipCode = zipCode;
        }

        City(City city) {
            this.zipCode = city.zipCode;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    public enum WeekDay {
        MON("hu", "hétfő", "en", "Monday", "de", "Montag", "fr", "lundi", "nl", "maandag", "se", "måndag", "gr", "Δευτέρα"),
        TUE("hu", "kedd", "en", "Tuesday", "de", "Dienstag", "fr", "mardi", "nl", "dinsdag", "se", "tisdag", "gr", "Τρίτη"),
        WED("hu", "szerda", "en", "Wednesday", "de", "Mittwoch", "fr", "mercredi", "nl", "woensdag", "se", "onsdag", "gr", "Τετάρτη"),
        THU("hu", "csütörtök", "en", "Thursday", "de", "Donnerstag", "fr", "jeudi", "nl", "donderdag", "se", "torsdag", "gr", "Πέμπτη"),
        FRI("hu", "péntek", "en", "Friday", "de", "Freitag", "fr", "vendredi", "nl", "vrijdag", "se", "fredag", "gr", "Παρασκευή"),
        SAT("hu", "szombat", "en", "Saturday", "de", "Samstag", "fr", "samdi", "nl", "zaterdag", "se", "lördag", "gr", "Σάββατο"),
        SUN("hu", "vasárnap", "en", "Sunday", "de", "Sonntag", "fr", "dimanche", "nl", "zondag", "se", "söndag", "gr", "Κυριακή");
        public static final String[] supportedLanguages = {"de", "en", "fr", "hu", "nl", "se", "gr"};
        private final Map<String, String> dayName = new HashMap<>();

        private WeekDay(String... names) {
            String lang = null;
            for (String name: names) {
                if (lang == null) {
                    lang = name;
                } else {
                    dayName.put(lang, name);
                    lang = null;
                }
            }
            if (lang != null) {
                throw new IllegalArgumentException("No name for language " + lang);
            }
        }

        public String getName(String lang) {
            String name = dayName.get(lang);
            if (name != null) {
                return name;
            } else {
                return "?";
            }
        }

        public WeekDay nextDay() {
            if(this.ordinal() - 6 == 0) {
                return this.MON;
            }
            return WeekDay.values()[this.ordinal() + 1];
        }

        public WeekDay nextDay(int step) {
            if(this.ordinal() + step%7 > 6) {
                return WeekDay.values()[this.ordinal() + step%7 - 7];
            } else if (this.ordinal() + step%7 < 0) {
                return WeekDay.values()[6 - (this.ordinal() + step%7)];
            }
            return WeekDay.values()[this.ordinal() + step%7];
        }
    }
    public static void main(String args[]) {
        System.out.println(City.DEBRECEN.toString());
        WeekDay w = WeekDay.FRI;
        System.out.println(w.nextDay().toString());
        System.out.println(w.nextDay(4).toString());
        System.out.println(w.nextDay(-4).toString());
        System.out.println(w.nextDay(14).toString());
        System.out.println(w.nextDay(-14).toString());
    }
}
