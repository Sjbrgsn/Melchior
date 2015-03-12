package controllers;

import enemies.Enemy;
import enemies.EnemyListener;
import enemies.GroundEnemy;
import enemies.GroundEnemyType;
import gui.GameComponent;
import gui.GameFrame;
import pathfinding.*;
import towers.BasicTower;
import towers.Tower;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Holmgr 2015-03-09
 */
public class GameController implements EnemyListener{

    private GameFrame frame;
    private GameComponent component;

    private SquareGrid grid;
    private static int gridSize = 20;

    private Location defaultStart = new Location(0, 0);
    private Location defaultEnd = new Location(19, 19);
    private Path path;

    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> enemiesToBeRemoved = new ArrayList<>();
    private ArrayList<Tower> towers;

    private int money = 300; //Used to buy/upgrade towers
    private int health = 20; //Starting health

    private int defaultTickSpeed = 1000/30;

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

        GroundEnemy testEnemy = new GroundEnemy(path, GroundEnemyType.EASY);
        testEnemy.addEnemyListener(this);
        enemies.add(testEnemy); // Path will always be instantiated

        component = new GameComponent(grid, gridSize, enemies, towers, this);
        frame = new GameFrame(component);

        component.setPath(path);

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

        for(Enemy enemy : enemies){
            enemy.moveStep();
        }
        for(Tower tower : towers){
            tower.onTick();
        }
        component.repaint();
    }

    public void buyTower(Location location){

        BasicTower tower = new BasicTower(location, this);

        if (grid.isPassable(location) && money >= tower.getUpgradeCost()){

            grid.getTowers().add(tower.getLocation());

            try{
                Path testPath = new AStarSearch(grid, defaultStart, defaultEnd).createPath();

                money -= tower.getUpgradeCost();
                towers.add(tower);
                path = testPath;
                component.setPath(path);
                component.repaint();
            }
            catch (PathNotFoundException e) {
                grid.getTowers().remove(tower.getLocation());
                e.setLocation(tower.getLocation());
                e.printStackTrace();
            }
        }
    }

    public Enemy getNearestEnemyInRange(Location id, int range){

        Enemy nearest = null;
        double distance = -1; // Placeholder distance if there are no enemies

        for (Enemy enemy : enemies){

            if (nearest == null || distanceToEnemy(enemy, id) < distanceToEnemy(nearest, id)){
                distance = distanceToEnemy(enemy, id);
                nearest = enemy;
            }
        }
        if (distance == -1 || range < distance)
            return null;
        else
            return nearest;
    }

    private double distanceToEnemy(Enemy enemy, Location loc){
        return Math.sqrt(Math.pow(enemy.getPositionX() - loc.x, 2) +
                Math.pow(enemy.getPositionY() - loc.y, 2));
    }

    private SquareGrid createArbitaryGrid(){

        grid = new SquareGrid(gridSize, gridSize);

        for (int x = 1; x < 4; x++) {
            for (int y = 7; y < 9; y++) {
                grid.getWalls().add(new Location(x, y));
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        new GameController();
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
            System.out.println("Enemy reached goal, health:" + health);
            removeEnemy(enemy);
        }
    }

    private void removeEnemy(Enemy enemy) {
        assert enemies.contains(enemy);
        enemiesToBeRemoved.add(enemy);
    }
}
