package towers;

import pathfinding.Location;

/**
 * Created by Holmgr 2015-03-06
 */
public interface Tower {
    public void fire();
    public void onTick();

    public int sell();
    public void upgrade();

    public int getUpgradeCost();
    public int getUpgradeLevel();
    public Location getLocation();
    public void setLocation(Location location);
}
