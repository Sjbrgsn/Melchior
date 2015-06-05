package pathfinding;

/**
 * Created by Holmgr 2015-03-02
 * Base immutable Tuple of two generic imutable elements. Public for easier access
 */
public class Tuple<A, B> {
    public final A x;
    public final B y;

    public Tuple(final A x, final B y) {
        this.x = x;
        this.y = y;
    }

    public B getY() {
        return y;
    }

    public A getX() {
        return x;
    }
}
