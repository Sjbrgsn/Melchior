package gui;

import controllers.GameController;
import towers.BasicTower;
import towers.PlagueTower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Holmgr 2015-03-04
 * The Frame that holds our GameComponent and handles
 * all menues and such.
 */
public class GameFrame extends JFrame {

    private JLabel healthLabel, cashLabel;
    private JButton buyBasicTowerButton, buyPlagueTowerButton,
            upgradeTowerButton, sellTowerButton;
    private GameController controller;

    public GameFrame(GameComponent gameComponent, int health, int cash, GameController controller) throws HeadlessException {
        super("Melchior");

        this.controller = controller;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.add(gameComponent, BorderLayout.EAST);
        createMenus();
        createStatusPane(health, cash);
        setupBindings();

        this.pack();
        this.setVisible(true);
    }

    private void setupBindings() {

        buyBasicTowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buyTower(BasicTower.class);
            }
        });

        buyPlagueTowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buyTower(PlagueTower.class);
            }
        });

        upgradeTowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.upgradeTower();
            }
        });

        sellTowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sellTower();
            }
        });
    }

    private void createStatusPane(int health, int cash) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        panel.add(upperPanel, BorderLayout.NORTH);

        this.healthLabel = new JLabel("Health: " + health);
        this.cashLabel = new JLabel("Cash: " + cash);

        upperPanel.add(this.healthLabel, BorderLayout.NORTH);
        upperPanel.add(this.cashLabel, BorderLayout.SOUTH);

        this.buyBasicTowerButton = new JButton("Buy Basic Tower");
        this.buyPlagueTowerButton = new JButton("Buy Plague Tower");
        this.upgradeTowerButton = new JButton("Upgrade");
        this.sellTowerButton = new JButton("Sell");

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        panel.add(lowerPanel, BorderLayout.SOUTH);

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

}
