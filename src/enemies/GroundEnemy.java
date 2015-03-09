package enemies;

import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 */
public class GroundEnemy extends AbstractEnemy{

    public GroundEnemy(Path path, GroundEnemyType type) {
        super(path);
        health = 100;
        type = type;

    }

    @Override
    public void moveStep() {

        int targetX = target.x;
        int targetY = target.y;

        if (Math.abs(targetX - x) <= movementSpeed && Math.abs(targetY - y) <= movementSpeed){
            x = targetX;
            y = targetY;
            target = currentPath.getNext(target);
            if (target == null)
                notifyReachedGoal();
        }

        else if (x < targetX)
            x += movementSpeed;

        else if (x > targetX)
            x -= movementSpeed;

        else if (y < targetX)
            y -= movementSpeed;
        else
            y += movementSpeed;
    }
}
