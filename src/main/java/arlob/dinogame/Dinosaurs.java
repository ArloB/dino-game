package arlob.dinogame;

import java.util.*;

import static arlob.dinogame.Orientation.*;
import static arlob.dinogame.State.*;

public class Dinosaurs implements Cloneable {

    /* The objective represents the problem to be solved in this instance of the game. */
    private final Objective objective;

    /*
     * States at each of the board's 20 locations (corners), initialized
     * to represent the empty board.  EMPTY states may be overwritten
     * by non-empty states (RED, GREEN), when a tile in that state is
     * placed at the same corner.
     *
     * Notice that this is initialized with all perimeter states, and
     * all inner locations that must be water are initialized as water
     * and all inner locations that must be land are initialized as
     * empty.
     */
    private State[][] boardstates = {
            {EMPTY, WATER, EMPTY, WATER, EMPTY},
            {WATER, EMPTY, WATER, EMPTY, WATER},
            {EMPTY, WATER, EMPTY, WATER, EMPTY},
            {WATER, EMPTY, WATER, EMPTY, WATER}
    };

    /*
     * Tiles occupying the board.   Indices refer to the square
     * to the lower-right of the given location (remember, locations
     * refer to corners, not to tiles).   So (0,0) refers to the
     * square in the top-left corner, (3,0) refers to the sqaure
     * in the top-right corner, (0,2) refers to the square in the
     * bottom-left corner, and (3,2) refers to the square in the
     * bottom-right corner.
     *
     * Each entry in the array points to the Tile instance occupying
     * the square, or null if the square is empty.   Since tiles
     * are two squares big, each placed tile should have two array
     * entries referring to it.
     *
     * Since this data structure only reflects placed tiles, it is
     * initially empty (all entries are null).
     */
    private Tile[][] tiles = new Tile[3][4];
    private HashMap<TileType, Tile> tile_list = new HashMap<>();

    /**
     * Construct a game with a given objective
     *
     * @param objective The objective of this game.
     */
    public Dinosaurs(Objective objective) {
        this.objective = objective;
    }

    /**
     * Construct a game for a given level of difficulty.
     * This chooses a new objective and creates a new instance of
     * the game at the given level of difficulty.
     *
     * @param difficulty The difficulty of the game.
     */
    public Dinosaurs(int difficulty) {
        this(Objective.newObjective(difficulty));
    }

    public Dinosaurs clone() {
        Dinosaurs d;

        try {
            d = (Dinosaurs) super.clone();
        } catch (CloneNotSupportedException e) {
            d = new Dinosaurs(this.objective);
        }

        d.tile_list = new HashMap<>();

        for (Map.Entry<TileType, Tile> e : this.tile_list.entrySet()) {
            Tile t = new Tile(e.getValue().toString());
            d.tile_list.put(t.getTileType(), t);
        }

        d.tiles = Arrays.stream(this.tiles).map(Tile[]::clone).toArray(Tile[][]::new);
        d.boardstates = Arrays.stream(this.boardstates).map(State[]::clone).toArray(State[][]::new);

        return d;
    }

    public Objective getObjective() {
        return objective;
    }

    /**
     * @param boardState A string consisting of 4*N characters, representing
     *                   initial tile placements (initial game state).
     */
    public void initializeBoardState(String boardState) {
        for (int i = 0; i < boardState.length() / 4; i++) {
            String placement = boardState.substring(i * 4, ((i + 1) * 4));
            addTileToBoard(placement);
        }
    }

    ///**
    // * Print out the board state.   May be useful for debugging.
    // */
    //private void printBoardState() {
    //    for (int r = 0; r < 4; r++) {
    //        for (int c = 0; c < 5; c++) {
    //            System.out.print((boardstates[r][c] == null ? ' ' : boardstates[r][c].name().charAt(0)) + " ");
    //        }
    //        System.out.println();
    //    }
    //}
//
    ///**
    // * Print out tile state.   May be useful for debugging.
    // */
    //private void printTileState() {
    //    for (int r = 0; r < 3; r++) {
    //        for (int c = 0; c < 4; c++) {
    //            System.out.print(tiles[r][c] == null ? '.' : tiles[r][c].getTileType().name().charAt(0) + "");
    //        }
    //        System.out.println();
    //    }
    //}

