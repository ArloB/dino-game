package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class OrientationToCharTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(Orientation dir, char expected) {
        char out = dir.toChar();
        assertTrue("Expected " + expected + " for input Orientation" + dir +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test(Orientation.NORTH, 'N');
        test(Orientation.EAST, 'E');
    }

    @Test
    public void testSimple2() {
        test(Orientation.NORTH, 'N');
        test(Orientation.SOUTH, 'S');
    }

    @Test
    public void testSimple3() {
        test(Orientation.NORTH, 'N');
        test(Orientation.WEST, 'W');
    }
}
