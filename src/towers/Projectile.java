package towers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by acrux on 2015-03-10.
 */
public class Projectile {
    private int damage;
    private double xSpeed, ySpeed;
    private double totalSpeed;
    private double targetX, targetY;
    private double x, y;

    private Collection<ProjectileListener> listeners = new ArrayList<>();
    private double distanceTraveled = 0;
    private int range;

    private final static double RADIUS = 0.2; // Easier to monitor if field, default RADIUS

    public Projectile(double speed, int damage, double x, double y, double targetX, double targetY, int range) {
        this.damage = damage;
        this.targetX = targetX;
        this.targetY = targetY;
        this.x = x;
        this.y = y;
        this.range = range;
        this.totalSpeed = speed;
        calculateVectorComponents(speed);
    }

    public void moveStep(){
        x += xSpeed;
        y += ySpeed;

        distanceTraveled += totalSpeed;
        if (distanceTraveled >= range){
            notifyReachedRangeLimit();
        }
    }

    private void calculateVectorComponents(double speed){

        double angle = Math.atan2(targetY - y, targetX - x);
        xSpeed = speed * Math.cos(angle);
        ySpeed = speed * Math.sin(angle);
    }

    public void addProjectileListener(ProjectileListener pl){
        listeners.add(pl);
    }

    private void notifyReachedRangeLimit(){
        for (ProjectileListener pl : listeners){
            pl.onReachedRangeLimit(this);
        }
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
        return RADIUS;
    }
}
