package enemies;

import pathfinding.Location;

import java.awt.*;
import java.util.*;

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

    private java.util.List<EnemyListener> listeners;

    protected AbstractEnemy(Path path, int cellSize) {
        listeners = new ArrayList<EnemyListener>();

        this.cellSize = cellSize;
        this.currentPath = path;

        this.position = locationToPixels(currentPath.getFirst());
        this.target = currentPath.getNext(currentPath.getFirst());

    }

    @Override
    public void moveStep() {
        Point targetInPixels = locationToPixels(target);
        if (position.equals(targetInPixels)){
            target = currentPath.getNext(target);
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

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (!isAlive())
            notifyKilled();
    }

    @Override
    public void addEnemyListener(EnemyListener el) {
        listeners.add(el);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean isAlive() {
        return 0 >= health;
    }

    private void notifyKilled(){
        for (EnemyListener el : listeners)
            el.onEnemyKilled();
    }
}
