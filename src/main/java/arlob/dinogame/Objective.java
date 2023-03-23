package arlob.dinogame;

import java.util.Random;

/**
 * An objective defines what the player must attempt to solve.   It is expressed
 * in terms of two parts:
 * <p>
 * --------------------
 * PART ONE
 * --------------------
 * <p>
 * A String of `4*N` characters, where `N` is the number of directly connected island pairs.
 * <p>
 * There are ten islands in the game, marked as (*) in the following board layout:
 * <p>
 * X 0    1    2    3    4
 * Y
 * 0  (*)--|---(*)---|--(*)
 * |    |    |    |    |
 * 1  |---(*)---|---(*)---|
 * |    |    |    |    |
 * 2  (*)--|---(*)---|--(*)
 * |    |    |    |    |
 * 3  |---(*)---|---(*)---|
 * <p>
 * Each island is addressed by its X and Y offsets.
 * <p>
 * A direct connection between island(i) and island(j) is encoded with 4 characters as follows:
 * <p>
 * [0] = X offset of island(i)
 * [1] = Y offset of island(i)
 * [2] = X offset of island(j)
 * [3] = Y offset of island(j)
 * <p>
 * For example, the "001110111102" objective string represents that:
 * - the islands at (0,0) and (1,1) must be connected, and
 * - the islands at (1,0) and (1,1) must be connected, and
 * - the islands at (1,1) and (0,2) must be connected, and
 * - all other pairs of islands must not be directly connected.
 * <p>
 * The above objective string leads to this board layout:
 * <p>
 * X 0    1    2    3    4
 * Y
 * 0  (*)--|---(*)---|--(*)
 * | (+)|(+) |    |    |
 * 1  |---(*)---|---(*)---|
 * | (+)|    |    |    |
 * 2  (*)--|---(*)---|--(*)
 * |    |    |    |    |
 * 3  |---(*)---|---(*)---|
 * <p>
 * where a `*` represents an island and a `+` represents a direct path between two islands.
 * <p>
 * --------------------
 * PART TWO
 * --------------------
 * <p>
 * A String of `4*M` characters, where `M` is the number of pieces already placed on the
 * board when the game starts.
 * <p>
 * A placement consists of 4 characters encoded as:
 * <p>
 * [0] = The piece ID {'a' to 'f'}
 * [1] = The x coordinate of the top left corner of the tile {'0' to '3'}
 * [2] = The y coordinate of the top left corner of the tile {'0' to '2'}
 * [3] = the orientation of the piece {'N'=NORTH, 'E'=EAST, 'S'=SOUTH and 'W'=WEST}
 * <p>
 * <p>
 * For example, the string "axyN" means that piece 'a' is placed with its top-left
 * corner at location (x, y), and is oriented NORTH.
 * <p>
 * Note that for all objectives, there should not be any paths between dinosaurs
 * with different colors.
 */
public class Objective {
    private final int problemNumber;          // The problem number from the original board game
    private final String connectedIslands;    // The pairs of islands that must be connected
    private final String initialState;        // The list of initial tile placements

