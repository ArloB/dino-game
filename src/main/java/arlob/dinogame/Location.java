package arlob.dinogame;

public class Location {
    private final int X;
    private final int Y;
    static final int OUT = -1;

    public Location(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public Location() {
        this.X = OUT;
        this.Y = OUT;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    @Override
    public String toString() {
        return this.X + this.Y + "";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Location obj = (Location) o;
        return this.X == obj.X && this.Y == obj.Y;
    }

    public int hashCode() {
        return (int) (1000 * ((float) this.X / this.Y));
    }
}
