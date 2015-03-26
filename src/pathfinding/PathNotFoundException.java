package pathfinding;

/**
 * Created by holmgr 2015-03-11
 * Exception thrown when a path connecting the start and end locations was not possible
 * Loc is used for by the controller for specifying the exact location which blocked the path.
 */
public class PathNotFoundException extends Exception{

    private Location loc;

    public PathNotFoundException(Location loc) {
        this.loc = loc;
    }

    public void setLocation(Location loc){
        this.loc = loc;
    }

    @Override
    public String toString() {
        return super.toString() + " Illegal location: " + loc;
    }
}
