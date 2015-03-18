package enemies;

import pathfinding.Path;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by acrux on 2015-03-18.
 */
public class BasicEnemyFactory implements Iterable<Enemy>{

    private int totalPoints;
    private int currentPoints;
    private Path path;

    public BasicEnemyFactory(int totalPoints, Path path) {
        this.totalPoints = totalPoints;
        this.currentPoints = totalPoints;
        this.path = path;

    }

    @Override
    public Iterator<Enemy> iterator() {
        Iterator<Enemy> it = new Iterator<Enemy>() {
            @Override
            public boolean hasNext() {
                return 0 < currentPoints;
            }

            @Override
            public Enemy next() {
                Random rnd = new Random();
                GroundEnemyType type = GroundEnemyType.values()[rnd.nextInt(GroundEnemyType.values().length)];
                Enemy enemy = new GroundEnemy(path, type);
                currentPoints -= enemy.getHealth();

                return enemy;
            }
        };
        return it;
    }
}
