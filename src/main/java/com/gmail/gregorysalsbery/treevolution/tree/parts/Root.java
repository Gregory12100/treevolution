package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class Root extends TreePart {

    public Root(Tree tree, int gridX, int gridY) {
        super(tree, TreePartType.ROOT, new Color(100, 40, 50), gridX, gridY, 8);
    }
}
