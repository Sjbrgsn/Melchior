package towers;

/**
 * Created by acrux on 2015-03-10.
 */
public class Projectile {
    private int damage;
    private double xSpeed, ySpeed;
    private double targetX, targetY;
    private double x, y;

    private double radius = 0.2;

    public Projectile(double speed, int damage, double x, double y, double targetX, double targetY) {
        this.damage = damage;
        this.targetX = targetX;
        this.targetY = targetY;
        this.x = x;
        this.y = y;
        calculateVectorComponents(speed);
    }

    public void moveStep(){
        x += xSpeed;
        y += ySpeed;
    }

    private void calculateVectorComponents(double speed){

        double angle = Math.atan2(targetY - y, targetX - x);
        xSpeed = speed * Math.cos(angle);
        ySpeed = speed * Math.sin(angle);
    }

    public int getDamage(){
        return damage;
    }

    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}
