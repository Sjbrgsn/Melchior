package enemies;

/**
 * Created by acrux on 2015-03-04.
 * Used for the Observer pattern, all observers to Enemy need to implement this interface
 */
public interface EnemyListener {
    public void onEnemyKilled(Enemy enemy);
    public void onReachedGoal(Enemy enemy);
}
