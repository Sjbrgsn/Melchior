package pathfinding;

/**
 * Created by Holmgr 2015-03-02
 * Base immutable Tuple of two generic elements.
 */
public class Tuple<A, B> {
    public final A x;
    public final B y;

    public Tuple(final A x, final B y) {
        this.x = x;
        this.y = y;
    }
}
