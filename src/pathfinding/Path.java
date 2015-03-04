package pathfinding;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Holmgr 2015-03-04
 * Holds a sequence of Locations demanded as an opimal path given my the A*
 * algoritm. Each Location is used as a key for getting the next Location
 * in the sequence. The value for the last Location as a key is null.
 */
public class Path {

    private Map<Location, Location> path;

    public Path(final HashMap<Location, Location> path) {
	this.path = path;
    }

    public Location getFirst(){
	return path.get(path.keySet().toArray()[0]);
    }

    public Location getNext(Location currentLocation){
	return path.get(currentLocation);
    }

    public boolean isGoal(Location currentLocation){
	return path.get(currentLocation) == null;
    }
}
