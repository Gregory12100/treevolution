package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.app.grid.GridObject;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public abstract class TreePart extends GridObject {

    private Tree tree;
    private TreePartType type;

    private int health;

    public TreePart(Tree tree, TreePartType type, Color color, int gridX, int gridY, int health) {
        super(gridX, gridY, color);

        this.tree = tree;
        this.type = type;
        this.health = health;
    }
}
