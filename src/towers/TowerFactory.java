package towers;

import controllers.GameController;

/**
 * Created by holmgr on 01/04/15.
 * Interface used to establish the Factory method pattern
 */
public interface TowerFactory {
    public Tower createTower(GameController controller);
}
