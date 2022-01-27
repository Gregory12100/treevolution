package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridObject;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
public abstract class TreePart extends GridObject {

    // a reference to the parent tree
    @Setter
    private Tree parentTree;

    private TreePartType type;

    private int health;

    public TreePart(TreePartType type, Color color, int gridX, int gridY, int health) {
        super(gridX, gridY, color);

        this.type = type;
        this.health = health;
    }

    public int hitByLight(int lightLevel) {
        // light level is always at least 1 at this point
        // light level is max 10
        switch (type) {
            case SEED -> {
                // seed will block all light and give the tree the amount of energy in light level
                parentTree.obtainEnergy(lightLevel);
                lightLevel = 0;
            }
            case TRUNK -> {
                // trunk will block all light and give the tree 0 energy
                lightLevel = 0;
            }
            case ROOT -> {
                // root should never be hit by light
            }
            case BRANCH -> {
                // branch will 5 light and give the tree 0 energy
                lightLevel -= 5;
            }
            case LEAF -> {
                // leaf will block 3 light and give the tree the amount of energy in light level
                parentTree.obtainEnergy(lightLevel);
                lightLevel -= 3;
            }
        }
        return lightLevel;
    }
}
