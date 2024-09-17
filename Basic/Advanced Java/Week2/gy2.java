package Week2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class gy2 {
    public static int fib1(int n) {
        if (n == 0)
            return 0;

        if (n == 1)
            return 1;

        int n1 = 0, n2 = 1;

        for (int i = 2; i <= n; ++i) {
            int n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
        }
        return n2;
    }

    private static int recFib(int n, int n1, int n2) {
        if (n == 0)
            return n1;
        if (n == 1)
            return n2;
        return recFib(n - 1, n2, n1 + n2);
    }

    public static int fib2(int n) {
        return recFib(n, 0, 1);
    }

    @ParameterizedTest
    @CsvSource ({
            "0,0",
            "1,1",
            "2,1",
            "5,5",
            "12,144"
    })
    void abcTest(int n1, int n2) {
        /*assertEquals(fib1(5),fib2(5));
        assertEquals(fib1(7),fib2(7));
        assertEquals(fib1(2),fib2(2));
        assertEquals(fib1(0),0);
        assertEquals(fib2(0),0);
        assertEquals(fib1(-1),1);*/
        assertEquals(fib1(n1),fib2(n2));
        /*Exception exception = assertThrows(IllegalArgumentException.class, () ->
                fib1(0));
        assertEquals("wrong argument", exception.getMessage());*/
    }
};
