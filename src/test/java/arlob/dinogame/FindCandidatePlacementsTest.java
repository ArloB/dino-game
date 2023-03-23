package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static arlob.dinogame.ViolatesObjectiveTest.randSols;
import static org.junit.Assert.assertTrue;

public class FindCandidatePlacementsTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(Objective obj, String state, Location loc, Set<String> expected) {
        Dinosaurs game = new Dinosaurs(obj);
        game.initializeBoardState(state);
        Set<String> out = game.findCandidatePlacements(loc);

        assertTrue("Expected " + expected.toString() +
                        " for obj.connections " + obj.getConnectedIslands() +
                        ", and state " + state +
                        ", and location (" + loc.getX() + ", " + loc.getY() + ")" +
                        ", but got " + out.toString() + ".",
                out.size() == expected.size() && out.containsAll(expected)
        );
    }

    @Test
    public void testLastAction() {
        for (int i = 15; i < randSols.length; i++) {
            for (int j = 0; j < randSols[i].length(); j += 4) {
                Set<String> exp = new LinkedHashSet<>();
                exp.add(randSols[i].substring(j, j + 4));
                test(
                        Objective.getObjective(i),
                        randSols[i].substring(0, j) + randSols[i].substring(j + 4),
                        new Location(
                                randSols[i].charAt(j + 1) - '0',
                                randSols[i].charAt(j + 2) - '0'
                        ),
                        exp
                );
            }
        }
    }

    @Test
    public void test00() {
        String[][] exp = {
                {"b00W", "b00S"},
                {"d00N", "c00N", "e00E", "f00W"},
                {"b00S", "e00E", "f00W"},
                {"a00S", "c00E", "d00E"},
                {"a00S", "c00E", "d00E"},
                {"d00N", "c00N", "b00W"},
                {"a00W", "a00S"},
                {"f00S", "e00N", "c00E", "d00E"}
        };
        Integer[] tests = {20, 21, 24, 27, 31, 35, 41, 44};
        for (int i = 0; i < 8; i++) {
            test(
                    Objective.getObjective(tests[i]),
                    "",
                    new Location(0, 0),
                    new LinkedHashSet<>(Arrays.asList(exp[i]))
            );
        }
    }

    @Test
    public void testAB() {
        for (int i = 15; i < randSols.length; i++) {
            Set<String> expA = new LinkedHashSet<>();
            Set<String> expB = new LinkedHashSet<>();

            expA.add(randSols[i].substring(0, 4));
            expB.add(randSols[i].substring(4, 8));

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(8),
                    new Location(
                            randSols[i].charAt(1) - '0',
                            randSols[i].charAt(2) - '0'
                    ),
                    expA
            );

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(8),
                    new Location(
                            randSols[i].charAt(5) - '0',
                            randSols[i].charAt(6) - '0'
                    ),
                    expB
            );
        }
    }

    @Test
    public void testCF() {

        Integer[] exArr = {31, 44, 53, 77};
        Set<Integer> exSet = new LinkedHashSet<>(Arrays.asList(exArr));

        for (int i = 15; i < randSols.length; i++) {

            if (exSet.contains(i))
                continue;

            Set<String> expA = new LinkedHashSet<>();
            Set<String> expB = new LinkedHashSet<>();

            expA.add(randSols[i].substring(8, 12));   // C
            expB.add(randSols[i].substring(20, 24));   // F

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 8) + randSols[i].substring(12, 20),
                    new Location(
                            randSols[i].charAt(9) - '0',
                            randSols[i].charAt(10) - '0'
                    ),
                    expA
            );

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 8) + randSols[i].substring(12, 20),
                    new Location(
                            randSols[i].charAt(21) - '0',
                            randSols[i].charAt(22) - '0'
                    ),
                    expB
            );
        }
    }

    @Test
    public void testDE() {

        Integer[] exArr = {19, 70, 71};
        Set<Integer> exSet = new LinkedHashSet<>(Arrays.asList(exArr));

        for (int i = 15; i < randSols.length; i++) {

            if (exSet.contains(i))
                continue;

            Set<String> expA = new LinkedHashSet<>();
            Set<String> expB = new LinkedHashSet<>();

            expA.add(randSols[i].substring(12, 16));   // D
            expB.add(randSols[i].substring(16, 20));   // E

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 12) + randSols[i].substring(20),
                    new Location(
                            randSols[i].charAt(13) - '0',
                            randSols[i].charAt(14) - '0'
                    ),
                    expA
            );

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 12) + randSols[i].substring(20),
                    new Location(
                            randSols[i].charAt(17) - '0',
                            randSols[i].charAt(18) - '0'
                    ),
                    expB
            );
        }
    }

    @Test
    public void testCD() {

        Integer[] inArr = {15, 17};
        Set<Integer> inSet = new LinkedHashSet<>(Arrays.asList(inArr));

        for (int i = 15; i < randSols.length; i++) {

            if (!inSet.contains(i))
                continue;

            Set<String> expA = new LinkedHashSet<>();
            Set<String> expB = new LinkedHashSet<>();

            expA.add(randSols[i].substring(8, 12));   // C
            expB.add(randSols[i].substring(12, 16));   // D

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 8) + randSols[i].substring(16),
                    new Location(
                            randSols[i].charAt(9) - '0',
                            randSols[i].charAt(10) - '0'
                    ),
                    expA
            );

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 8) + randSols[i].substring(16),
                    new Location(
                            randSols[i].charAt(13) - '0',
                            randSols[i].charAt(13) - '0'
                    ),
                    expB
            );
        }
    }

    @Test
    public void testBC() {

        Integer[] exArr = {57, 62};
        Set<Integer> exSet = new LinkedHashSet<>(Arrays.asList(exArr));

        for (int i = 15; i < randSols.length; i++) {

            if (exSet.contains(i))
                continue;

            Set<String> expA = new LinkedHashSet<>();
            Set<String> expB = new LinkedHashSet<>();

            expA.add(randSols[i].substring(4, 8));   // B
            expB.add(randSols[i].substring(8, 12));   // C

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 4) + randSols[i].substring(12),
                    new Location(
                            randSols[i].charAt(5) - '0',
                            randSols[i].charAt(6) - '0'
                    ),
                    expA
            );

            test(
                    Objective.getObjective(i),
                    randSols[i].substring(0, 4) + randSols[i].substring(12),
                    new Location(
                            randSols[i].charAt(9) - '0',
                            randSols[i].charAt(10) - '0'
                    ),
                    expB
            );
        }
    }
}
