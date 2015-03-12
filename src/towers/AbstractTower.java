package towers;

import controllers.GameController;
import enemies.Enemy;
import pathfinding.Location;

/**
 * Created by Holmgr 2015-03-06
 */
public abstract class AbstractTower implements Tower{
    private Location location;
    private int upgradeLevel = 0;
    private int upgradeCost = 100;

    protected int cooldown = 100;
    protected int currentCooldown = 0;

    protected int range;
    protected GameController controller;

    protected Enemy nearestEnemy;

    public AbstractTower(final Location location, GameController controller) {
        this.location = location;
        this.controller = controller; // Needed for requesting nearest enemy
    }

    @Override
    public void onTick(){
        if (currentCooldown <= 0) { // Ready to fire
            nearestEnemy = controller.getNearestEnemyInRange(this.location, range);
            if (nearestEnemy != null) {
                fire();
                currentCooldown = cooldown;
            }
        }
        else {
            currentCooldown--;
        }
    }
    @Override public int sell() {
        return (int)(upgradeCost * 0.75);
    }

    @Override public void upgrade() {
        upgradeLevel++;
        upgradeCost += 200; // Arbitrary increase per level
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    @Override public int getUpgradeCost() {
        return upgradeCost;
    }
}
