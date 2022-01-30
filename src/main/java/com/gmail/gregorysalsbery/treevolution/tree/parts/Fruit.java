package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;

import java.awt.*;

public class Fruit extends TreePart {

    // another tree part
    // drop new seeds?
    // maybe trees have limited life span time
    // those that are able to grow the most fruit get the most chance to pass on treenome?
    // could even start new trees where the seeds landed
    // could do continuous sim instead of discrete generations...

    // fruit can only grow on the bottom side of leaves
    // fruit takes a very large amount of energy to grow
    // number of fruit is a huge part of tree scoring

    public Fruit(int gridX, int gridY) {
        super(TreePartType.FRUIT, new Color(200, 40, 40), gridX, gridY, 1);
    }

    public Fruit(GridPoint xy) {
        super(TreePartType.FRUIT, new Color(200, 40, 40), xy.getX(), xy.getY(), 1);
    }
}
