package controllers;

import gui.GameComponent;
import gui.GameFrame;
import pathfinding.AStarSearch;
import pathfinding.Location;
import pathfinding.Path;
import pathfinding.SquareGrid;

/**
 * Created by Holmgr 2015-03-09
 */
public class GameController {

    private GameFrame frame;
    private GameComponent component;

    private SquareGrid grid;
    private static int gridSize = 20;

    private Location defaultStart = new Location(0, 0);
    private Location defaultEnd = new Location(8, 11);

    public GameController() {

        grid = createArbitaryGrid();
        component = new GameComponent(grid, gridSize);
        frame = new GameFrame(component);

        AStarSearch search = new AStarSearch(grid, defaultStart, defaultEnd);
        Path path = search.createPath();

        component.setPath(path);
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
