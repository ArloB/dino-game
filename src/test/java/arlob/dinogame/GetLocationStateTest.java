package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class GetLocationStateTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String state, Location loc, State expected) {
        Dinosaurs game = new Dinosaurs(Objective.getObjective(68));
        game.initializeBoardState(state);
        State out = game.getLocationState(loc);

        assertTrue("Expected " + expected + " for state " + state +
                ", and location(" + loc.getX() + ", " + loc.getY() + "), but got " + out + ".", out == expected);
    }

    @Test
    public void testInitialState() {
        test("", new Location(0,0), State.EMPTY);
        test("", new Location(1,0), State.WATER);
        test("", new Location(2,0), State.EMPTY);
        test("", new Location(3,0), State.WATER);
        test("", new Location(4,0), State.EMPTY);
        test("", new Location(0,1), State.WATER);
        test("", new Location(1,1), State.EMPTY);
        test("", new Location(2,1), State.WATER);
        test("", new Location(3,1), State.EMPTY);
        test("", new Location(4,1), State.WATER);
        test("", new Location(0,2), State.EMPTY);
        test("", new Location(1,2), State.WATER);
        test("", new Location(2,2), State.EMPTY);
        test("", new Location(3,2), State.WATER);
        test("", new Location(4,2), State.EMPTY);
        test("", new Location(0,3), State.WATER);
        test("", new Location(1,3), State.EMPTY);
        test("", new Location(2,3), State.WATER);
        test("", new Location(3,3), State.EMPTY);
        test("", new Location(4,3), State.WATER);
    }

    @Test
    public void testSingleA() {
        test("b20W", new Location(2,0), State.GREEN);
        test("b20W", new Location(3,0), State.WATER);
        test("b20W", new Location(4,0), State.GREEN);
        test("b20W", new Location(2,1), State.WATER);
        test("b20W", new Location(3,1), State.GREEN);
        test("b20W", new Location(4,1), State.WATER);
    }

    @Test
    public void testSingleB() {
        test("c11N", new Location(1,1), State.RED);
        test("c11N", new Location(2,1), State.WATER);
        test("c11N", new Location(1,2), State.WATER);
        test("c11N", new Location(2,2), State.RED);
        test("c11N", new Location(1,3), State.EMPTY);
        test("c11N", new Location(2,3), State.WATER);
    }

    @Test
    public void testSingleC() {
        test("e21W", new Location(2,1), State.WATER);
        test("e21W", new Location(3,1), State.GREEN);
        test("e21W", new Location(4,1), State.WATER);
        test("e21W", new Location(2,2), State.EMPTY);
        test("e21W", new Location(3,2), State.WATER);
        test("e21W", new Location(4,2), State.GREEN);
    }

    @Test
    public void testSingleD() {
        test("a22W", new Location(2,2), State.EMPTY);
        test("a22W", new Location(3,2), State.WATER);
        test("a22W", new Location(4,2), State.EMPTY);
        test("a22W", new Location(2,3), State.WATER);
        test("a22W", new Location(3,3), State.RED);
        test("a22W", new Location(4,3), State.WATER);
    }

    @Test
    public void testDouble() {
        test("f00Wd01S", new Location(0,0), State.RED);
        test("f00Wd01S", new Location(1,0), State.WATER);
        test("f00Wd01S", new Location(2,0), State.EMPTY);
        test("f00Wd01S", new Location(0,1), State.WATER);
        test("f00Wd01S", new Location(1,1), State.RED);
        test("f00Wd01S", new Location(2,1), State.WATER);
        test("f00Wd01S", new Location(0,2), State.GREEN);
        test("f00Wd01S", new Location(0,3), State.WATER);
        test("f00Wd01S", new Location(1,2), State.WATER);
        test("f00Wd01S", new Location(1,3), State.GREEN);
    }

    @Test
    public void testMulti() {
        test("f00Wd01Sb20W", new Location(0,0), State.RED);
        test("f00Wd01Sb20W", new Location(1,0), State.WATER);
        test("f00Wd01Sb20W", new Location(2,0), State.GREEN);
        test("f00Wd01Sb20W", new Location(3,0), State.WATER);
        test("f00Wd01Sb20W", new Location(4,0), State.GREEN);
        test("f00Wd01Sb20W", new Location(0,1), State.WATER);
        test("f00Wd01Sb20W", new Location(1,1), State.RED);
        test("f00Wd01Sb20W", new Location(2,1), State.WATER);
        test("f00Wd01Sb20W", new Location(0,2), State.GREEN);
        test("f00Wd01Sb20W", new Location(0,3), State.WATER);
        test("f00Wd01Sb20W", new Location(1,2), State.WATER);
        test("f00Wd01Sb20W", new Location(1,3), State.GREEN);
        test("f00Wd01Sb20W", new Location(3,1), State.GREEN);
        test("f00Wd01Sb20W", new Location(4,1), State.WATER);
    }
}
