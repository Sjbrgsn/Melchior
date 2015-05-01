package enemies;

import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 * Basic enemy implementing movement functionality.
 */
public class GroundEnemy extends AbstractEnemy{
    private static final double MOVEMENT_SPEED = 0.1;

    public GroundEnemy(Path path, int difficultly) {
        super(path, difficultly, MOVEMENT_SPEED);
    }

    @Override
    public void moveStep() {

        if (Math.abs(target.x - x) <= movementSpeed && Math.abs(target.y - y) <= movementSpeed){
            x = target.x;
            y = target.y;
            
            target = currentPath.getNext(target);

            if (target == null) {
                notifyReachedGoal();
            }

        }

        else if (x < target.x){
            x += movementSpeed;
            currentDirection = Direction.RIGHT;
        }

        else if (x > target.x){
            x -= movementSpeed;
            currentDirection = Direction.LEFT;
        }

        else if (y < target.y){
            y += movementSpeed;
            currentDirection = Direction.DOWN;
        }
        else{
            y -= movementSpeed;
            currentDirection = Direction.UP;
        }
    }

    @Override
    public double getSize() {
        return size;
    }
}
