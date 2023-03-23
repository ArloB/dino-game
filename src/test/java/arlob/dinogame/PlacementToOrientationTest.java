package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class PlacementToOrientationTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String pl, Orientation expected) {
        Orientation out = Tile.placementToOrientation(pl);

        assertTrue("Expected " + expected + " for input placement " + pl +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        test("a00N", Orientation.NORTH);
        test("a00E", Orientation.EAST);
    }

    @Test
    public void testSimple2() {
        test("a00N", Orientation.NORTH);
        test("a00S", Orientation.SOUTH);
    }

    @Test
    public void testSimple3() {
        test("a00N", Orientation.NORTH);
        test("a00W", Orientation.WEST);
    }

    @Test
    public void testSimple4() {
        test("f32S", Orientation.SOUTH);
        test("d21W", Orientation.WEST);
    }
}
