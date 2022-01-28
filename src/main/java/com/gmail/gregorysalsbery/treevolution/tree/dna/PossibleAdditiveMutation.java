package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PossibleAdditiveMutation {

    private GridPoint xy;

    private List<TreePartType> possibleTypes;

    @Setter
    private boolean checked;

    @Setter
    private boolean duplicate;

    public PossibleAdditiveMutation(GridPoint xy) {
        this.xy = xy;
        this.duplicate = false;
        this.possibleTypes = new ArrayList<>();
    }

    public void addPossibleType(TreePartType type) {
        possibleTypes.add(type);
    }

    public void addPossibleTypes(List<TreePartType> types) {
        possibleTypes.addAll(types);
    }

    @Override
    public String toString() {
        return "PossibleAdditiveMutation(" + possibleTypes + " at (" + xy.getX() + ", " + xy.getY() + "))";
    }
}
