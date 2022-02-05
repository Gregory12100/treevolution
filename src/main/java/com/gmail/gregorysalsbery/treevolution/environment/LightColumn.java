package com.gmail.gregorysalsbery.treevolution.environment;

import com.gmail.gregorysalsbery.treevolution.grid.GridObject;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePart;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class LightColumn {

    private List<TreePart> columnOfParts;

    public LightColumn() {
        this.columnOfParts = new ArrayList<>();
    }

    public void registerPart(TreePart part) {
        columnOfParts.add(part);
    }

    public void shine(int lightLevel) {
        // sort the column by y location of the tree part in descending order
        columnOfParts.sort(Comparator.comparing(TreePart::getY).reversed());

        // iterate down the column, applying light level appropriately
        for(TreePart part : columnOfParts) {
            // if two parts happen to occupy the same space, then whichever happens to be first will get the most light, the next one getting the reduced light
            lightLevel = part.hitByLight(lightLevel);
            // no need to iterate over the tree parts below if the light level has been reduced to 0
            if(lightLevel <= 0) break;
        }
    }
}
