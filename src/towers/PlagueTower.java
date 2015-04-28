package towers;

import controllers.GameController;
import enemies.Enemy;

/**
 * Created by Holmgr 2015-03-16
 * Simple AOE (Area of Effect) tower damaging all enemies in its range
 */
public class PlagueTower extends AbstractTower{
    private final static int COOLDOWN = 10;
    private final static int RANGE = 2;
    private final static int UPGRADE_COST = 100;

    private int damage = 5;

    public PlagueTower(GameController controller) {
        super(controller, COOLDOWN, RANGE, UPGRADE_COST);
    }

    @Override
    public void fire() {
        int max = Math.min(controller.allEnemiesInRange(getLocation(), range).size(), 2);
        for (Enemy enemy: controller.allEnemiesInRange(getLocation(), range).subList(0,max)){
            enemy.takeDamage(damage + (getUpgradeLevel() * 5));
        }
    }
}
