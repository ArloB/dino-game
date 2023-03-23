package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class IsPlacementDangerousTest {
    String[] randSolutions1 = {
            "a20Wb00Wc22Ed11Ne01Sf21E",
            "a02Wb20Wc01Wd22Ee21Wf00W",
            "a21Eb22Wc20Ed00Ee01Wf02W",
            "a02Wb01Ec20Ed00Ee22Ef21E",
            "a02Wb20Wc01Wd31Ne21Sf00W",
            "a22Wb21Ec01Wd02Ee20Ef00W",
            "a31Sb21Nc00Nd20Ee02Ef10N",
            "a02Wb22Wc00Nd20Ee21Wf10N",
            "a02Wb00Sc20Nd10Se22Ef30N",
            "a00Wb22Wc20Nd01Se11Nf30N",
            "a21Nb20Wc01Sd00Ee31Nf11S",
            "a02Wb22Wc00Nd30Se20Nf10N",
            "a22Wb21Ec00Ed20Ee11Nf01N",
            "a22Wb02Wc00Nd30Se20Nf10N",
            "a01Nb11Sc20Ed00Ee22Ef21E",
    };

    String[] randDanger = {
            "f00Wd11N",
            "b20Wf21E",
            "f00We01S"
    };

    String[] randSafty = {
            "c20Nd01S",
            "e20Ef00W",
            "f00Wd01S"
    };

    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String state, String pl, boolean expected) {
        Dinosaurs game = new Dinosaurs(Objective.getObjective(68));
        game.initializeBoardState(state);
        boolean out = game.isPlacementDangerous(pl);

        assertTrue("Expected " + expected + " for state " + state +
                ", and placement " + pl + ", but got " + out + ".", out == expected);
    }

    @Test
    public void test1() {
        for (int i = 0; i < randDanger.length && i < randSafty.length; i++) {
            test(randDanger[i].substring(0, 4), randDanger[i].substring(4, 8), true);
            test(randSafty[i].substring(0, 4), randSafty[i].substring(4, 8), false);
            test(randDanger[i].substring(4, 8), randDanger[i].substring(0, 4), true);
            test(randSafty[i].substring(4, 8), randSafty[i].substring(0, 4), false);
        }
    }

    @Test
    public void testSingle() {
        for (int i = 0; i < randSolutions1.length; i++) {
            for (int j = 0; j < randSolutions1[i].length(); j += 4) {
                test(randSolutions1[i].substring(0, j) + randSolutions1[i].substring(j + 4),
                        randSolutions1[i].substring(j, j + 4),
                        false
                );
                test(randDanger[i % 2].substring(0, 4), randDanger[i % 2].substring(4, 8), true);
            }
        }
    }

    @Test
    public void testDouble1() {
        for (int i = 0; i < randSolutions1.length; i++) {
            String state = randSolutions1[i].substring(0, 8) +
                    randSolutions1[i].substring(16);
            String pl1 = randSolutions1[i].substring(12, 13) +
                    randSolutions1[i].substring(9, 12);
            String pl2 = randSolutions1[i].substring(8, 9) +
                    randSolutions1[i].substring(13, 16);
            test(state, pl1, true);
            test(state, pl2, true);
            test(randSafty[i % 2].substring(0, 4), randSafty[i % 2].substring(4, 8), false);
        }
    }
}
