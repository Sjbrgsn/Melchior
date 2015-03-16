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
    private Location startLocation;

    public Path(final Map<Location, Location> path, Location start) {
        this.path = path;
        this.startLocation = start;
    }

    public Location getFirst(){
        return startLocation;
    }

    public Location getNext(Location currentLocation){
        return path.get(currentLocation);
    }

    public boolean isGoal(Location currentLocation){
        return path.get(currentLocation) == null;
    }
}
