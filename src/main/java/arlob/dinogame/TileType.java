package arlob.dinogame;

import static arlob.dinogame.State.*;

public enum TileType {
    A, B, C, D, E, F;

    /**
     * Return the state for a given board position given x and y offsets
     * from the tile's location and its orientation.  If the position is
     * not covered by the tile, return null.
     * <p>
     * The following indices reflect the four rotations of a tile.   The top
     * left represents the tile location.   The integer indicates the index
     * into the state array for the tile.
     * <p>
     * 0 1     4 2 0   5 4     1 3 5
     * 2 3     5 3 1   3 2     0 2 4
     * 4 5             1 0
     *
     * @param xoff        The x-offset from the tile's location
     * @param yoff        The y-offset from the tile's location
     * @param orientation The orientation of the piece
     * @return The state at the given position given the orientation, or null if
     * not covered by the tile.
     */
    public State stateFromOffset(int xoff, int yoff, Orientation orientation) {
        if (xoff < 0 || yoff < 0 || xoff > 2 || yoff > 2) return null;

        State[] states = statemap[this.ordinal()];

        switch (orientation) {
            case NORTH:
                return xoff == 2 ? null : states[yoff * 2 + xoff];
            case EAST:
                return yoff == 2 ? null : states[(2 - xoff) * 2 + yoff];
            case SOUTH:
                return xoff == 2 ? null : states[(2 - yoff) * 2 + 1 - xoff];
            case WEST:
                return yoff == 2 ? null : states[xoff * 2 + 1 - yoff];
        }

        return null;
    }

    private static final State[][] statemap = {
            {WATER, EMPTY,
                    RED, WATER,
                    WATER, EMPTY},
            {WATER, GREEN,
                    GREEN, WATER,
                    WATER, GREEN},
            {RED, WATER,
                    WATER, RED,
                    EMPTY, WATER},
            {GREEN, WATER,
                    WATER, GREEN,
                    EMPTY, WATER},
            {EMPTY, WATER,
                    WATER, GREEN,
                    GREEN, WATER},
            {WATER, RED,
                    RED, WATER,
                    WATER, EMPTY}
    };
}
