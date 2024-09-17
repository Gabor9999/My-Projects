package Week2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

public class Infinite {
    public static void infiniteLoop() {
        while(true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        @Test
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
        void failsIfExecutionTimeExceeds500Milliseconds() {
            infiniteLoop();
        }
    }