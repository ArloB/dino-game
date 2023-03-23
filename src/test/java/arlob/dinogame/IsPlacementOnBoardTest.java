package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class IsPlacementOnBoardTest {
    char [] dirs = {'N', 'E', 'S', 'W'};

    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String pl, boolean expected) {
        boolean out = Dinosaurs.isPlacementOnBoard(pl);
        assertTrue("Expected " + expected + " for placement string " + pl +
                ", but got " + out + ".", out == expected);
    }

    @Test
    public void testX() {
        for (char i = 'a'; i <= 'f'; i++) {
            String in = "" + i + "30" + "N";
            test(in, true);
            in = "" + i + "30" + "E";
            test(in, false);
            in = "" + i + "30" + "S";
            test(in, true);
            in = "" + i + "30" + "W";
            test(in, false);
        }
    }

    @Test
    public void testY() {
        for (char i = 'a'; i <= 'f'; i++) {
            String in = "" + i + "02" + "N";
            test(in, false);
            in = "" + i + "02" + "E";
            test(in, true);
            in = "" + i + "02" + "S";
            test(in, false);
            in = "" + i + "02" + "W";
            test(in, true);
        }
    }

    @Test
    public void testXY() {
        for (char i = 'a'; i <= 'f'; i++) {
            for (char o:dirs) {
                String in = "" + i + "32" + o;
                test(in, false);
                test("a00W", true);
            }
        }
    }

    @Test
    public void test00() {
        for (char i = 'a'; i <= 'f'; i++) {
            for (char o:dirs) {
                String in = "" + i + "00" + o;
                test(in, true);
                test("a32E", false);
            }
        }

    }
}