    /**
     * Check whether a tile placement fits inside the game board.
     *
     * @param placement A String representing a tile placement.
     * @return True if the tile is completely within the board, and false otherwise.
     */
    public static boolean isPlacementOnBoard(String placement) {
        int x = Character.getNumericValue(placement.charAt(1));
        int y = Character.getNumericValue(placement.charAt(2));
        boolean r = true;

        switch (placement.charAt(3)) {
            case 'N':
            case 'S':
                r = y >= 0 && y < 2;
                break;
            case 'E':
            case 'W':
                r = x >= 0 && x < 3;
                break;
        }

        return r;
    }


    /**
     * Add a new tile placement to the board state, updating
     * all relevant data structures accordingly.  If you add
     * additional data structures, you will need to update
     * this.
     *
     * @param placement The placement to add.
     */
    public void addTileToBoard(String placement) {
        /* create the tile, and figure out its location and orientation */
        Tile tile = new Tile(placement);

        /* update the tile data structure for the two squares that compose this tile */
        updateTiles(tile);

        /* update the states for each of the six points on the tile */
        updateBoardStates(tile);

        tile_list.put(tile.getTileType(), tile);
    }

    public void updateTileOnBoard(String placement) {
        Tile new_tile = new Tile(placement);
        Tile old_tile = tile_list.get(new_tile.getTileType());
        tile_list.put(new_tile.getTileType(), new_tile);

        removeTile(old_tile);

        updateTiles(new_tile);
        updateBoardStates(new_tile);
    }

    /**
     * Update the tile data structure with a new tile placement.
     * <p>
     * Each entry in the data structure corresponds to a square, and
     * each tile is composed of two squares.   So each time a tile
     * is added, two entries in the data structure need to be updated
     * to point to the new tile.
     * <p>
     * Squares that are covered by a tile will have their data structure
     * entry point to the covering tile.
     * <p>
     * Squares that are not covered by a tile will point to null.
     *
     * @param tile The tile being placed
     */
    private void updateTiles(Tile tile) {
        Location location = tile.getLocation();
        Orientation orientation = tile.getOrientation();
        tiles[location.getY()][location.getX()] = tile;

        if (orientation == NORTH || orientation == SOUTH) {
            tiles[location.getY() + 1][location.getX()] = tile;  // vertical orientation
        } else {
            tiles[location.getY()][location.getX() + 1] = tile;  // horizontal orientation
        }
    }

    private void removeTile(Tile tile) {
        Location location = tile.getLocation();
        Orientation orientation = tile.getOrientation();
        tiles[location.getY()][location.getX()] = null;

        if (orientation == NORTH || orientation == SOUTH) {
            tiles[location.getY() + 1][location.getX()] = null;  // vertical orientation
        } else {
            tiles[location.getY()][location.getX() + 1] = null;  // horizontal orientation
        }

        for (int i = 0; i < offsetCount(tile, true); i++) {
            for (int j = 0; j < offsetCount(tile, false); j++) {
                State c = getLocationState(tile.getLocation(), j, i);
                State s = tile.getTileType().stateFromOffset(j, i, tile.getOrientation());

                if ((c == RED || c == GREEN) && c == s) {
                    boardstates[tile.getLocation().getY() + i][tile.getLocation().getX() + j] = EMPTY;
                }
            }
        }
    }

    public void removeTile(char tile) {
        Tile t = tile_list.get(new Tile(tile + "00N").getTileType());

        if (t != null)
            removeTile(t);
    }


    /**
     * Check whether a proposed tile placement overlaps with any previous
     * placements.
     * <p>
     * You will need to use both the placement about to be made, and
     * the existing board state (specifically, the tiles data structure),
     * which is kept up to date when addTileToBoard is called.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return False if the proposed tile placement does not overlap with
     * the already placed tiles, and True if there is any overlap.
     */
    public boolean doesPlacementOverlap(String placement) {
        int x = Character.getNumericValue(placement.charAt(1));
        int y = Character.getNumericValue(placement.charAt(2));
        boolean r = false;

        switch (placement.charAt(3)) {
            case 'N':
            case 'S':
                if (tiles[y][x] != null || tiles[y + 1][x] != null) {
                    r = true;
                }
                break;
            case 'E':
            case 'W':
                if (tiles[y][x] != null || tiles[y][x + 1] != null) {
                    r = true;
                }
                break;
        }
        return r;
    }


