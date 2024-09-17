package concurent.labs.solution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represent a zone a contract can take place in
 * In part 3 only one witcher can do a contract per zone
 */
public enum Zone {

    VIZIMA,
    VERGEN,
    LOC_MUINNE,
    FLOTSAM,
    NOVIGRAD;

    private static final List<Zone> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static int getCount() {
        return SIZE;
    }

    public static Zone randomZone()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