    /**
     * This array defines a set of 80 pre-defined puzzle objectives.
     * <p>
     * There are 4 categories of objective, according to 4 difficulty levels, with
     * 20 objectives per difficulty level, organized within the array as follows:
     * <p>
     * Starter: 0-19
     * Junior: 20-39
     * Expert: 40-59
     * Master: 60-79
     * <p>
     * Each objective is encoded in terms of:
     * 1 - A string representing the necessary inter-island connections
     * 2 - A string representing the tiles which are already placed on the board when the game starts
     * 3 - An objective problemNumber corresponding to the
     * objective problem number used in the original game.
     */
    static Objective[] OBJECTIVES = {
            new Objective("001120111102112231424233",       // STARTER
                    "b00We01Sd11N",
                    1),
            new Objective("001120112031403102134233",
                    "f02Wb10Ec22E",
                    2),
            new Objective("001120314031110231424233",
                    "f00Wa02We21W",
                    3),
            new Objective("201140311122223342330213",
                    "c20Ea21Ef02W",
                    4),
            new Objective("201140311102112231422233",
                    "d00Ec20E",
                    5),
            new Objective("203111023122021322334233",
                    "c01Wd21W",
                    6),
            new Objective("001120314031110222134233",
                    "e00Ed01W",
                    7),
            new Objective("001120114031021322334233",
                    "c20Ef02W",
                    8),
            new Objective("001120112031403122334233",
                    "f31S",
                    9),
            new Objective("001120114031312222130213",
                    "b02W",
                    10),
            new Objective("201111022031403122334233",
                    "a11S",
                    11),
            new Objective("203140311102314202134233",
                    "b20W",
                    12),
            new Objective("001140311122110231422233",
                    "",
                    13),
            new Objective("001120112031021322334233",
                    "",
                    14),
            new Objective("001120111102021322334233",
                    "",
                    15),
            new Objective("203111023122314202134233",
                    "",
                    16),
            new Objective("001120311102312222132233",
                    "",
                    17),
            new Objective("001120111122314202134233",
                    "",
                    18),
            new Objective("001120314031112202134233",
                    "",
                    19),
            new Objective("001111223142021322134233",
                    "",
                    20),
            new Objective("001120114031110222134233",       // JUNIOR
                    "",
                    21),
            new Objective("001140313122021322334233",
                    "",
                    22),
            new Objective("001120313122314202132233",
                    "",
                    23),
            new Objective("001120314031312222134233",
                    "",
                    24),
            new Objective("001111021122314202134233",
                    "",
                    25),
            new Objective("001120314031312202132233",
                    "",
                    26),
            new Objective("001120314031312231420213",
                    "",
                    27),
            new Objective("201140311122314202134233",
                    "",
                    28),
            new Objective("001120314031312202134233",
                    "",
                    29),
            new Objective("203140311102021322334233",
                    "",
                    30),
            new Objective("001120314031021322334233",
                    "",
                    31),
            new Objective("201140310213221322334233",
                    "",
                    32),
            new Objective("001111224031312202134233",
                    "",
                    33),
            new Objective("001120314031110231223142",
                    "",
                    34),
            new Objective("001120311102314231222213",
                    "",
                    35),
            new Objective("001120114031312202132233",
                    "",
                    36),
            new Objective("001120114031314222334233",
                    "",
                    37),
            new Objective("201120313142021322134233",
                    "",
                    38),
            new Objective("001120314031112222134233",
                    "",
                    39),
            new Objective("403111221102021322134233",
                    "",
                    40),
            new Objective("001120314031110211222233",       // EXPERT
                    "",
                    41),
            new Objective("203140310213221322334233",
                    "",
                    42),
            new Objective("201120314031021322134233",
                    "",
                    43),
            new Objective("001120113122314222334233",
                    "",
                    44),
            new Objective("201140311102312231422213",
                    "",
                    45),
            new Objective("201120311102314222132233",
                    "",
                    46),
            new Objective("201140311122021322134233",
                    "",
                    47),
            new Objective("201111021122403131420213",
                    "",
                    48),
            new Objective("001120111102312202134233",
                    "",
                    49),
            new Objective("001120311122314202134233",
                    "",
                    50),
            new Objective("001120311102112231422213",
                    "",
                    51),
            new Objective("001111023142021322134233",
                    "",
                    52),
            new Objective("001140311102312222334233",
                    "",
                    53),
            new Objective("201140311102312222334233",
                    "",
                    54),
            new Objective("001140311102112231422213",
                    "",
                    55),
            new Objective("001120113142110202134233",
                    "",
                    56),
            new Objective("001120314031021322134233",
                    "",
                    57),
            new Objective("001120112031314202132233",
                    "",
                    58),
            new Objective("110231223142021322334233",
                    "",
                    59),
            new Objective("001120114031314202134233",
                    "",
                    60),
            new Objective("001120113142021322133122",       // MASTER
                    "",
                    61),
            new Objective("001120314031110211224233",
                    "",
                    62),
            new Objective("001120114031314202132233",
                    "",
                    63),
            new Objective("001120314031110231422213",
                    "",
                    64),
            new Objective("201140311122314222132233",
                    "",
                    65),
            new Objective("001120112031403122132233",
                    "",
                    66),
            new Objective("001120314031110202134233",
                    "",
                    67),
            new Objective("001120311122314222132233",
                    "",
                    68),
            new Objective("001120314031112231420213",
                    "",
                    69),
            new Objective("001120114031112231420213",
                    "",
                    70),
            new Objective("201140311102314222132233",
                    "",
                    71),
            new Objective("201120314031110222132233",
                    "",
                    72),
            new Objective("001120313122314202134233",
                    "",
                    73),
            new Objective("001120112031110222134233",
                    "",
                    74),
            new Objective("203140311122314202134233",
                    "",
                    75),
            new Objective("001140311102312202134233",
                    "",
                    76),
            new Objective("001120313122021322134233",
                    "",
                    77),
            new Objective("001120114031312202134233",
                    "",
                    78),
            new Objective("001120311122021322334233",
                    "",
                    79),
            new Objective("203140311102314202132233",
                    "",
                    80),
    };

