package com.gmail.gregorysalsbery.treevolution.tree;

import java.awt.*;

public class Branch extends TreePart {

    public Branch(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.BRANCH, new Color(80, 50, 50), gridX, gridY, 8);
    }
}
