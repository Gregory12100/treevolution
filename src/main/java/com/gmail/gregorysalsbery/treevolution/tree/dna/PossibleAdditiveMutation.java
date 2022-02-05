package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.Getter;

@Getter
public class PossibleAdditiveMutation {

    private GridPoint xy;

    private TreePartType type;

    public PossibleAdditiveMutation(GridPoint xy, TreePartType type) {
        this.xy = xy;
        this.type = type;
    }

    @Override
    public String toString() {
        return "PossibleAdditiveMutation(" + type + " at (" + xy.getX() + ", " + xy.getY() + "))";
    }
}
