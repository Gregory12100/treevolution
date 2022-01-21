package com.gmail.gregorysalsbery.treevolution.environment;

import com.gmail.gregorysalsbery.treevolution.app.Config;

import java.awt.*;

public class Dirt {

    private int gridX;
    private int gridY;

    private int food;

    private Color color;

    public Dirt(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;

        this.color = new Color(100,80,60);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(gridX * Config.CELL_SIZE, gridY * Config.CELL_SIZE, Config.CELL_SIZE, Config.CELL_SIZE);
    }
}
