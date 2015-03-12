package towers;

/**
 * Created by acrux on 2015-03-10.
 */
public class Projectile {
    private int damage;
    private int speed;
    private double targetX, targetY;
    private double posX, posY;

    public Projectile(double posX, double posY, double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.posX = posX;
        this.posY = posY;
    }
}
