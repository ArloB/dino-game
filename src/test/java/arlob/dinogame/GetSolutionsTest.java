package arlob.dinogame;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class GetSolutionsTest {
    private String normalizePlacement(String placement) {
      Set<String> placements = new HashSet<>();
      for (int i = 0; i < placement.length(); i += 4) {
        placements.add(placement.substring(i, i + 4));
      }
      List<String> sortedPlacements = new ArrayList<>(placements);
      Collections.sort(sortedPlacements);
      String rtn = "";
      for (String p : sortedPlacements)
        rtn += p;
      return rtn;
    }

    private void test(Objective obj, Set<String> expected) {
      Set<String> out = new HashSet<>();
      Dinosaurs game = new Dinosaurs(obj);
      Set<String> raw = game.getSolutions();
      for (String r : raw)
        out.add(normalizePlacement(r));

      for (String e : expected) {
        assertTrue("Expected " + e +
                        " to be in solutions for objective " + obj.getConnectedIslands() +
                        ", but got " + out.toString() + ".",
                out.contains(e));

      }
      for (String o : out) {
        assertTrue("Got " + o +
                        " among solutions for objective " + obj.getConnectedIslands() +
                        ", but expected " + expected.toString() + ".",
                expected.contains(o));

      }
    }

    @Test
    public void testAll() {
        for (int i = 0; i < Objective.getOBJECTIVES().length; i++) {
            Set<String> resSet = new LinkedHashSet<>(Arrays.asList(sols[i]));
            test(Objective.getObjective(i), resSet);
        }
    }

    public static String[][] sols = {
            {"a20Wb00Wc22Ed11Ne01Sf21E"},
            {"a11Wb10Ec22Ed00Ne30Sf02W"},
            {"a02Wb20Wc01Wd22Ee21Wf00W"},
            {"a21Eb22Wc20Ed00Ee01Wf02W"},
            {"a02Wb01Ec20Ed00Ee22Ef21E"},
            {"a00Wb22Wc01Wd21We20Ef02W"},
            {"a21Nb20Wc02Ed01We00Ef31S"},
            {"a21Eb22Wc20Ed00Ne10Sf02W"},
            {"a02Wb20Wc21Sd00Ne10Sf31S"},
            {"a31Sb02Wc00Nd20Ee21Sf10N"},
            {"a11Sb20Wc21Sd00Ee01Sf31S"},
            {"a00Wb20Wc01Wd22Ee21Wf02W"},
            {"a02Wb00Sc20Ed10Se22Ef21E"},
            {"a11Sb00Wc21Sd01Se20Ef31S"},
            {"a20Wb00Wc21Sd01We02Ef31S"},
            {"a00Wb21Ec01Wd22Ee20Ef02W"},
            {"a31Sb21Nc01Wd02Ee20Ef00W"},
            {"a20Wb10Nc22Ed00Ne02Ef21E"},
            {"a21Nb20Wc11Nd01Se31Nf00W"},
            {"a20Wb11Sc22Ed01Se00Ef21E"},
            {
                    "a21Eb00Wc20Ed22Ee01Sf11S",
                    "a11Wb00Wc12Wd20Ee01Sf31S",
                    "a20Sb00Sc22Ed02Ee10Sf30N",
                    "a20Sb00Sc02Ed22Ee10Sf30N",
                    "a21Nb00Sc02Ed20Ee10Sf31S"
            },
            {
                    "a10Nb21Nc00Nd20Ee31Nf02W",
                    "a10Eb22Wc00Nd11Ee30Sf02W"
            },
            {
                    "a10Nb21Nc00Nd31Ne20Ef02W",
                    "a10Nb20Sc00Nd30Se22Ef02W"
            },
            {
                    "a10Nb20Wc00Nd02Ee21Sf31S",
                    "a01Nb20Wc22Ed21We11Nf00W",
                    "a01Eb20Sc22Ed02Ee30Sf00W"
            },
            {
                    "a20Wb00Sc22Ed10Se02Ef21E",
                    "a20Wb01Nc22Ed11Ne00Ef21E"
            },
            {
                    "a10Nb20Wc00Nd21We22Ef02W",
                    "a31Sb20Wc01Sd11Ee12Wf00W"
            },
            {
                    "a10Nb20Wc00Nd31Ne21Sf02W",
                    "a22Wb30Nc00Nd11Ee10Wf02W"
            },
            {
                    "a00Sb30Nc10Wd22Ee02Ef11W",
                    "a21Nb31Sc00Ed20Ee02Ef01E"
            },
            {
                    "a10Nb20Sc00Nd22Ee30Sf02W",
                    "a01Eb20Wc22Ed21We02Ef00W"
            },
            {
                    "a00Wb22Wc01Wd20Ne30Sf02W",
                    "a00Wb20Wc01Wd21Se31Nf02W"
            },
            {
                    "a10Nb22Wc00Nd20Ne30Sf02W",
                    "a10Nb20Wc00Nd21Se31Nf02W",
                    "a11Sb20Wc21Sd01Se00Ef31S"
            },
            {
                    "a00Sb02Wc20Ed21Se31Nf10N",
                    "a20Sb22Wc00Ed01Se11Nf30N"
            },
            {"a10Eb11Wc22Ed00Ne30Sf02W"},
            {"a02Wb20Wc01Wd31Ne21Sf00W"},
            {"a22Wb21Ec01Wd02Ee20Ef00W"},
            {"a31Sb21Nc00Nd20Ee02Ef10N"},
            {"a02Wb22Wc00Nd20Ee21Wf10N"},
            {"a21Nb31Sc01Sd00Ee20Ef11S"},
            {"a01Nb11Sc20Nd22Ee00Ef30N"},
            {"a20Sb02Wc22Ed10Se00Nf30N"},
            {"a02Wb00Sc20Nd10Se22Ef30N"},
            {"a00Wb22Wc20Nd01Se11Nf30N"},
            {"a21Nb20Wc01Sd00Ee31Nf11S"},
            {"a02Wb22Wc00Nd30Se20Nf10N"},
            {"a22Wb21Ec00Ed20Ee11Nf01N"},
            {"a11Wb10Ec02Ed30Se00Nf22W"},
            {"a20Sb02Wc22Ed00Ee01Wf30N"},
            {"a22Wb01Ec20Ed00Ee02Ef21E"},
            {"a30Nb00Wc22Ed01We02Ef20S"},
            {"a21Nb31Sc11Nd01Se20Ef00W"},
            {"a22Wb00Sc30Sd02Ee11Ef10E"},
            {"a20Wb02Wc22Ed01We00Ef21E"},
            {"a02Wb21Nc01Wd20Ee31Nf00W"},
            {"a02Wb22Wc10Wd11Ee30Sf00S"},
            {"a22Wb01Ec20Ed02Ee00Ef21E"},
            {"a20Wb00Wc22Ed01We02Ef21E"},
            {"a01Eb02Wc22Ed20Ne30Sf00W"},
            {"a11Wb00Wc01Sd31Ne20Ef12E"},
            {"a00Wb22Wc01Wd30Se20Nf02W"},
            {"a21Nb31Sc00Nd20Ee02Ef10N"},
            {"a22Wb02Wc00Nd30Se20Nf10N"},
            {"a02Wb01Ec20Nd22Ee00Ef30N"},
            {"a11Sb00Wc01Sd20Ee21Wf22W"},
            {"a22Wb20Wc01Wd02Ee21Wf00W"},
            {"a01Nb11Sc20Ed00Ee22Ef21E"},
            {"a11Wb10Ec02Ed00Ne30Sf22W"},
            {"a11Sb01Nc20Nd22Ee00Ef30N"},
            {"a01Nb11Sc31Nd21Se00Ef20W"},
            {"a22Wb20Wc11Nd01Se21Wf00W"},
            {"a22Wb10Nc20Ed00Ne02Ef21E"},
            {"a11Wb30Nc02Ed10We00Nf22W"},
            {"a31Sb20Wc21Sd00Ee01Sf11S"},
            {"a10Nb21Ec00Nd22Ee20Ef02W"},
            {"a21Nb00Wc02Ed01We20Ef31S"},
            {"a00Sb20Wc10Sd22Ee21Wf02W"},
            {"a10Eb00Sc11Ed22Ee02Ef30N"},
            {"a01Eb02Wc22Ed21We20Ef00W"},
            {"a11Sb00Wc20Ed01Se31Nf21N"},
            {"a21Eb22Wc01Sd11Ne00Ef20W"},
            {"a00Wb30Nc01Wd20Ne22Ef02W"},
    };
}
