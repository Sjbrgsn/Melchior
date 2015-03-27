package gui;

import controllers.GameConstants;
import controllers.GameController;
import enemies.Direction;
import handlers.SoundHandler;
import towers.BasicTower;
import towers.PlagueTower;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Holmgr 2015-03-04
 * The Frame that holds our GameComponent and handles
 * all menues and such.
 */
public class GameFrame extends JFrame {

    private JLabel healthLabel, cashLabel, roundLabel, counterLabel;
    private JToggleButton muteSoundEffectsButton, muteMusicButton;

    private JButton buyBasicTowerButton, buyPlagueTowerButton,
            upgradeTowerButton, sellTowerButton;
    private GameController controller;

    public GameFrame(GameComponent gameComponent, int health, int cash, GameController controller) throws HeadlessException {
        super("Melchior");

        this.controller = controller;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.add(gameComponent, BorderLayout.CENTER);
        createMenus();
        createStatusPane(health, cash);
        setupBindings();

        this.pack();
        this.setVisible(true);
    }

    private void setupBindings() {

        ActionListener buyBasicTowerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buyTower(BasicTower.class);
            }
        };
        buyBasicTowerButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Q"), "buyBasic");
        buyBasicTowerButton.getActionMap().put("buyBasic", (Action) buyBasicTowerAction);
        buyBasicTowerButton.addActionListener(buyBasicTowerAction);

        ActionListener buyPlagueTowerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buyTower(PlagueTower.class);
            }
        };
        buyPlagueTowerButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "buyPlague");
        buyPlagueTowerButton.getActionMap().put("buyPlague", (Action) buyPlagueTowerAction);
        buyPlagueTowerButton.addActionListener(buyPlagueTowerAction);

        ActionListener upgradeTowerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.upgradeTower();
            }
        };
        upgradeTowerButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("U"), "upgrade");
        upgradeTowerButton.getActionMap().put("upgrade", (Action) upgradeTowerAction);
        upgradeTowerButton.addActionListener(upgradeTowerAction);

        ActionListener sellTowerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sellTower();
            }
        };
        sellTowerButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "sell");
        sellTowerButton.getActionMap().put("sell", (Action) sellTowerAction);
        sellTowerButton.addActionListener(sellTowerAction);
    }

    private void createStatusPane(int health, int cash) {

        JPanel panel = new JPanel();
        panel.setBackground(new Color(69, 69, 69));
        panel.setPreferredSize(new Dimension(140, 640));
        panel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        upperPanel.setBackground(new Color(69, 69, 69));
        panel.add(upperPanel, BorderLayout.NORTH);

        this.healthLabel = new JLabel("Health: " + health);
        this.cashLabel = new JLabel("Cash: " + cash);
        this.roundLabel = new JLabel("Round: 1"); // Starting round is 1

        this.counterLabel = new JLabel("Counter: 0");
        this.counterLabel.setVisible(false);

        upperPanel.add(healthLabel);
        upperPanel.add(cashLabel);
        upperPanel.add(roundLabel);
        upperPanel.add(counterLabel);



        this.muteMusicButton = new JToggleButton();
        muteMusicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameConstants.PLAY_MUSIC = !GameConstants.PLAY_MUSIC;
                if (GameConstants.PLAY_MUSIC) {
                    SoundHandler.getInstance().playMusic();
                }
                else {
                    SoundHandler.getInstance().stopMusic();
                }
            }
        });

        this.muteSoundEffectsButton = new JToggleButton();
        muteSoundEffectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameConstants.PLAY_SOUND_EFFECTS = !GameConstants.PLAY_SOUND_EFFECTS;
            }
        });

        try {
            BufferedImage soundButtons = ImageIO.read(getClass().getResourceAsStream("/images/buttons.png"));

            ImageIcon musicEnabled = new ImageIcon(soundButtons.getSubimage(64, 0, 64, 56));
            ImageIcon musicDisabled = new ImageIcon(soundButtons.getSubimage(0, 0, 64, 56));
            muteMusicButton.setPressedIcon(musicDisabled);
            muteMusicButton.setSelectedIcon(musicDisabled);
            muteMusicButton.setIcon(musicEnabled);
            muteMusicButton.setBorderPainted(false);
            muteMusicButton.setContentAreaFilled(false);

            ImageIcon soundEnabled = new ImageIcon(soundButtons.getSubimage(192, 0, 64, 56));
            ImageIcon soundDisabled = new ImageIcon(soundButtons.getSubimage(128, 0, 64, 56));
            muteSoundEffectsButton.setPressedIcon(soundDisabled);
            muteSoundEffectsButton.setSelectedIcon(soundDisabled);
            muteSoundEffectsButton.setIcon(soundEnabled);
            muteSoundEffectsButton.setBorderPainted(false);
            muteSoundEffectsButton.setContentAreaFilled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }


        this.buyBasicTowerButton = new JButton("Buy Basic Tower");
        this.buyPlagueTowerButton = new JButton("Buy Plague Tower");
        this.upgradeTowerButton = new JButton("Upgrade");
        this.sellTowerButton = new JButton("Sell");

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.setBackground(new Color(69, 69, 69));
        panel.add(lowerPanel, BorderLayout.SOUTH);

        lowerPanel.add(muteMusicButton);
        lowerPanel.add(muteSoundEffectsButton);
        lowerPanel.add(buyBasicTowerButton);
        lowerPanel.add(buyPlagueTowerButton);
        lowerPanel.add(upgradeTowerButton);
        lowerPanel.add(sellTowerButton);

        this.add(panel, BorderLayout.WEST);

    }


    private void createMenus(){
        final JMenu file = new JMenu("File");

        file.addSeparator();
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                System.exit(0); // Kill program
            }
        });

        file.add(exit);

        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        this.setJMenuBar(menuBar);
    }

    public void setCashLabel(int cash) {
        this.cashLabel.setText("Cash: " + cash);
    }

    public void setHealthLabel(int health) {
        this.healthLabel.setText("Health: " + health);
    }

    public void setRoundLabel(int round) {
        this.roundLabel.setText("Round: " + round);
    }

    public void setCounterLabel(int counter) {
        if (counter == 0){
            counterLabel.setVisible(false);
        }
        else {
            counterLabel.setVisible(true);
            counterLabel.setText("Counter: " + counter);
        }
    }
}
