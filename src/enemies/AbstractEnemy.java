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
    protected int reward;

    protected double x, y;
    protected Location target;

    protected Path currentPath;
    protected Direction currentDirection;

    private List<EnemyListener> listeners;

    public AbstractEnemy(Path path) {
        listeners = new ArrayList<>();

        this.currentPath = path;
        setPosition(currentPath.getFirst());

        this.target = currentPath.getNext(currentPath.getFirst());
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
    public Direction getDirection() {
        return currentDirection;
    }

    @Override
    public int getKillReward() {
        return reward;
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
