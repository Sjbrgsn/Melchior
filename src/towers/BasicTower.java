package towers;

import controllers.GameController;
import handlers.SoundHandler;

/**
 * Created by acrux on 2015-03-10.
 * Basic projectile based tower implementing the fire method, also defines cooldown, range and damage values etc.
 */
public class BasicTower extends AbstractTower {

    private int baseDamage;

    public BasicTower(GameController controller) {
        super(controller);
        cooldown = 20;
        range = 3; // Range at which enemies can be engaged
        baseDamage = 10;
    }

    @Override
    public void fire() {
        controller.spawnProjectile(getLocation(), nearestEnemy, 0.8,
                (int) (baseDamage * (1 + (double) getUpgradeLevel()/2)), range);

        SoundHandler.getInstance().playProjectileFired();
    }
}
