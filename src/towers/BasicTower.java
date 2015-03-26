package towers;

import controllers.GameConstants;
import controllers.GameController;
import handlers.SoundHandler;
import pathfinding.Location;

/**
 * Created by acrux on 2015-03-10.
 */
public class BasicTower extends AbstractTower {

    private int baseDamage;

    public BasicTower(Location location, GameController controller) {
        super(location, controller);
        cooldown = 20;
        range = 3; // Range at which enemies can be engaged
        baseDamage = 10;
    }

    @Override
    public void fire() {
        controller.spawnProjectile(getLocation(), nearestEnemy, 0.8,
                (int) (baseDamage * (1 + (double) getUpgradeLevel()/2)), range);

        if (GameConstants.PLAY_SOUND_EFFECTS){
            SoundHandler.getInstance().playShotFired();
        }
    }
}
