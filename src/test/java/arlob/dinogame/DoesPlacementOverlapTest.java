package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class DoesPlacementOverlapTest {
    char [] dirs = {'N', 'E', 'S', 'W'};

    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String state, String pl, boolean expected) {
        Dinosaurs game = new Dinosaurs(Objective.getObjective(57));
        game.initializeBoardState(state);
        boolean out = game.doesPlacementOverlap(pl);

        assertTrue("Expected " + expected + " for state " + state +
                ", and placement " + pl + ", but got " + out + ".", out == expected);
    }

    @Test
    public void testSimple1() {
        for (int i = 'a'; i <= 'f'; i++) {
            for (char dir:dirs) {
                String pl = "" + (char)(i) + "00" + dir;
                test("", pl, false);
                test(pl, pl, true);
            }
        }
    }

    @Test
    public void testSimple2() {
        for (char dir:dirs) {
            test("b00Wc01Sf12E", "a10"+dir, true);
            test("b00Wc01Sf12E", "a20"+dir, false);
            test("b00Wc01Sf12E", "a12"+dir, true);
            test("b00Wc01Sf12E", "a22"+dir, true);
        }
    }

    @Test
    public void testSimple3() {
        test("b00Wc01Sf12E", "a11N", true);
        test("b00Wc01Sf12E", "a11E", false);
        test("b00Wc01Sf12E", "a11S", true);
        test("b00Wc01Sf12E", "a11W", false);
        test("b00Wc01Sf12E", "a21N", true);
        test("b00Wc01Sf12E", "a21E", false);
        test("b00Wc01Sf12E", "a21S", true);
        test("b00Wc01Sf12E", "a21W", false);
    }
}
