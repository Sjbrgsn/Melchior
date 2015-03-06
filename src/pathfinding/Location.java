package pathfinding;

/**
 * Created by Holmgr 2015-03-02
 * Base datatype containing a pair x,y coordinates which is immutable.
 */
public class Location {
    public final int x,y;

    public Location(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override public int hashCode() {
        return 10000 * y + x; // Unique due to x,y never exceeding 10000
    }

    @Override public boolean equals(final Object obj) {
        return hashCode() == obj.hashCode();
    }

    @Override public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
