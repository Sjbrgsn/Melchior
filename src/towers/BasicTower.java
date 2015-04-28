package towers;

import controllers.GameController;
import handlers.SoundHandler;

/**
 * Created by acrux on 2015-03-10.
 * Basic projectile based tower implementing the fire method, also defines cooldown, range and damage values etc.
 */
public class BasicTower extends AbstractTower {
    private final static int COOLDOWN = 20;
    private final static int RANGE = 3; // Range at which enemies can be engaged
    private final static int UPGRADE_COST = 50;

    private int baseDamage;

    public BasicTower(GameController controller) {
        super(controller, COOLDOWN, RANGE, UPGRADE_COST);
        baseDamage = 10;
    }

    @Override
    public void fire() {
        controller.spawnProjectile(getLocation(), nearestEnemy, 0.8,
                (int) (baseDamage * (1 + (double) getUpgradeLevel()/2)), range);

        SoundHandler.getInstance().playProjectileFired();
    }
}
