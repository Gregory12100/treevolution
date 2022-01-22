package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class Seed extends TreePart {

    public Seed(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.SEED, new Color(80, 100, 40), gridX, gridY, 1);
    }
}
