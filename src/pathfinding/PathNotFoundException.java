package pathfinding;

/**
 * Created by holmgr 2015-03-11
 * Exception thrown when a path connecting the start and end locations was not possible
 * Loc is used for by the controller for specifying the exact location which blocked the path.
 */
public class PathNotFoundException extends Exception{

    private Location location;

    public PathNotFoundException(Location location) {
        this.location = location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    @Override
    public String toString() {
        return super.toString() + " Illegal location: " + location;
    }
}
