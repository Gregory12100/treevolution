package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class BranchPart extends TreePart {

    public BranchPart(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.BRANCH, new Color(80, 50, 50), gridX, gridY, 8);
    }
}
