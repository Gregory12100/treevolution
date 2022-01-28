package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.dna.Treenome;
import com.gmail.gregorysalsbery.treevolution.tree.parts.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class Tree {

    private int energy = 10000;
    private int water;
    private int food;

    private GridPoint xy;

    Treenome treenome;
    List<TreePart> treeParts;

    // TODO: get current height, depth, and width in both directions of tree

    public Tree(Treenome treenome, int seedX, int seedY) {
        this.xy = new GridPoint(seedX, seedY);

        this.treenome = treenome;
        this.treeParts = new ArrayList<>();
    }

    public void draw(Graphics g) {
        for(TreePart treePart : treeParts) {
            treePart.draw(g);
        }
    }

//    public void sprout() {
//        treeParts.add(new Seed(xy.getX(), xy.getY()));
//        treeParts.add(new Root(xy.getX(), xy.getY()-1));
//        treeParts.add(new Trunk(xy.getX(), xy.getY()+1));
//        treeParts.add(new Trunk(xy.getX(), xy.getY()+2));
//        treeParts.add(new Leaf(xy.getX(), xy.getY()+3));
//    }
//
//    public void growHigher() {
//        for(TreePart treePart : treeParts) {
//            if(treePart.getType() != TreePartType.SEED && treePart.getType() != TreePartType.ROOT) {
//                treePart.moveUp();
//            }
//        }
//        treeParts.add(new Trunk(xy.getX(), xy.getY()+1));
//    }

//    public void matureInstantly() {
//        treeParts = treenome.getAllTreeParts();
//        for(TreePart treePart : treeParts) {
//            treePart.translate(xy.getX(), xy.getY());
//        }
//    }

    public void grow() {
        if(energy >= 1000) {
            TreePart nextGrowth = treenome.getNextGrowth();
            if (nextGrowth != null) {
                nextGrowth.translate(xy.getX(), xy.getY());
                nextGrowth.setParentTree(this);
                treeParts.add(nextGrowth);
            }
            energy -= 1000;
        }
    }

    public void obtainEnergy(int amount) {
        energy += amount;
    }

    public int getSize() {
        return treeParts.size();
    }

    public boolean isFullGrown() {
        return getSize() == treenome.getSize();
    }

    public int getScore() {
        return energy/1000 + treeParts.size();
    }

    @Override
    public String toString() {
        return "Tree(size=" + treeParts.size() + ", energy=" + energy + ", score=" + getScore() + ")";
    }
}
