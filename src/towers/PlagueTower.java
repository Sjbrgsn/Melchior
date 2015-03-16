package towers;

import controllers.GameController;
import enemies.Enemy;
import pathfinding.Location;

/**
 * Created by Holmgr 2015-03-16
 * Simple AOE (Area of Effect) tower damaging
 * all enemies in its range
 */
public class PlagueTower extends AbstractTower{

    private int damage = 10;

    public PlagueTower(Location location, GameController controller) {
        super(location, controller);
        cooldown = 10;
        range = 3;
    }

    @Override
    public void fire() {
        for (Enemy enemy: controller.allEnemiesInRange(getLocation(), range)){
            enemy.takeDamage(damage);
        }
    }
}
