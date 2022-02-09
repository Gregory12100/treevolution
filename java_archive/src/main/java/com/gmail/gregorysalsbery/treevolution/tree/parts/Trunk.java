package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;

import java.awt.*;

public class Trunk extends TreePart {

    public Trunk(int gridX, int gridY) {
        super(TreePartType.TRUNK, new Color(80, 50, 50), gridX, gridY, 10);
    }

    public Trunk(GridPoint xy) {
        super(TreePartType.TRUNK, new Color(80, 50, 50), xy.getX(), xy.getY(), 10);
    }
}
