package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.app.grid.GridPoint;
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

    private GridPoint xy;

    private Seed seed;

    List<TreePart> treeParts;

    public Tree(int seedX, int seedY) {
        this.xy = new GridPoint(seedX, seedY);

        this.seed = new Seed(this, seedX, seedY);
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
        treeParts.add(new TrunkPart(this, xy.getX(), xy.getY()+1));
        treeParts.add(new TrunkPart(this, xy.getX(), xy.getY()+2));
        treeParts.add(new Leaf(this, xy.getX(), xy.getY()+3));
    }

    public void growHigher() {
        for(TreePart treePart : treeParts) {
            treePart.moveUp();
        }
        treeParts.add(new TrunkPart(this, xy.getX(), xy.getY()+1));
    }


}
