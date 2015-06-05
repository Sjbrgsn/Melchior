package enemies;

import pathfinding.Path;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by acrux on 2015-03-18.
 * Class to create all enemies for one level
 */
public class EnemyWave implements Iterable<Enemy>{

    private Path path;
    private int enemyCount;
    private int points;

    public EnemyWave(int level, Path path) {
        this.path = path;

        enemyCount = (int) (Math.pow(level, 0.5) * 10);
        points = (int) (( 1 + (level * ((float)level/5))) * 1000);

    }

    @Override
    public Iterator<Enemy> iterator() {
        return new Iterator<Enemy>() {
            @Override
            public boolean hasNext() {
                return 0 < enemyCount;
            }

            @Override
            public Enemy next() {
                if (enemyCount <= 0) {
                    throw new NoSuchElementException(); // No need for additional information
                }
                Random rnd = new Random();
                Enemy enemy = new GroundEnemy(path, ((points / enemyCount) - points / (enemyCount * 2) + rnd.nextInt(points/(enemyCount + 1))));
                points -= enemy.getHealth();
                enemyCount --;
                return enemy;
            }
        };
    }
}
