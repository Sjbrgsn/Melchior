package towers;

import controllers.GameController;
import pathfinding.Location;

/**
 * Created by acrux on 2015-03-10.
 */
public class BasicTower extends AbstractTower {

    public BasicTower(Location location, GameController controller) {
        super(location, controller);
        cooldown = 30;
        range = 5; // Range at which enemies can be engaged
    }

    @Override
    public void fire() {
        controller.spawnProjectile(getLocation(), nearestEnemy);
        System.out.println("Fire!");
    }
}
