package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class PlacementToLocationTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String pl, Location expected) {
        Location out = Tile.placementToLocation(pl);

        assertTrue("Expected (" + expected.getX() + "," + expected.getY() + ")" +
                        " for input placement " + pl +
                ", but got (" + out.getX() + "," + out.getY() + ")" + ".",
                out.getX() == expected.getX() && out.getY() == expected.getY());
    }

    @Test
    public void testSimple1() {
        test("a00N", new Location(0,0));
        test("a10E", new Location(1,0));
    }

    @Test
    public void testSimple2() {
        test("a22N", new Location(2,2));
        test("a02S", new Location(0,2));
    }

    @Test
    public void testSimple3() {
        test("a11N", new Location(1,1));
        test("a32W", new Location(3,2));
    }
}
