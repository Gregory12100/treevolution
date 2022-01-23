package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class Seed extends TreePart {

    public Seed(int gridX, int gridY) {
        super(TreePartType.SEED, new Color(80, 100, 40), gridX, gridY, 1);
    }

    public Seed(GridPoint xy) {
        super(TreePartType.SEED, new Color(80, 100, 40), xy.getX(), xy.getY(), 1);
    }
}
