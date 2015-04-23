package enemies;

import pathfinding.Location;
import pathfinding.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acrux on 2015-03-04.
 * Abstract class for the Enemy interface, implementing
 * base functionality concerning positioning,  health and taking damage.
 */
public abstract class AbstractEnemy implements Enemy {
    protected int health;
    protected int maximumHealth;
    protected double movementSpeed = 0.1; // Default speed
    protected int killReward;

    protected double x, y;
    protected Location target;

    protected Path currentPath;
    protected Direction currentDirection;

    private List<EnemyListener> listeners;

    protected AbstractEnemy(Path path, int health, double movementSpeed) {
        this.currentPath = path;
        this.health = health;
        this.movementSpeed = movementSpeed;

        listeners = new ArrayList<>();

        maximumHealth = health;
        killReward = maximumHealth / 5;

        setPosition(currentPath.getStartLocation());

        this.target = currentPath.getNext(currentPath.getStartLocation());
        this.currentDirection = Direction.DOWN; // Default direction
    }


    private void setPosition(Location location) {
        this.x = location.x;
        this.y = location.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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
    public int getMaximumHealth() {
        return maximumHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean isAlive() {
        return 0 < health;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public int getKillReward() {
        return killReward;
    }

    private void notifyKilled(){
        for (EnemyListener el : listeners)
            el.onEnemyKilled(this);
    }

    protected void notifyReachedGoal(){
        for (EnemyListener el : listeners)
            el.onReachedGoal(this);
    }

}
