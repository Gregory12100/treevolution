package com.gmail.gregorysalsbery.treevolution.app;

import com.gmail.gregorysalsbery.treevolution.generation.Generation;
import com.gmail.gregorysalsbery.treevolution.tree.dna.Treenome;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import com.gmail.gregorysalsbery.treevolution.environment.Dirt;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.CodeSigner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel implements ActionListener {

    // controls the delay between each tick in ms
    private final int DELAY = 17;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    private Generation generation;
    private List<Dirt> dirts;

    private Random rand = new Random();

    private long prevTime;

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y));
        // set the game board background color
        setBackground(Config.BACK_COLOR);

        dirts = createGround(Config.GROUND_DEPTH);
        generation = new Generation(10);

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();

        prevTime = System.nanoTime();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // get time between loops
        long currentTime = System.nanoTime();
        float dt = (currentTime - prevTime) / 1000000000F;
        prevTime = currentTime;

        generation.update(dt);

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

            // draw our graphics.
            drawBackground(g);

        for(Dirt dirt : dirts) {
            dirt.draw(g);
        }

        generation.draw(g);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    private List<Dirt> createGround(int depth) {
        List<Dirt> dirtList = new ArrayList<>();

        for(int j=0; j<depth; j++) {
            for (int i=0; i<Config.GRID_SIZE_X; i++) {
                dirtList.add(new Dirt(i, j));
            }
        }

        return dirtList;
    }

    private void drawBackground(Graphics g) {
        g.setColor(Config.BACK_COLOR);
        g.fillRect(0, 0, Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y);
    }
}
