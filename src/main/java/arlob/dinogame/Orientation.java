package arlob.dinogame;

public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    /**
     * Return the single character associated with a `Orientation`, which is the first character of
     * the direction name, as an upper case character ('N', 'E', 'S', 'W')
     *
     * @return A char value equivalent to the `Orientation` enum
     */
    public char toChar() {
        char i = 'N';
        switch (this) {
            case NORTH:
                break;
            case EAST:
                i = 'E';
                break;
            case SOUTH:
                i = 'S';
                break;
            case WEST:
                i = 'W';
                break;
        }
        return i;
    }
}
