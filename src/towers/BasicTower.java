package towers;

import enemies.AbstractEnemy;
import pathfinding.Location;

/**
 * Created by acrux on 2015-03-10.
 */
public class BasicTower extends AbstractTower {

    public BasicTower(Location location) {
        super(location);
        cooldown = 200;
    }

    @Override
    public void fire() {
        System.out.println("Fire!");
    }
}
