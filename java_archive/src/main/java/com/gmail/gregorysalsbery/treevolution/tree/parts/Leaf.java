package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class Leaf extends TreePart {

    public Leaf(int gridX, int gridY) {
        super(TreePartType.LEAF, new Color(10, 120, 50), gridX, gridY, 2);
    }

    public Leaf(GridPoint xy) {
        super(TreePartType.LEAF, new Color(10, 120, 50), xy.getX(), xy.getY(), 2);
    }
}
