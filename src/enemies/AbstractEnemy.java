package enemies;

import pathfinding.Location;

import java.awt.*;
import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 */
public abstract class AbstractEnemy implements Enemy{
    private int health;
    private int movementSpeed;
    private int cellSize;
    private Point position;
    private Location target;

    private Path currentPath;

    protected AbstractEnemy(Path path, int cellSize) {
        this.cellSize = cellSize;
        this.currentPath = path;

        this.position = locationToPixels(currentPath.getFirst());
        this.target = currentPath.getNext(currentPath.getFirst());

    }

    @Override
    public void moveStep() {
        Point targetInPixels = locationToPixels(target);
        if (position.equals(targetInPixels)){
            // TODO
        }
        if (position.x < targetInPixels.x)
            position.x += movementSpeed;

        else if (position.x > targetInPixels.x)
            position.x -= movementSpeed;

        else if (position.y < targetInPixels.y)
            position.y += movementSpeed;

        else
            position.y -= movementSpeed;

    }

    private Point locationToPixels(Location location){
        return new Point(location.x * cellSize + cellSize/2, location.y * cellSize + cellSize/2);
    }

    public Point getPosition(){
        return position;
    }

    private void notifyKilled(){

    }
}
