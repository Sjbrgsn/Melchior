package enemies;

import pathfinding.Location;

import java.util.*;
import java.util.List;

import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 */
public abstract class AbstractEnemy implements Enemy{
    private int health;
    private double movementSpeed = 0.1; // Defualt speed
    private double x, y;
    private Location target;

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

	if ((int) x == targetX && (int) y == targetY)
	    target = currentPath.getNext(target);

  	if (x < targetX)
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

    public void takeDamage(int damage) {
        health -= damage;
        if (!isAlive())
            notifyKilled();
    }

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
