package arlob.dinogame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class ViolatesObjectiveTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(Objective obj, String state, String pl, boolean expected) {
        Dinosaurs game = new Dinosaurs(obj);
        game.initializeBoardState(state);
        boolean out = game.violatesObjective(pl);

        assertTrue("Expected " + expected +
                " for obj.connections " + obj.getConnectedIslands() +
                ", and state " + state +
                ", and placement " + pl +
                ", but got " + out + ".", out == expected);
    }

    private char reverse(char dir) {
        switch (dir) {
            case 'N':
                return 'S';
            case 'E':
                return 'W';
            case 'S':
                return 'N';
            case 'W':
                return 'E';
        }
        assert false;
        return 'Z';
    }

    @Test
    public void testSinglePD() {
        for (int i = 15; i < Objective.getOBJECTIVES().length; i++) {
            for (int j = 4; j < randSols[i].length(); j += 4) {
                test(Objective.getObjective(i),
                        "",
                        randSols[i].substring(j, j + 4),
                        false);
                test(Objective.getObjective(i),
                        "",
                        randSols[i].substring(j, j + 3) + reverse(randSols[i].charAt(j + 3)),
                        true);
            }
        }
    }

    @Test
    public void testSinglePU() {
        for (int i = 15; i < Objective.getOBJECTIVES().length; i++) {
            for (int j = 4; j < randSols[i].length(); j += 4) {
                test(Objective.getObjective(i),
                        randSols[i].substring(0, j) + randSols[i].substring(j + 4),
                        randSols[i].substring(j, j + 3) + reverse(randSols[i].charAt(j + 3)),
                        true);
                test(Objective.getObjective(i),
                        randSols[i].substring(0, j) + randSols[i].substring(j + 4),
                        randSols[i].substring(j, j + 4),
                        false);
            }
        }
    }

    @Test
    public void testDoublePU() {
        for (int i = 15; i < Objective.getOBJECTIVES().length; i++) {
            for (int j = 0; j < randSols[i].length(); j += 4) {
                for (int k = j + 4; k < randSols[i].length(); k += 4) {
                    if ((j == (4 * ('c' - 'a')) && k == (4 * ('d' - 'a'))) ||
                            (j == (4 * ('e' - 'a')) && k == (4 * ('f' - 'a')))) {
                        continue;
                    }
                    test(Objective.getObjective(i),
                            randSols[i].substring(0, j) +
                                    randSols[i].substring(j + 4, k) +
                                    randSols[i].substring(k + 4),
                            randSols[i].substring(j, j + 4),
                            false);
                    test(Objective.getObjective(i),
                            randSols[i].substring(0, j) +
                                    randSols[i].substring(j + 4, k) +
                                    randSols[i].substring(k + 4),
                            randSols[i].substring(j, j + 1) +
                                    randSols[i].substring(k + 1, k + 4),
                            true);
                    test(Objective.getObjective(i),
                            randSols[i].substring(0, j) +
                                    randSols[i].substring(j + 4, k) +
                                    randSols[i].substring(k + 4),
                            randSols[i].substring(k, k + 4),
                            false);
                    test(Objective.getObjective(i),
                            randSols[i].substring(0, j) +
                                    randSols[i].substring(j + 4, k) +
                                    randSols[i].substring(k + 4),
                            randSols[i].substring(k, k + 1) +
                                    randSols[i].substring(j + 1, j + 4),
                            true);
                }
            }
        }
    }

    public static String[] randSols = {
            "a20Wb00Wc22Ed11Ne01Sf21E",
            "a11Wb10Ec22Ed00Ne30Sf02W",
            "a02Wb20Wc01Wd22Ee21Wf00W",
            "a21Eb22Wc20Ed00Ee01Wf02W",
            "a02Wb01Ec20Ed00Ee22Ef21E",
            "a00Wb22Wc01Wd21We20Ef02W",
            "a21Nb20Wc02Ed01We00Ef31S",
            "a21Eb22Wc20Ed00Ne10Sf02W",
            "a02Wb20Wc21Sd00Ne10Sf31S",
            "a31Sb02Wc00Nd20Ee21Sf10N",
            "a11Sb20Wc21Sd00Ee01Sf31S",
            "a00Wb20Wc01Wd22Ee21Wf02W",
            "a02Wb00Sc20Ed10Se22Ef21E",
            "a11Sb00Wc21Sd01Se20Ef31S",
            "a20Wb00Wc21Sd01We02Ef31S",
            "a00Wb21Ec01Wd22Ee20Ef02W",
            "a31Sb21Nc01Wd02Ee20Ef00W",
            "a20Wb10Nc22Ed00Ne02Ef21E",
            "a21Nb20Wc11Nd01Se31Nf00W",
            "a20Wb11Sc22Ed01Se00Ef21E",
            "a21Eb00Wc20Ed22Ee01Sf11S",
            "a10Nb21Nc00Nd20Ee31Nf02W",
            "a10Nb21Nc00Nd31Ne20Ef02W",
            "a10Nb20Wc00Nd02Ee21Sf31S",
            "a20Wb00Sc22Ed10Se02Ef21E",
            "a10Nb20Wc00Nd21We22Ef02W",
            "a10Nb20Wc00Nd31Ne21Sf02W",
            "a00Sb30Nc10Wd22Ee02Ef11W",
            "a10Nb20Sc00Nd22Ee30Sf02W",
            "a00Wb22Wc01Wd20Ne30Sf02W",
            "a10Nb22Wc00Nd20Ne30Sf02W",
            "a00Sb02Wc20Ed21Se31Nf10N",
            "a10Eb11Wc22Ed00Ne30Sf02W",
            "a02Wb20Wc01Wd31Ne21Sf00W",
            "a22Wb21Ec01Wd02Ee20Ef00W",
            "a31Sb21Nc00Nd20Ee02Ef10N",
            "a02Wb22Wc00Nd20Ee21Wf10N",
            "a21Nb31Sc01Sd00Ee20Ef11S",
            "a01Nb11Sc20Nd22Ee00Ef30N",
            "a20Sb02Wc22Ed10Se00Nf30N",
            "a02Wb00Sc20Nd10Se22Ef30N",
            "a00Wb22Wc20Nd01Se11Nf30N",
            "a21Nb20Wc01Sd00Ee31Nf11S",
            "a02Wb22Wc00Nd30Se20Nf10N",
            "a22Wb21Ec00Ed20Ee11Nf01N",
            "a11Wb10Ec02Ed30Se00Nf22W",
            "a20Sb02Wc22Ed00Ee01Wf30N",
            "a22Wb01Ec20Ed00Ee02Ef21E",
            "a30Nb00Wc22Ed01We02Ef20S",
            "a21Nb31Sc11Nd01Se20Ef00W",
            "a22Wb00Sc30Sd02Ee11Ef10E",
            "a20Wb02Wc22Ed01We00Ef21E",
            "a02Wb21Nc01Wd20Ee31Nf00W",
            "a02Wb22Wc10Wd11Ee30Sf00S",
            "a22Wb01Ec20Ed02Ee00Ef21E",
            "a20Wb00Wc22Ed01We02Ef21E",
            "a01Eb02Wc22Ed20Ne30Sf00W",
            "a11Wb00Wc01Sd31Ne20Ef12E",
            "a00Wb22Wc01Wd30Se20Nf02W",
            "a21Nb31Sc00Nd20Ee02Ef10N",
            "a22Wb02Wc00Nd30Se20Nf10N",
            "a02Wb01Ec20Nd22Ee00Ef30N",
            "a11Sb00Wc01Sd20Ee21Wf22W",
            "a22Wb20Wc01Wd02Ee21Wf00W",
            "a01Nb11Sc20Ed00Ee22Ef21E",
            "a11Wb10Ec02Ed00Ne30Sf22W",
            "a11Sb01Nc20Nd22Ee00Ef30N",
            "a01Nb11Sc31Nd21Se00Ef20W",
            "a22Wb20Wc11Nd01Se21Wf00W",
            "a22Wb10Nc20Ed00Ne02Ef21E",
            "a11Wb30Nc02Ed10We00Nf22W",
            "a31Sb20Wc21Sd00Ee01Sf11S",
            "a10Nb21Ec00Nd22Ee20Ef02W",
            "a21Nb00Wc02Ed01We20Ef31S",
            "a00Sb20Wc10Sd22Ee21Wf02W",
            "a10Eb00Sc11Ed22Ee02Ef30N",
            "a01Eb02Wc22Ed21We20Ef00W",
            "a11Sb00Wc20Ed01Se31Nf21N",
            "a21Eb22Wc01Sd11Ne00Ef20W",
            "a00Wb30Nc01Wd20Ne22Ef02W",
    };
}
