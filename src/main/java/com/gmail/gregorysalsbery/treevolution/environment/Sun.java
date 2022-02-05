package com.gmail.gregorysalsbery.treevolution.environment;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePart;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import lombok.Setter;

import java.util.List;

public class Sun {

    // TODO: need to find a more efficient method for doing the sun
    // the current method literally looks at every space and then looks at every individual tree part for each space
    // maybe each treepart could register its position to a new Grid class
    // the grid class could maintain lists of everything in a particular column
    // tree parts never change their x positions so the lists per column would only have to change to add new growths
    // then we can sort the column lists by y position and assign sunlight down the list
    // should be much faster!

    private static final int START_LIGHT_LEVEL = 10;

    @Setter
    private List<Tree> trees;

    public Sun(List<Tree> trees) {
        this.trees = trees;
    }

    public void shine() {
        for(int x=0; x<Config.GRID_SIZE_X; x++) {
            shineColumn(x);
        }
    }

    public void shineColumn(int x) {
        int lightLevel = START_LIGHT_LEVEL;
        for(int y=Config.GRID_SIZE_Y-1; y>=0; y--) {
            lightLevel = shineSpace(x, y, lightLevel);
            // if all light is used up for the column, then exit the loop for that column
            if(lightLevel <= 0) break;
        }
    }

    public int shineSpace(int x, int y, int lightLevel) {
        for(Tree tree : trees) {
            for(TreePart treePart : tree.getTreeParts()) {
                if(treePart.getXy().compare(x, y)) {
                    // assume only one tree part can occupy a space, no overlaps! (will have to actually make that happen somehow)
                    return treePart.hitByLight(lightLevel);
                }
            }
        }
        // no tree parts in the space
        return lightLevel;
    }
}
