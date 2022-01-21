package com.gmail.gregorysalsbery.treevolution.tree;

import java.awt.*;

public class Trunk extends TreePart {

    public Trunk(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.TRUNK, new Color(80, 50, 50), gridX, gridY, 10);
    }
}
