package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.app.Config;

import java.awt.*;

public abstract class TreePart {

    private Tree tree;
    private TreePartType type;

    private int gridX;
    private int gridY;

    private Color color;

    private int health;

    private boolean visible;

    public TreePart(Tree tree, TreePartType type, Color color, int gridX, int gridY, int health) {
        this.tree = tree;
        this.type = type;
        this.color = color;
        this.gridX = gridX;
        this.gridY = gridY;
        this.health = health;

        this.visible = true;
    }

    public void draw(Graphics g) {
        if(visible) {
            g.setColor(color);
            g.drawRect(gridX * Config.cell_size, gridY * Config.cell_size, Config.cell_size, Config.cell_size);
        }
    }
}
