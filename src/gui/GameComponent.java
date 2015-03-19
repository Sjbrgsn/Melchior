package gui;

import controllers.GameController;
import enemies.Direction;
import enemies.Enemy;
import pathfinding.Location;
import pathfinding.Path;
import pathfinding.SquareGrid;
import towers.PlagueTower;
import towers.Projectile;
import towers.Tower;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Holmgr 2015-03-04
 * The view in our MVC pattern, responsible for drawing
 * the gameboard, the enemies, towers and so on.
 */
public class GameComponent extends JComponent{

    private int cellSize;
    private int gridSize;
    private final SquareGrid grid;
    private Path path;

    private Iterable<Enemy> enemies;
    private Iterable<Tower> towers;
    private Iterable<Projectile> projectiles;

    private GameController controller;
    private ImageIcon background;
    private ImageIcon plagueTowerImage;
    private Map<Direction, Image> enemyImageMap = new EnumMap<>(Direction.class);

    private List<Image> basicTowerImages = new ArrayList<>();
    private int basicTowerImageCounter = 0;

    private List<Image> flagImages = new ArrayList<>();
    private int flagImageCounter = 0; // Needed for animating the goal flag

    public GameComponent(final SquareGrid grid, int gridSize, Iterable<Enemy> enemies,
                         Iterable<Tower> towers, Iterable<Projectile> projectiles, GameController controller) {
        this.gridSize = gridSize;
        this.grid = grid;
        this.enemies = enemies;
        this.towers = towers;
        this.projectiles = projectiles;
        this.controller = controller;

        this.setDoubleBuffered(true);

        setupImageMappings();
        setupBindings();
    }

    private void setupImageMappings() {

        background = new ImageIcon(getClass().getClassLoader().getResource("images/snow.png"));
        plagueTowerImage = new ImageIcon(getClass().getClassLoader().getResource("images/plague_tower.png"));

        try {
            BufferedImage enemyImage = ImageIO.read(getClass().getResourceAsStream("/images/easy_enemy.png"));
            enemyImageMap.put(Direction.UP, enemyImage.getSubimage(40, 0, 40, 40));
            enemyImageMap.put(Direction.LEFT, enemyImage.getSubimage(0, 40, 40, 40));
            enemyImageMap.put(Direction.RIGHT, enemyImage.getSubimage(80, 40, 40, 40));
            enemyImageMap.put(Direction.DOWN, enemyImage.getSubimage(40, 80, 40, 40));

            BufferedImage flagImage = ImageIO.read(getClass().getResourceAsStream("/images/flag.png"));

            int n = 0;
            while (n < flagImage.getWidth()){
                flagImages.add(flagImage.getSubimage(n, 0, 20, 21));
                n += 20;
            }

            BufferedImage basicTowerImage = ImageIO.read(getClass().getResourceAsStream("/images/basic_tower.png"));

            int imageCount = 7;
            int imageWidth = 40;
            int imageHeight = 42;
            for (n = 0; n < imageCount; n++) {
                basicTowerImages.add(basicTowerImage.getSubimage(n * imageWidth, 0, imageWidth, imageHeight));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBindings() {


        // Mouse events
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Location loc = new Location(e.getX() / cellSize, e.getY() / cellSize);
                controller.setSelectedLocation(loc);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    @Override
    // Needs to ignore superclass, want specific dimens
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        cellSize = getHeight() / gridSize;

        g2d.clearRect(0, 0, getWidth(), getHeight()); // To prevent overlapping from last frame

        drawBackground(g2d);
        drawGrid(g2d);
        drawPath(g2d);
        drawWalls(g2d);
        drawTowers(g2d);
        drawEnemies(g2d);
        drawProjectiles(g2d);
        drawGoalFlag(g2d);

        Location loc = controller.getSelectedLocation();
        g2d.setColor(Color.RED);

        if (loc != null){
            g2d.drawRect(loc.x * cellSize, loc.y * cellSize, cellSize, cellSize);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawGoalFlag(Graphics2D g2d) {
        Location goal = path.getGoal();
        int ticksPerAnimation = 4;

        g2d.drawImage(flagImages.get(flagImageCounter / ticksPerAnimation),
                goal.x * cellSize, goal.y * cellSize, cellSize, cellSize, null);

        if (flagImageCounter < flagImages.size() * ticksPerAnimation -1)
            flagImageCounter++;
        else
            flagImageCounter = 0;
    }

    private void drawBackground(Graphics2D g2d) {

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                g2d.drawImage(background.getImage(), i * cellSize, j * cellSize, cellSize, cellSize, null);
            }
        }
    }

    private void drawProjectiles(Graphics2D g2d) {

        g2d.setColor(Color.YELLOW);

        for (Projectile projectile : projectiles){
            g2d.drawOval((int) (projectile.getX() * cellSize), (int) (projectile.getY() * cellSize),
                    (int) (projectile.getRadius() * cellSize), (int) (projectile.getRadius() * cellSize));
        }
    }

    private void drawTowers(Graphics2D g2d) {

        g2d.setColor(Color.RED);

        for (Tower tower : towers){
            Location loc = tower.getLocation();
            int ticksPerAnimation = 5;

            if (tower instanceof PlagueTower){
                g2d.drawImage(plagueTowerImage.getImage(), loc.x * cellSize, loc.y * cellSize,
                        cellSize, cellSize, null);
            }
            else {
                g2d.drawImage(basicTowerImages.get(basicTowerImageCounter / ticksPerAnimation),
                    loc.x * cellSize, loc.y * cellSize, cellSize, cellSize, null);

                if (basicTowerImageCounter < basicTowerImages.size() * ticksPerAnimation -1)
                    basicTowerImageCounter++;
                else
                    basicTowerImageCounter = 0;
            }

        }
    }

    private void drawEnemies(Graphics2D g2d) {

        for(Enemy enemy : enemies){
            g2d.drawImage(enemyImageMap.get(enemy.getDirection()), (int) (enemy.getX() * cellSize), (int) (enemy.getY() * cellSize),
                    cellSize, cellSize, null);
            drawHealthBar(g2d, enemy);
        }
    }

    private void drawHealthBar(Graphics2D g2d, Enemy enemy){

        int healthBarHeight = cellSize/8;

        g2d.setColor(Color.RED);
        g2d.fillRect((int) (enemy.getX() * cellSize), (int) ((enemy.getY() - 0.5) * cellSize),
                cellSize, healthBarHeight);

        g2d.setColor(Color.GREEN);
        g2d.fillRect((int) (enemy.getX() * cellSize), (int) ((enemy.getY() - 0.5) * cellSize),
                cellSize * enemy.getHealth() / enemy.getMaximumHealth(), healthBarHeight);

    }

    private void drawWalls(final Graphics2D g2d) {

        g2d.setColor(Color.YELLOW);
        for (Location wall : grid.getWalls()) {
            g2d.fillRect(wall.x * cellSize, wall.y * cellSize, cellSize, cellSize);
        }
    }

    private void drawPath(final Graphics2D g2d) {

        // No path to be drawn
        if (path == null)
            return;

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

    public void setPath(Path path) {
        this.path = path;
    }
}
