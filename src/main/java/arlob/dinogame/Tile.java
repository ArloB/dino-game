package arlob.dinogame;

import static arlob.dinogame.Orientation.*;

public class Tile implements Comparable<Tile> {

    private final TileType tileType;         // Which tile type is it (a ... f)
    private final Location location;         // The tile's current location on board
    private final Orientation orientation;   // The tile's current orientation

    public Tile(String placement) {
        this.tileType = TileType.valueOf(Character.toString((placement.charAt(0) - 32)));
        this.orientation = placementToOrientation(placement);
        this.location = placementToLocation(placement);
    }

    public Location getLocation() {
        return location;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public TileType getTileType() {
        return tileType;
    }

    /**
     * Given a four-character tile placement string, decode the tile's orientation.
     * <p>
     * You will need to read the description of the encoding in the class `Objective`.
     *
     * @param placement A string representing the placement of a tile on the game board
     * @return A value of type `Orientation` corresponding to the tile's orientation on board
     */
    public static Orientation placementToOrientation(String placement) {
        Orientation i = NORTH;
        switch (placement.charAt(3)) {
            case 'E':
                i = EAST;
                break;
            case 'S':
                i = SOUTH;
                break;
            case 'W':
                i = WEST;
                break;
        }
        return i;
    }

    /**
     * Given a four-character tile placement string, decode the tile's location.
     * <p>
     * You will need to read the description of the encoding in the class `Objective'
     *
     * @param placement A string representing the placement of a tile on the game board
     * @return A value of type `Location` corresponding to the tile's location on the board
     */
    public static Location placementToLocation(String placement) {
        return new Location(placement.charAt(1) - '0', placement.charAt(2) - '0');
    }

    public String toString() {
        return tileType.toString().toLowerCase() + location.getX() + location.getY() + orientation.toString().charAt(0) + "";
    }

    public int compareTo(Tile t) {
        return this.tileType.ordinal() - t.tileType.ordinal();
    }
}
