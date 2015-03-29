package enemies;

import pathfinding.Path;

/**
 * Created by acrux on 2015-03-04.
 * Basic enemy implementing movement functionality. Comes in different types defined by GroundEnemyType
 */
public class GroundEnemy extends AbstractEnemy{

    private int targetX;
    private int targetY;
    private double size = 0.8;

    public GroundEnemy(Path path, GroundEnemyType enemyType) {
        super(path);

        switch (enemyType){
            case EASY:
                health = 100;
                movementSpeed = 0.1;
                break;
            case MEDIUM:
                health = 150;
                movementSpeed = 0.075;
                break;
            case HARD:
                health = 200;
                movementSpeed = 0.05;
        }

        maximumHealth = health;
        reward = maximumHealth / 5;
        //type = enemyType;

        // Set start target values
        targetX = target.x;
        targetY = target.y;

    }

    @Override
    public void moveStep() {

        if (Math.abs(targetX - x) <= movementSpeed && Math.abs(targetY - y) <= movementSpeed){
            x = targetX;
            y = targetY;
            
            target = currentPath.getNext(target);

            if (target == null) {
                notifyReachedGoal();
            }
            else {
                targetX = target.x;
                targetY = target.y;
            }
        }

        else if (x < targetX){
            x += movementSpeed;
            currentDirection = Direction.RIGHT;
        }

        else if (x > targetX){
            x -= movementSpeed;
            currentDirection = Direction.LEFT;
        }

        else if (y < targetY){
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
