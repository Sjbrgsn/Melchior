package enemies;

import pathfinding.Location;

import java.util.*;

import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 * Abstract class for the Enemy interface, implementing
 * base functionality conserning positioning, moving
 * health and damage.
 */
public abstract class AbstractEnemy implements Enemy{
    protected int health;
    protected double movementSpeed = 0.1; // Defualt speed
    protected double x, y;
    protected Location target;

    private Path currentPath;

    private List<EnemyListener> listeners;

    protected AbstractEnemy(Path path) {
        listeners = new ArrayList<>();

        this.currentPath = path;
        setPosition(currentPath.getFirst());

        this.target = currentPath.getNext(currentPath.getFirst());

    }

    @Override
    public void moveStep() {

        int targetX = target.x;
        int targetY = target.y;

        if (Math.abs(targetX - x) <= movementSpeed && Math.abs(targetY - y) <= movementSpeed){
            x = targetX;
            y = targetY;
            target = currentPath.getNext(target);
        }

        else if (x < targetX)
            x += movementSpeed;

        else if (x > targetX)
            x -= movementSpeed;

        else if (y < targetX)
            y -= movementSpeed;
        else
            y += movementSpeed;
    }

    private void setPosition(Location location){
        this.x = location.x;
        this.y = location.y;
    }

    public double getPositionX(){
        return x;
    }

    public double getPositionY(){
        return y;
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