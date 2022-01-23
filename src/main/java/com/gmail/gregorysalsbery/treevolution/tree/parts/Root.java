package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;

import java.awt.*;

public class Root extends TreePart {

    public Root(int gridX, int gridY) {
        super(TreePartType.ROOT, new Color(100, 40, 50), gridX, gridY, 8);
    }

    public Root(GridPoint xy) {
        super(TreePartType.ROOT, new Color(100, 40, 50), xy.getX(), xy.getY(), 8);
    }
}