    /**
     * Update the boardstates data structure due to a valid (correct) new
     * tile placement.
     * <p>
     * Each point in the boardstates data structure corresponds to one of
     * the twenty board locations, each of which is a place where the corner
     * of a tile may be placed (see the game description for a diagram, and
     * more information).
     * <p>
     * Each entry in the boardstates data structure is a State, which may
     * be null (unassigned), WATER, EMPTY, GREEN, or RED.
     * <p>
     * When a valid tile placement is made, some locations may change.   For
     * example null locations may become non-null due to a tile placement,
     * and some EMPTY locations may become GREEN or RED.
     * <p>
     * Notice that when a tile is placed, six locations will change, since
     * a tile covers two squares, it will affect two locations at each end
     * and two locations in its middle.  The affected locations will depend
     * on the location of the tile (its top-left corner), and the tile's
     * orientation.
     *
     * @param tile The tile being placed
     */
    private void updateBoardStates(Tile tile) {
        for (int i = 0; i < offsetCount(tile, true); i++) {
            for (int j = 0; j < offsetCount(tile, false); j++) {
                State c = getLocationState(tile.getLocation(), j, i);

                if (c == EMPTY || c == null) {
                    boardstates[tile.getLocation().getY() + i][tile.getLocation().getX() + j] = tile.getTileType().stateFromOffset(j, i, tile.getOrientation());
                }
            }
        }
    }

    /**
     * Given an island location, return its current state.
     * <p>
     * The current state of an island is the state of the dinosaur(s)
     * which are directly or indirectly connected to the island.
     * <p>
     * For example, after applying the placement string "c00N",
     * the islands at location(0,0) and location(1,1) become RED,
     * as they are connected to a RED dinosaur.
     * <p>
     * When an island is not connected to any dinosaurs,
     * its state is EMPTY.
     *
     * @param location A location on the game board.
     * @return An object of type `enum State`, representing
     * the given location.
     */
    public State getLocationState(Location location) {
        return boardstates[location.getY()][location.getX()];
    }

    public State getLocationState(Location location, int offsetX, int offsetY) {
        return boardstates[location.getY() + offsetY][location.getX() + offsetX];
    }

    /**
     * Check whether the locations of land and water on a tile placement
     * are consistent with its surrounding tiles or board, given the current
     * state of the board due to previous placements.
     * <p>
     * Important: The test for this method is not concerned with dinosaur color.
     * Thus it is simply a matter of ensuring that water meets water and land
     * meets land (whether or not the land is occupied by a dinosaur).
     * <p>
     * For example, the placement string "a00N" is not consistent
     * since it puts the water at top-left of tile 'a', next to the island
     * at the top-left of the game board.
     * <p>
     * You will need to use both the placement about to be made, and
     * the existing board state which is kept up to date when
     * addTileToBoard is called.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement is consistent with the board and placed
     * tiles, and False if it is inconsistent.
     */
    public boolean isPlacementConsistent(String placement) {
        Tile tile = new Tile(placement);
        Location tile_loc = tile.getLocation();

        int count = 0;

        for (int i = 0; i < offsetCount(tile, true); i++) {
            for (int j = 0; j < offsetCount(tile, false); j++) {
                State s1 = tile.getTileType().stateFromOffset(j, i, tile.getOrientation());
                State s2 = getLocationState(tile_loc, j, i);

                if ((s1 == WATER && s2 == WATER) || (s1 != WATER && s2 != WATER)) {
                    count++;
                }
            }
        }

        return count == 6;
    }

    public int offsetCount(Tile tile, boolean isY) {
        boolean weast = tile.getOrientation() == WEST || tile.getOrientation() == EAST;

        weast = isY != weast;

        return weast ? 3 : 2;
    }

    /**
     * Check whether a tile placement would cause a collision between
     * green and red dinosaurs.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement would cause a collision
     * between red and green dinosaurs.
     */
    public boolean isPlacementDangerous(String placement) {
        Tile tile = new Tile(placement);
        Location tile_loc = tile.getLocation();

        int count = 0;

        if (!isPlacementConsistent(placement)) {
            return true;
        }

        for (int i = 0; i < offsetCount(tile, true); i++) {
            for (int j = 0; j < offsetCount(tile, false); j++) {
                State s = tile.getTileType().stateFromOffset(j, i, tile.getOrientation());
                State s2 = getLocationState(tile_loc, j, i);

                if (s == GREEN) {
                    if (s2 != RED) count++;
                } else if (s == RED) {
                    if (s2 != GREEN) count++;
                } else {
                    count++;
                }
            }
        }

        return count != 6;
    }

