package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class IsPlacementConsistentTest {
    char[] dgroup1 = {'N', 'E'};
    char[] dgroup2 = {'S', 'W'};
    char[] tgroup1 = {'a', 'b', 'f'};
    char[] tgroup2 = {'c', 'd', 'e'};

    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String state, String pl, boolean expected) {
        Dinosaurs game = new Dinosaurs(Objective.getObjective(57));
        game.initializeBoardState(state);
        boolean out = game.isPlacementConsistent(pl);

        assertTrue("Expected " + expected + " for placement " +
                pl + ", but got " + out + ".", out == expected);
    }

    @Test
    public void test00() {
        for (char t : tgroup1) {
            for (char d : dgroup1)
                test("", "" + t + "00" + d, false);
            for (char d : dgroup2)
                test("", "" + t + "00" + d, true);
        }
        for (char t : tgroup2) {
            for (char d : dgroup1)
                test("", "" + t + "00" + d, true);
            for (char d : dgroup2)
                test("", "" + t + "00" + d, false);
        }
    }

    @Test
    public void test01() {
        for (char t : tgroup1) {
            for (char d : dgroup1)
                test("", "" + t + "01" + d, true);
            for (char d : dgroup2)
                test("", "" + t + "01" + d, false);
        }
        for (char t : tgroup2) {
            for (char d : dgroup1)
                test("", "" + t + "01" + d, false);
            for (char d : dgroup2)
                test("", "" + t + "01" + d, true);
        }
    }

    @Test
    public void test11() {
        for (char t : tgroup1) {
            for (char d : dgroup1)
                test("", "" + t + "11" + d, false);
            for (char d : dgroup2)
                test("", "" + t + "11" + d, true);
        }
        for (char t : tgroup2) {
            for (char d : dgroup1)
                test("", "" + t + "11" + d, true);
            for (char d : dgroup2)
                test("", "" + t + "11" + d, false);
        }
    }

    @Test
    public void test21() {
        for (char t : tgroup1) {
            for (char d : dgroup1)
                test("", "" + t + "21" + d, true);
            for (char d : dgroup2)
                test("", "" + t + "21" + d, false);
        }
        for (char t : tgroup2) {
            for (char d : dgroup1)
                test("", "" + t + "21" + d, false);
            for (char d : dgroup2)
                test("", "" + t + "21" + d, true);
        }
    }

    @Test
    public void testComplex20() {
        for (char d : dgroup1) {
            test("b00Wd31Nf12E", "" + "c" + "20" + d, true);
            test("b00Wd31Nf12E", "" + "e" + "20" + d, true);
            test("b00Wc01Sf12E", "" + "a" + "20" + d, false);
        }
        for (char d : dgroup2) {
            test("b00Wd31Nf12E", "" + "c" + "20" + d, false);
            test("b00Wd31Nf12E", "" + "e" + "20" + d, false);
            test("b00Wc01Sf12E", "" + "a" + "20" + d, true);
        }
    }

    @Test
    public void testComplex10() {
        for (char d : dgroup1) {
            test("c01Sd31Nf12E", "" + "a" + "10" + d, true);
            test("c01Sd31Nf12E", "" + "e" + "10" + d, false);
            test("c01Sd31Nf12E", "" + "b" + "10" + d, true);
        }
        for (char d : dgroup2) {
            test("c01Sd31Nf12E", "" + "a" + "10" + d, false);
            test("c01Sd31Nf12E", "" + "e" + "10" + d, true);
            test("c01Sd31Nf12E", "" + "b" + "10" + d, false);
        }
    }
}
