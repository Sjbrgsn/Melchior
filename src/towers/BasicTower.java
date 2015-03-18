package towers;

import controllers.GameController;
import pathfinding.Location;

/**
 * Created by acrux on 2015-03-10.
 */
public class BasicTower extends AbstractTower {

    private int baseDamage;

    public BasicTower(Location location, GameController controller) {
        super(location, controller);
        cooldown = 30;
        range = 5; // Range at which enemies can be engaged
        baseDamage = 10;
    }

    @Override
    public void fire() {
        controller.spawnProjectile(getLocation(), nearestEnemy, 1,
                (int) (baseDamage * (1 + (double) getUpgradeLevel()/2)));
        System.out.println("Fire!");
    }
}
