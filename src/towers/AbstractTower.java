package towers;

import pathfinding.Location;

/**
 * Created by Holmgr 2015-03-06
 */
public abstract class AbstractTower implements Tower{

    private Location location;
    private int upgradeLevel = 0;
    private int upgradeCost = 100;

    public AbstractTower(final Location location) {
	this.location = location;
    }

    @Override public int sell() {
	return (int)(upgradeCost * 0.75);
    }

    @Override public void upgrade() {
	upgradeLevel++;
	upgradeCost += 200; // Arbritary increase per level
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
