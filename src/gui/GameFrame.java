package gui;

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

    public GameFrame(GameComponent gameComponent, int health, int cash) throws HeadlessException {
        super("Melchior");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.add(gameComponent, BorderLayout.EAST);
        createMenus();
        createStatusPane(health, cash);

        this.pack();
        this.setVisible(true);
    }

    private void createStatusPane(int health, int cash) {

        JPanel panel = new JPanel();

        this.healthLabel = new JLabel("Health: " + health);
        this.cashLabel = new JLabel("Cash: " + cash);

        panel.add(this.healthLabel);
        panel.add(this.cashLabel);

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
