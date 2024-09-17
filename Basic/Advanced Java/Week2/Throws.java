package Week2;

import static Week2.Throws.Unreliable.div;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class Throws {
    public class Unreliable {
        public static int div(int n, int m) {
            return n / m;
        }

        public static int get(int[] array, int index) {
            return array[index];
        }
    }

    @Test
    void testZeroDivision() {
        assertEquals("/ by zero", assertThrows(ArithmeticException.class,() -> Unreliable.div(1,0)).getMessage());
    }

    @Test
    void indexOutOfBounds() {
        assertEquals("Index 3 out of bounds for length 3", assertThrows(IndexOutOfBoundsException.class,() -> Unreliable.get(new int[]{1, 2, 3},3)).getMessage());
    }

}
