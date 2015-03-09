package gui;

import pathfinding.AStarSearch;
import pathfinding.Location;
import pathfinding.Path;
import pathfinding.SquareGrid;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Holmgr 2015-03-04
 * The view in our MVC pattern, responsible for drawing
 * the gameboard, the enemies, towers and so on.
 */
public class GameComponent extends JComponent{

    private int cellSize;
    private int gridSize = 20;
    private SquareGrid grid;
    private Path path;

    public GameComponent() {
        this.setOpaque(true); // Needed for background color to show
        grid = new SquareGrid(gridSize, gridSize);

        for (int x = 1; x < 4; x++) {
            for (int y = 7; y < 9; y++) {
                grid.getWalls().add(new Location(x, y));
            }
        }

        AStarSearch search = new AStarSearch(grid, new Location(0, 0), new Location(6, 12));
        path = search.createPath();
    }

    @Override
    // Needs to ignore superclass, want specific dimens
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    @Override protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        cellSize = getHeight() / gridSize;
        drawGrid(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        drawGrid(g2d);
        drawPath(g2d);
        drawWalls(g2d);
    }

    private void drawWalls(final Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);

        for (Location wall : grid.getWalls()) {
            g2d.fillRect(wall.x * cellSize, wall.y * cellSize, cellSize, cellSize);
        }
    }

    private void drawPath(final Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));

        Location current = path.getFirst();
        Location next = path.getNext(current);

        while (next != null){
            g2d.drawLine((int) ((current.x + 0.5) * cellSize), (int) ((current.y + 0.5) * cellSize),
                    (int) ((next.x + 0.5) * cellSize), (int) ((next.y + 0.5) * cellSize));

            current = path.getNext(current);
            next = path.getNext(current);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                g2d.drawLine(col * cellSize, row * cellSize, col * cellSize, getHeight());
            }
            g2d.drawLine(0, row * cellSize, getWidth(), row * cellSize);
        }
    }
}
