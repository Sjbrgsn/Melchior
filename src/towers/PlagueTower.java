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

    private int damage = 5;

    public PlagueTower(GameController controller) {
        super(controller);
        cooldown = 10;
        range = 2;
        upgradeCost = 100;
    }

    @Override
    public void fire() {
        int max = Math.min(controller.allEnemiesInRange(getLocation(), range).size(), 2);
        for (Enemy enemy: controller.allEnemiesInRange(getLocation(), range).subList(0,max)){
            enemy.takeDamage(damage);
        }
    }
}
