package controllers;

import enemies.Enemy;
import enemies.GroundEnemy;
import enemies.GroundEnemyType;
import gui.GameComponent;
import gui.GameFrame;
import pathfinding.AStarSearch;
import pathfinding.Location;
import pathfinding.Path;
import pathfinding.SquareGrid;
import towers.BasicTower;
import towers.Tower;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Holmgr 2015-03-09
 */
public class GameController {

    private GameFrame frame;
    private GameComponent component;

    private SquareGrid grid;
    private static int gridSize = 20;

    private Location defaultStart = new Location(0, 0);
    private Location defaultEnd = new Location(19, 19);

    private ArrayList<Enemy> enemies;
    private ArrayList<Tower> towers;


    private int money;


    private int defaultTickSpeed = 1000/30;

    public GameController() {

        grid = new SquareGrid(gridSize, gridSize);
        money = 1000;

        AStarSearch search = new AStarSearch(grid, defaultStart, defaultEnd);
        Path path = search.createPath();

        towers = new ArrayList<>();

        enemies = new ArrayList<>();
        enemies.add(new GroundEnemy(path, GroundEnemyType.EASY));

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
        
        for(Enemy enemy : enemies){
            enemy.moveStep();
        }
        component.repaint();
    }

    public void buyTower(Location location){

        BasicTower tower = new BasicTower(location);

        if (grid.isPassable(location) && money > tower.getUpgradeCost()){
            grid.getTowers().add(tower.getLocation());
            towers.add(tower);

        }
        component.repaint();
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
}
