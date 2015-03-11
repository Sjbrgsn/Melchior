package pathfinding;

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
