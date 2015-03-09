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

    private GameComponent component;

    public GameFrame(GameComponent component) throws HeadlessException {
        super("Melchior");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        this.setLayout(new BorderLayout());
        this.component = component;
        this.add(component);
        createMenus();

        this.setBackground(new Color(85, 161, 196)); // Arbitary background color
        this.pack();
        this.setVisible(true);
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
}
