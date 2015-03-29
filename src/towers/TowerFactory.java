package towers;

import controllers.GameController;

/**
 * Created by acrux on 2015-03-29.
 */
public class TowerFactory {
    private GameController controller;

    public TowerFactory(GameController controller){
        this.controller = controller;
    }

    public Tower getTower(TowerType towerType){
        switch (towerType){
            case BASIC:
                return new BasicTower(controller);
            case PLAGUE:
                return new PlagueTower(controller);
            default:
                return null;
        }
    }
}
