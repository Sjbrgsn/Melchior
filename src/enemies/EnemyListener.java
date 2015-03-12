package enemies;

/**
 * Created by acrux on 2015-03-04.
 */
public interface EnemyListener {
    public void onEnemyKilled(Enemy enemy);
    public void onReachedGoal(Enemy enemy);
}
