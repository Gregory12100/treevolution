package com.gmail.gregorysalsbery.treevolution.tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tree {

    private int energy;
    private int water;
    private int food;

    private int startGridX;
    private int startGridY;

    private Seed seed;

    List<TreePart> treeParts;

    public Tree(int startGridX, int startGridY) {
        this.startGridX = startGridX;
        this.startGridY = startGridY;

        this.seed = new Seed(this, startGridX, startGridY);

        this.treeParts = new ArrayList<TreePart>();
        this.treeParts.add(this.seed);
    }

    public void draw(Graphics g) {
        for(TreePart treePart : treeParts) {
            treePart.draw(g);
        }
    }
}
