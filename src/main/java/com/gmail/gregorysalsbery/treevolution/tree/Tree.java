package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.environment.Sun;
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

    private int energy = 5000;
    private int water;
    private int food;

    private GridPoint xy;

    Treenome treenome;
    List<TreePart> treeParts;

    Sun sun;

    // TODO: get current height, depth, and width in both directions of tree

    public Tree(Treenome treenome, int seedX, int seedY, Sun sun) {
        this.xy = new GridPoint(seedX, seedY);

        this.treenome = treenome;
        this.treeParts = new ArrayList<>();

        this.sun = sun;
    }

    public void draw(Graphics g) {
        for(TreePart treePart : treeParts) {
            treePart.draw(g);
        }
    }

    public void growHigher() {
        for(TreePart treePart : treeParts) {
            if(treePart.getType() == TreePartType.LEAF || treePart.getType() == TreePartType.BRANCH || treePart.getType() == TreePartType.FRUIT) {
                treePart.translateUp();
            }
        }
    }

    public void grow() {
        if(energy >= 1000) {
            TreePart nextGrowth = treenome.getNextGrowth();
            if(nextGrowth == null) {
                return;
            }

            nextGrowth.translate(xy.getX(), xy.getY());
            nextGrowth.setParentTree(this);
            treeParts.add(nextGrowth);

            // let the sun know about this part so it can sort it into a light column
            sun.registerPart(nextGrowth);

            // move everything up if we just grew a trunk
            if(nextGrowth.getType() == TreePartType.TRUNK) {
                growHigher();
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
