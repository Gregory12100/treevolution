package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;

import java.awt.*;

public class Branch extends TreePart {

    public Branch(int gridX, int gridY) {
        super(TreePartType.BRANCH, new Color(80, 50, 50), gridX, gridY, 8);
    }

    public Branch(GridPoint xy) {
        super(TreePartType.BRANCH, new Color(80, 50, 50), xy.getX(), xy.getY(), 8);
    }
}
