package com.gmail.gregorysalsbery.treevolution.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    // controls the delay between each tick in ms
    private final int DELAY = 17;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

//        private ArrayList<Food> foods;
//        private Generation generation;

    private long prevTime;

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y));
        // set the game board background color
        setBackground(Config.BACK_COLOR);

//            // initialize the game state
//            foods = populateFood(400);
//            generation = new Generation(120);

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

//            // update things
//            for(Food food : foods) {
//                food.update(dt);
//            }
//
//            generation.update(dt);
//
//            // check for collisions
//            for(Creature creature : generation.getCreatures()) {
//                if(creature.getAlive()) {
//                    for(BodyPart bodyPart : creature.getBodyParts()) {
//                        if(bodyPart.isAlive()) {
//                            for (Food food : foods) {
//                                if (Utility.rectangleCollision(bodyPart, food)) {
//                                    food.reset();
//                                    switch (bodyPart.getName()) {
//                                        case "heart" -> bodyPart.hit(10);
//                                        case "skin" -> bodyPart.hit(1);
//                                        case "mouth" -> creature.eat();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

//            // draw our graphics.
//            drawBackground(g);
//
//            for (Food food : foods) {
//                food.draw(g);
//            }
//
//            generation.draw(g);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

//        private void drawBackground(Graphics g) {
//            g.setColor(Config.BACK_COLOR);
//            g.fillRect(0, 0, Config.SCREEN_SIZE_X, Config.SCREEN_SIZE_Y);
//        }
//
//        private ArrayList<Food> populateFood(int numFoods) {
//            ArrayList<Food> foods = new ArrayList<Food>();
//            Random rand = new Random();
//            for (int i = 0; i < numFoods; i++) {
//                foods.add(new Food(rand.nextFloat() * Config.SCREEN_SIZE_X, rand.nextFloat() * Config.SCREEN_SIZE_Y));
//            }
//            return foods;
//        }
}
