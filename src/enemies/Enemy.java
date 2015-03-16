package enemies;

/**
 * Created by acrux on 2015-03-04.
 */
public interface Enemy {
    public int getHealth();

    public int getMaximumHealth();

    public boolean isAlive();

    public void moveStep();

    public void takeDamage(int damage);

    public double getX();

    public double getY();

    public void addEnemyListener(EnemyListener el);

}