package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class TrunkPart extends TreePart {

    public TrunkPart(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.TRUNK, new Color(80, 50, 50), gridX, gridY, 10);
    }
}
