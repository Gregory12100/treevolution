package com.gmail.gregorysalsbery.treevolution.generation;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import com.gmail.gregorysalsbery.treevolution.tree.dna.Treenome;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import com.gmail.gregorysalsbery.treevolution.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Generation {

    private List<Tree> trees;

    public Generation(int numTrees) {
        trees = new ArrayList<>();
        for(int i=0; i<numTrees; i++) {
            Treenome treenome = new Treenome("src/main/resources/treena1.csv");
            trees.add(new Tree(treenome, Util.getRandomGridX(), Config.GROUND_DEPTH-1));
        }
    }

    public void update(float dt) {
        for(Tree tree : trees) {
            tree.grow();
        }
    }

    public void draw(Graphics g) {
        for(Tree tree : trees) {
            tree.draw(g);
        }
    }
}
