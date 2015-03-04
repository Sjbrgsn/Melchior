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
    private SquareGrid grid = new SquareGrid(20, 20);

    public GameComponent() {
	this.setOpaque(true); // Needed for background color to show
    }

    @Override
        // Needs to ignore superclass, want specific dimens
        public Dimension getPreferredSize() {
            return new Dimension(800, 640);
        }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);


    }
}
