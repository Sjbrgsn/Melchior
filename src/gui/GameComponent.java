package gui;

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

    public GameComponent() {
        this.setOpaque(true); // Needed for background color to show
        grid = new SquareGrid(gridSize, gridSize);

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
