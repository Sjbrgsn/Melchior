package towers;

import controllers.GameController;

/**
 * Created by holmgr on 01/04/15.
 */
public class BasicTowerFactory implements TowerFactory{
    @Override
    public Tower createTower(GameController controller) {
        return new BasicTower(controller);
    }
}