    /**
     * Given the two parts of a game objective and a problem number, constructs an `Objective` object
     *
     * @param connected     A string representing the pairs of islands that must
     *                      be connected.
     * @param initialState  A string representing the list of initial tile placements
     * @param problemNumber The problem number from the original board game,
     *                      a value from 1 to 80.
     */
    public Objective(String connected, String initialState, int problemNumber) {

        assert problemNumber >= 1 && problemNumber <= 80;
        this.connectedIslands = connected;
        this.initialState = initialState;
        this.problemNumber = problemNumber;
    }

    /**
     * Choose a new objective, given a difficulty level.
     * <p>
     * The method should select a randomized objective from the 80 pre-defined solutions,
     * being sure to select an objective with the correct level of difficulty.
     * <p>
     * (See the comments above the declaration of the OBJECTIVES array.)
     * <p>
     * So, for example, if the difficulty is 0 (starter), then this function should use a randomized
     * index between 0 and 19 (inclusive) to return an objective from the OBJECTIVES array that is
     * level 0 difficulty.  On the other hand, if the difficulty is 3 (master), then this function
     * should use a randomized index between 60 and 79 (inclusive) to return an objective from the
     * OBJECTIVES array that is level 3 difficulty.
     * <p>
     * The original code below simply returns OBJECTIVES[0], which neither respects the difficulty
     * (it always returns a level 0 objective), nor is it randomized (it always returns the same
     * objective).
     *
     * @param difficulty The difficulty of the game (0 - starter, 1 - junior, 2 - expert, 3 - master)
     * @return An objective at the appropriate level of difficulty.
     */
    public static Objective newObjective(int difficulty) {
        assert difficulty >= 0 && difficulty <= 3;
        Random ran = new Random();
        Objective obj = null;
        switch (difficulty) {
            case 0:
                obj = OBJECTIVES[ran.nextInt(20)];
                break;
            case 1:
                obj = OBJECTIVES[20 + ran.nextInt(20)];
                break;
            case 2:
                obj = OBJECTIVES[40 + ran.nextInt(20)];
                break;
            case 3:
                obj = OBJECTIVES[60 + ran.nextInt(20)];
        }
        return obj;
    }

    public String getConnectedIslands() {
        return connectedIslands;
    }

    public String getInitialState() {
        return initialState;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public static Objective getObjective(int index) {
        return OBJECTIVES[index];
    }

    public static Objective[] getOBJECTIVES() {
        return OBJECTIVES;
    }
}
