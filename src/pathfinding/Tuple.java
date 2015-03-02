package pathfinding;

/**
 * Created by Holmgr 2015-03-02
 * Base immutable Tuple of two generic elements.
 */
public class Tuple<A, B> {
    public final A a;
    public final B b;

    public Tuple(final A a, final B b) {
	this.a = a;
	this.b = b;
    }
}
