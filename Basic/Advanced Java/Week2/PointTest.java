package Week2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    Point p;
    @BeforeEach
    void testBeforeEach(){
        p = new Point(1,2);
    }

    @Test
    void getX() {
        assertEquals(p.getX(),1);
    }

    @Test
    void getY() {
        assertEquals(p.getY(),2);
    }

    @Test
    void shift() {
        p.shift(1,2);
        assertEquals(p, new Point(2,4));
    }

    @Test
    void mirror() {
        p.mirror(1,2);
        assertEquals(p, new Point(1,2));
    }
}