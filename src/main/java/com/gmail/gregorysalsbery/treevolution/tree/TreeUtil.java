package com.gmail.gregorysalsbery.treevolution.tree;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.dna.TreeNA;
import com.gmail.gregorysalsbery.treevolution.tree.parts.*;

public class TreeUtil {

    public static TreePart createTreePart(TreePartType type, GridPoint xy) {
        switch (type) {
            case SEED -> {
                return new Seed(xy);
            }
            case TRUNK -> {
                return new Trunk(xy);
            }
            case ROOT -> {
                return new Root(xy);
            }
            case BRANCH -> {
                return new Branch(xy);
            }
            case LEAF -> {
                return new Leaf(xy);
            }
            default -> {
                return null;
            }
        }
    }
}
