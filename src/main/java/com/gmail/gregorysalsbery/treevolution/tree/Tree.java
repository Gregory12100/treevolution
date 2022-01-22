package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.tree.parts.Leaf;
import com.gmail.gregorysalsbery.treevolution.tree.parts.Seed;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePart;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TrunkPart;

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

    public void sprout() {
        treeParts.remove(seed);
        treeParts.add(new TrunkPart(this, startGridX, startGridY+1));
        treeParts.add(new TrunkPart(this, startGridX, startGridY+2));
        treeParts.add(new Leaf(this, startGridX, startGridY+3));
    }

    public void growHigher() {
        for(TreePart treePart : treeParts) {
            treePart.goUpOneSpace();
        }
        treeParts.add(new TrunkPart(this, startGridX, startGridY+1));
    }


}
