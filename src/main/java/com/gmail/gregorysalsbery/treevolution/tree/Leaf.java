package com.gmail.gregorysalsbery.treevolution.tree;

import java.awt.*;

public class Leaf extends TreePart {

    public Leaf(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.LEAF, new Color(10, 120, 50), gridX, gridY, 2);
    }
}
