package controllers;

import enemies.Enemy;
import enemies.EnemyListener;
import enemies.GroundEnemy;
import enemies.GroundEnemyType;
import gui.GameComponent;
import gui.GameFrame;
import pathfinding.*;
import towers.BasicTower;
import towers.PlagueTower;
import towers.Projectile;
import towers.Tower;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holmgr 2015-03-09
 */
public class GameController implements EnemyListener{

    private GameFrame frame;
    private GameComponent gameComponent;

    private SquareGrid grid;
    private static int gridSize = 20;

    private Location defaultStart = new Location(0, 0);
    private Location defaultEnd = new Location(19, 19);
    private Path path = null;

    private ArrayList<Enemy> enemies;
    private List<Enemy> enemiesToBeRemoved;
    private ArrayList<Tower> towers;
    //private Tower towerToBeRemoved;
    private ArrayList<Projectile> projectiles;
    private List<Projectile> projectilesToBeRemoved;

    private int money = 300; //Used to buy/upgrade towers
    private int health = 20; //Starting health

    private int defaultTickSpeed = 1000/30;

    private Location selectedLocation = null;

    public GameController() {

        grid = new SquareGrid(gridSize, gridSize);

        AStarSearch search = new AStarSearch(grid, defaultStart, defaultEnd);

        try {
            path = search.createPath();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }

        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToBeRemoved = new ArrayList<>();
        projectiles = new ArrayList<>();
        projectilesToBeRemoved = new ArrayList<>();

        Enemy testEnemy = new GroundEnemy(path, GroundEnemyType.EASY);
        testEnemy.addEnemyListener(this);
        enemies.add(testEnemy); // Path will always be instantiated

        gameComponent = new GameComponent(grid, gridSize, enemies, towers, projectiles, this);
        gameComponent.setPath(path);

        frame = new GameFrame(gameComponent, health, money, this);


        Timer loopTimer = new Timer(defaultTickSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTick();
            }
        });

        loopTimer.setCoalesce(true);
        loopTimer.start();

    }

    private void doTick() {

        // Remove all enemies which are dead or have reached the goal
        if (!enemiesToBeRemoved.isEmpty()){
            enemies.removeAll(enemiesToBeRemoved);
            enemiesToBeRemoved.clear();
        }
        // Remove all projectiles that hit
        if (!projectilesToBeRemoved.isEmpty()){
            projectiles.removeAll(projectilesToBeRemoved);
            projectilesToBeRemoved.clear();
        }

        for (Projectile projectile : projectiles){
            if (!grid.inBounds(new Location((int) projectile.getX(), (int) projectile.getY()))){ // Remove if outside
                projectilesToBeRemoved.add(projectile);
            }
            else {
                projectile.moveStep();
            }
        }

        for(Enemy enemy : enemies){
            enemy.moveStep();
            doCollisions(enemy);
        }
        for(Tower tower : towers){
            tower.onTick();
        }
        gameComponent.repaint();
    }

    /**
     * Creates a new tower of given type (class), inserts into grid only if location
     * is empty and the money >= the cost of the given tower.
     */
    public void buyTower(Class<?> towerType){

        Tower tower;
        if (towerType == BasicTower.class){
            tower = new BasicTower(selectedLocation, this);
        }
        else {
            tower = new PlagueTower(selectedLocation, this);
        }

        if (selectedLocation != null && grid.isPassable(selectedLocation) && money >= tower.getUpgradeCost()){

            grid.getTowers().add(tower.getLocation());

            try{
                Path testPath = new AStarSearch(grid, defaultStart, defaultEnd).createPath();

                money -= tower.getUpgradeCost();
                towers.add(tower);
                path = testPath;
                gameComponent.setPath(path);
                frame.setCashLabel(money);
                gameComponent.repaint();
            }
            catch (PathNotFoundException e) {
                grid.getTowers().remove(tower.getLocation());
                e.setLocation(tower.getLocation());
                e.printStackTrace();
            }
        }
    }

    public void upgradeTower() {

        for (Tower tower : towers){
            if (tower.getLocation().equals(selectedLocation)) {
                int upgradeCost = tower.getUpgradeCost();

                if (money >= upgradeCost){
                    money -= upgradeCost;
                    tower.upgrade();
                    frame.setCashLabel(money);
                }
                break;
            }
        }
    }

    public void sellTower(){

        Tower towerToBeRemoved = null;

        for (Tower tower : towers){
            if (tower.getLocation().equals(selectedLocation)) {
                tower.sell();
                towerToBeRemoved = tower;
                break;
            }
        }
        if (towerToBeRemoved != null){
            towers.remove(towerToBeRemoved);
        }
    }

    public Iterable<Enemy> allEnemiesInRange(Location id, int range){

        ArrayList<Enemy> enemiesInRange = new ArrayList<>();

        for (Enemy enemy : enemies){

            if (range >= getDistance(enemy.getX(), enemy.getY(), id.x, id.y)){
                enemiesInRange.add(enemy);
            }
        }
        return enemiesInRange;
    }

    public Enemy getNearestEnemyInRange(Location id, int range){

        Enemy nearest = null;
        double distance = -1; // Placeholder distance if there are no enemies

        for (Enemy enemy : enemies){

            double distanceToEnemy = getDistance(enemy.getX(), enemy.getY(), id.x, id.y);
            if (nearest == null || distanceToEnemy < getDistance(nearest.getX(), nearest.getY(), id.x, id.y)){
                distance = distanceToEnemy;
                nearest = enemy;
            }
        }
        if (distance == -1 || range < distance)
            return null;
        else
            return nearest;
    }

    private void doCollisions(Enemy enemy){

        for (Projectile proj : projectiles){
            if (proj.getRadius() + enemy.getSize() >= getDistance(proj.getX(), proj.getY(), enemy.getX(), enemy.getY())){
                projectilesToBeRemoved.add(proj);
                enemy.takeDamage(proj.getDamage());
            }
        }
    }

    private double getDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) +
                Math.pow(y1 - y2, 2));
    }

    public void spawnProjectile(Location id, Enemy enemy, int speed, int damage){
        projectiles.add(new Projectile(speed, damage, id.x, id.y, enemy.getX(), enemy.getY()));
    }

    @Override
    public void onEnemyKilled(Enemy enemy) {
        System.out.println("Enemy killed");
        removeEnemy(enemy);
    }

    @Override
    public void onReachedGoal(Enemy enemy) {
        if (health == 1){
            System.exit(0); // TODO: Show highscore table
        }
        else {
            health--;
            frame.setHealthLabel(health);
            System.out.println("Enemy reached goal, health:" + health);
            removeEnemy(enemy);
        }
    }

    private void removeEnemy(Enemy enemy) {
        assert enemies.contains(enemy);
        enemiesToBeRemoved.add(enemy);
    }

    public static void main(String[] args) {
        new GameController();
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }
}
