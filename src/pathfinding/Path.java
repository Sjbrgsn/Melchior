package pathfinding;

import java.util.HashMap;

public class Path {

    private HashMap<Location,Location> path;

    public Path(final HashMap<Location, Location> path) {
	this.path = path;
    }

    public Location getFirst(){
	return path.get(path.keySet().toArray()[0]);
    }

    public Location next(Location currentLocation){
	return path.get(currentLocation);
    }

    public boolean isGoal(Location currentLocation){
	return path.get(currentLocation) == null;
    }
}
