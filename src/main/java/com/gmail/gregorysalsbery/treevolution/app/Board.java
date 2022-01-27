package com.gmail.gregorysalsbery.treevolution.app;

import com.gmail.gregorysalsbery.treevolution.util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    private SimHandler simHandler;

    private long prevTime;

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y));
        // set the game board background color
        setBackground(Config.BACK_COLOR);

        simHandler = new SimHandler();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(Config.LOOP_DELAY, this);
        timer.start();

        prevTime = System.nanoTime();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // get time between loops
        long currentTime = System.nanoTime();
        float dt = (currentTime - prevTime) / 1000000000F;
        prevTime = currentTime;

        simHandler.update(dt);

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw our graphics.
        drawBackground(g);

        simHandler.draw(g);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawBackground(Graphics g) {
        g.setColor(Config.BACK_COLOR);
        g.fillRect(0, 0, Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y);
    }
}
