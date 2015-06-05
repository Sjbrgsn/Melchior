package gui;

import controllers.GameConstants;
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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Holmgr 2015-03-04
 * The view in our MVC pattern, responsible for drawing the gameboard, the enemies, towers and so on.
 * Also redirects keyboard and mouse events to GameController and loads it's own image files.
 */
public class GameComponent extends JComponent{

    /**
     * Default grid SIZE (px)
     */
    public static final int GRID_SIZE = 640;
    private int cellSize;
    private final SquareGrid grid;
    private Path path = null;

    private Iterable<Enemy> enemies;
    private Iterable<Tower> towers;
    private Iterable<Projectile> projectiles;

    private GameController controller;
    private ImageIcon background = null;
    private ImageIcon plagueTowerImage = null;
    private Map<Direction, Image> enemyImageMap = new EnumMap<>(Direction.class);

    private List<Image> basicTowerImages = new ArrayList<>();
    private int basicTowerImageCounter = 0;

    private List<Image> flagImages = new ArrayList<>();
    private int flagImageCounter = 0; // Needed for animating the goal flag

    public GameComponent(final SquareGrid grid, Iterable<Enemy> enemies,
                         Iterable<Tower> towers, Iterable<Projectile> projectiles, GameController controller) {

        this.grid = grid;
        this.enemies = enemies;
        this.towers = towers;
        this.projectiles = projectiles;
        this.controller = controller;

        this.setDoubleBuffered(true);

        setupImageMappings();
        setupBindings();
    }

    /**
     * Attempts to load images using resource stream returning a new ImageIcon,
     * throws FileNotFoundException if image does not exist.
     * Can also throw a general IOException.
     */
    private ImageIcon createImageIcon(String path) throws IOException, FileNotFoundException {
        try (InputStream imageStream = getClass().getResourceAsStream(path)) {
            if (imageStream != null) {
                return new ImageIcon(ImageIO.read(imageStream));
            }
        }
        throw new FileNotFoundException("failed loading " + path);
    }

    private void setupImageMappings() {

        try {

            background = createImageIcon("/images/snow.png");
            plagueTowerImage = createImageIcon("/images/plague_tower.png");

            final int enemyImageSize = 40; // Size of subimage in spritesheet for enemy
            BufferedImage enemyImage = ImageIO.read(getClass().getResourceAsStream("/images/easy_enemy.png"));
            enemyImageMap.put(Direction.UP, enemyImage.getSubimage(enemyImageSize, 0, enemyImageSize, enemyImageSize));
            enemyImageMap.put(Direction.LEFT, enemyImage.getSubimage(0, enemyImageSize, enemyImageSize, enemyImageSize));
            enemyImageMap.put(Direction.RIGHT, enemyImage.getSubimage(2 * enemyImageSize, enemyImageSize, enemyImageSize, enemyImageSize));
            enemyImageMap.put(Direction.DOWN, enemyImage.getSubimage(enemyImageSize, 2 * enemyImageSize, enemyImageSize, enemyImageSize));

            BufferedImage flagImage = ImageIO.read(getClass().getResourceAsStream("/images/flag.png"));
            // Dimensions of subimage in spritesheet for the flag
            final int flagImageWidth = 20;
            final int flagImageHeight = 21;
            final int flagImageCount = 5;
            for (int i = 0; i < flagImageCount; i++) {
                flagImages.add(flagImage.getSubimage(i * flagImageWidth, 0, flagImageWidth, flagImageHeight));
            }

            BufferedImage basicTowerImage = ImageIO.read(getClass().getResourceAsStream("/images/basic_tower.png"));
            // Dimensions of subimage in spritesheet for basic tower
            final int towerImageCount = 7;
            final int towerImageWidth = 40;
            final int towerImageHeight = 42;
            for (int i = 0; i < towerImageCount; i++) {
                basicTowerImages.add(basicTowerImage.getSubimage(i * towerImageWidth, 0, towerImageWidth, towerImageHeight));
            }

        } catch (IOException e) {
            e.printStackTrace(); // Failed to load some images
            JOptionPane.showMessageDialog(this, "Failed to load images resources");
            System.exit(0);
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

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "MoveLeft");
        actionMap.put("MoveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.moveSelected(-1, 0);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "MoveRight");
        actionMap.put("MoveRight", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.moveSelected(1, 0);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("UP"), "MoveUp");
        actionMap.put("MoveUp", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.moveSelected(0, -1);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "MoveDown");
        actionMap.put("MoveDown", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.moveSelected(0, 1);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("P"), "PAUSE/UNPAUSE");
        actionMap.put("PAUSE/UNPAUSE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.switchPause();
            }
        });

    }

    @Override
    // Needs to ignore superclass, want specific dimens
    public Dimension getPreferredSize() {
        return new Dimension(GRID_SIZE, GRID_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        cellSize = getHeight() / GameConstants.GRID_SIZE;

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
        Location goal = path.getGoalLocation();
        int ticksPerAnimation = 4;

        g2d.drawImage(flagImages.get(flagImageCounter / ticksPerAnimation),
                goal.x * cellSize, goal.y * cellSize, cellSize, cellSize, null);

        if (flagImageCounter < flagImages.size() * ticksPerAnimation -1)
            flagImageCounter++;
        else
            flagImageCounter = 0;
    }

    private void drawBackground(Graphics2D g2d) {

        for (int i = 0; i < GameConstants.GRID_SIZE; i++) {
            for (int j = 0; j < GameConstants.GRID_SIZE; j++) {
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

        int ticksPerAnimation = 8;
        for (Tower tower : towers){
            Location loc = tower.getLocation();

            if (tower instanceof PlagueTower){
                g2d.drawImage(plagueTowerImage.getImage(), loc.x * cellSize, loc.y * cellSize,
                        cellSize, cellSize, null);
            }
            else {
                g2d.drawImage(basicTowerImages.get(basicTowerImageCounter / ticksPerAnimation),
                    loc.x * cellSize, loc.y * cellSize, cellSize, cellSize, null);

            }

        }
        if (basicTowerImageCounter < basicTowerImages.size() * ticksPerAnimation -1)
            basicTowerImageCounter++;
        else
            basicTowerImageCounter = 0;
    }

    private void drawEnemies(Graphics2D g2d) {

        for(Enemy enemy : enemies){
            g2d.drawImage(enemyImageMap.get(enemy.getCurrentDirection()), (int) (enemy.getX() * cellSize), (int) (enemy.getY() * cellSize),
                    cellSize, cellSize, null);
            drawHealthBar(g2d, enemy);
        }
    }

    private void drawHealthBar(Graphics2D g2d, Enemy enemy){

        int healthBarHeight = cellSize/8;


        // Want to center healthbar over each enemy, therefor 0.5
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

        Location current = path.getStartLocation();
        Location next = path.getNext(current);

        while (next != null){
            // Want to draw path in the middle of grid cell, therefor 0.5
            g2d.drawLine((int) ((current.x + 0.5) * cellSize), (int) ((current.y + 0.5) * cellSize),
                    (int) ((next.x + 0.5) * cellSize), (int) ((next.y + 0.5) * cellSize));

            current = path.getNext(current);
            next = path.getNext(current);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        for (int row = 0; row < GameConstants.GRID_SIZE; row++) {
            for (int col = 0; col < GameConstants.GRID_SIZE; col++) {
                g2d.drawLine(col * cellSize, row * cellSize, col * cellSize, getHeight());
            }
            g2d.drawLine(0, row * cellSize, getWidth(), row * cellSize);
        }
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
