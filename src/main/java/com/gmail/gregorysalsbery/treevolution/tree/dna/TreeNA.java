package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.Getter;

@Getter
public class TreeNA {

    private GridPoint xy;

    private TreePartType type;

    // keep track of when this part should be grown relative to the others in the body plan
    private int growNumber = -1;

    public TreeNA(TreePartType type, int x, int y) {
        this.xy = new GridPoint(x, y);
        this.type = type;
    }
}