    /**
     * Check whether the given tile placement violates the game objective,
     * specifically:
     * <p>
     * 1 - All required island connections are either connected or not occupied,
     * and
     * 2 - All other pairs of islands are either disconnected or not occupied
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement violates the game objective,
     * and false otherwise.
     */
    public boolean violatesObjective(String placement) {
        String req_conn = this.objective.getConnectedIslands();

        if (isPlacementDangerous(placement)) {
            return true;
        }

        Tile tile = new Tile(placement);
        Location tile_loc = tile.getLocation();

        HashSet<Location> found_land = new HashSet<>();

        for (int i = 0; i < offsetCount(tile, true); i++) {
            for (int j = 0; j < offsetCount(tile, false); j++) {
                State s = tile.getTileType().stateFromOffset(j, i, tile.getOrientation());

                if (s == RED || s == GREEN) {
                    found_land.add(new Location(tile_loc.getX() + j, tile_loc.getY() + i));
                }
            }
        }

        HashSet<Location> req_land = new HashSet<>();

        int minX = placement.charAt(1) - '0';
        int maxX = minX + offsetCount(tile, false);
        int minY = placement.charAt(2) - '0';
        int maxY = minY + offsetCount(tile, true);

        for (int i = 0; i < req_conn.length(); i += 4) {
            String isl = req_conn.substring(i, i + 4);

            int x1 = isl.charAt(0) - '0';
            int y1 = isl.charAt(1) - '0';
            int x2 = isl.charAt(2) - '0';
            int y2 = isl.charAt(3) - '0';

            if (x1 >= minX && x1 < maxX && y1 >= minY && y1 < maxY && x2 >= minX && x2 < maxX && y2 >= minY && y2 < maxY) {
                req_land.add(new Location(x1, y1));
                req_land.add(new Location(x2, y2));
            }
        }


        if (!found_land.containsAll(req_land)) {
            return true;
        }

        return found_land.size() > 1 && !req_land.containsAll(found_land);
    }

    public boolean validPlacement(String placement) {
        return isPlacementOnBoard(placement) && !doesPlacementOverlap(placement) && !violatesObjective(placement);
    }

    /**
     * Given a target location, find the set of actions which:
     * 1 - occupy the target location
     * 2 - satisfy all of the game requirements(e.g. objectives)
     *
     * @param targetLoc A location (x,y) on the game board.
     * @return A set of strings, each representing a tile placement
     */
    public Set<String> findCandidatePlacements(Location targetLoc) {
        Set<String> hS = new TreeSet<>();
        String[] t = {"a", "b", "c", "d", "e", "f"}, o = {"N", "E", "S", "W"};

        String loc = targetLoc.getX() + "" + targetLoc.getY();

        for (String tile : t) {
            for (String orientation : o) {
                String tmp = tile + loc + orientation;

                if (validPlacement(tmp) && !tile_list.containsKey(TileType.valueOf(tile.toUpperCase()))) {
                    hS.add(tmp);
                }
            }
        }

        return hS;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        tile_list.values().stream().sorted().forEach(str::append);

        return str.toString();
    }

    /**
     * Find the solutions to the game (the current Dinosaurs object).
     *
     * @return A set of strings, each representing a placement of all tiles,
     * which satisfies all of the game objectives.
     */
    public Set<String> getSolutions() {
        Dinosaurs d = new Dinosaurs(this.objective);

        LinkedHashSet<String> sols = new LinkedHashSet<>();

        recsol(d, this.objective.getInitialState(), sols);

        return sols;
    }

    private void recsol(Dinosaurs d, String s, LinkedHashSet<String> sols) {
        Dinosaurs d_local = d.clone();

        if (s.length() > 0) {
            for (int i = 0; i < s.length(); i += 4) {
                d_local.addTileToBoard(s.substring(i, i + 4));
            }
        }

        if (d_local.tile_list.size() == 6) {
            sols.add(d_local.toString());
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (tiles[i][j] != null)
                        continue;

                    Location l = new Location(j, i);

                    for (String t : d_local.findCandidatePlacements(l)) {
                        recsol(d_local, t, sols);
                    }
                }
            }
        }
    }
}
