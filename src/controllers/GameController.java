package controllers;

import enemies.*;
import gui.GameComponent;
import gui.GameFrame;
import handlers.SoundHandler;
import pathfinding.*;
import towers.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Holmgr 2015-03-09
 */
public class GameController implements EnemyListener, ProjectileListener{

    private GameState currentState = GameState.BUILD;

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

    private BasicEnemyFactory enemyFactory;
    private int difficulty = 100;
    private int round = 0; // Current round (increments when changing to state RUNNING)

    private int money = 300; //Used to buy/upgrade towers
    private int health = 20; //Starting health

    private int defaultTickSpeed = 1000/30;

    private Location selectedLocation = new Location(0, 0);

    private int spawnDelayCounter = GameConstants.ENEMY_SPAWN_DELAY;
    private int stateDelayCounter = GameConstants.PAUSE_STATE_TIME;

    public GameController() {

        grid = new SquareGrid(gridSize, gridSize);

        AStarSearch search = new AStarSearch(grid, defaultStart, defaultEnd);


        try {
            path = search.createPath();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }

        enemyFactory = new BasicEnemyFactory(difficulty, path);

        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToBeRemoved = new ArrayList<>();
        projectiles = new ArrayList<>();
        projectilesToBeRemoved = new ArrayList<>();

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

        SoundHandler.getInstance().playMusic();

    }

    private void doTick() {

        switch (currentState){
            case RUNNING:
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
                    projectile.moveStep();
                }

                for(Enemy enemy : enemies){
                    enemy.moveStep();
                    doCollisions(enemy);
                }
                for(Tower tower : towers){
                    tower.onTick();
                }

                if(spawnDelayCounter == 0){
                    spawnDelayCounter = GameConstants.ENEMY_SPAWN_DELAY;
                    if (enemyFactory.iterator().hasNext()){
                        Enemy enemy = enemyFactory.iterator().next();
                        enemy.addEnemyListener(this);
                        enemies.add(enemy);
                    }
                    else if (enemies.size() == 0) {
                        currentState = GameState.BUILD;
                        projectiles.clear(); // Remove any lingering projectiles in the air
                        System.out.println("State: BUILD");
                    }
                }
                else
                    spawnDelayCounter--;

                break;
            case PAUSE:
                break;

            case BUILD:
                if (stateDelayCounter == 0){
                    stateDelayCounter = GameConstants.PAUSE_STATE_TIME;
                    difficulty *= GameConstants.DIFFICULTY_INCREASE_FACTOR;
                    enemyFactory = new BasicEnemyFactory(difficulty, path);
                    currentState = GameState.RUNNING;
                    round++;
                    frame.setRoundLabel(round);
                }
                else {
                    stateDelayCounter--;
                    frame.setCounterLabel(stateDelayCounter / defaultTickSpeed);
                }
                break;
            default:
                break;
        }
        gameComponent.repaint();


    }

    /**
     * Creates a new tower of given type (class), inserts into grid only if location
     * is empty and the money >= the cost of the given tower.
     */
    public void buyTower(Class<?> towerType){
        if (currentState != GameState.BUILD){
            return;
        }
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
                towerToBeRemoved = tower;
                break;
            }
        }
        if (towerToBeRemoved != null){
            towers.remove(towerToBeRemoved);
            towerToBeRemoved.sell();
            grid.getTowers().remove(towerToBeRemoved.getLocation());
            try {
                path = new AStarSearch(grid, defaultStart, defaultEnd).createPath();
            } catch (PathNotFoundException e) {
                e.printStackTrace();
            }
            gameComponent.setPath(path);
        }
    }

    public ArrayList<Enemy> allEnemiesInRange(Location id, int range){

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
            if (!projectilesToBeRemoved.contains(proj) && proj.getRadius() + enemy.getSize() >= getDistance(proj.getX(), proj.getY(), enemy.getX(), enemy.getY())){
                projectilesToBeRemoved.add(proj);
                enemy.takeDamage(proj.getDamage());
            }
        }
    }

    private double getDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) +
                Math.pow(y1 - y2, 2));
    }

    public void spawnProjectile(Location id, Enemy enemy, double speed, int damage, int range){
        Projectile proj = new Projectile(speed, damage, id.x, id.y, enemy.getX(), enemy.getY(), range);
        proj.addProjectileListener(this);
        projectiles.add(proj);
    }

    @Override
    public void onEnemyKilled(Enemy enemy) {
        System.out.println("Enemy killed");
        money += enemy.getKillReward();
        frame.setCashLabel(money);
        removeEnemy(enemy);
    }

    @Override
    public void onReachedGoal(Enemy enemy) {
        if (health == 1){
            currentState = GameState.PAUSE; // TODO: Show highscore table
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

    public void moveSelected(int offsetX, int offsetY) {
        Location loc = new Location(selectedLocation.x + offsetX, selectedLocation.y + offsetY);
        if (grid.inBounds(loc)){
            selectedLocation = loc;
        }
    }

    /**
     * Sets the current state of the game, although setting
     * state to BUILD is not allowed (to prevent any type of cheating)
     */
    public void setCurrentState(GameState state){
        if (state != GameState.BUILD){
            currentState = state;
        }
    }

    public void switchPause(){
        if (currentState == GameState.RUNNING) {
            currentState = GameState.PAUSE;
        }
        else {
            currentState = GameState.RUNNING;
        }
    }

    @Override
    public void onReachedRangeLimit(Projectile projectile) {
        assert projectiles.contains(projectile);
        projectilesToBeRemoved.add(projectile);
    }
}
