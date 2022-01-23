package com.gmail.gregorysalsbery.treevolution.tree.parts;

import com.gmail.gregorysalsbery.treevolution.grid.GridObject;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import lombok.Getter;

import java.awt.*;

@Getter
public abstract class TreePart extends GridObject {

    private TreePartType type;

    private int health;

    public TreePart(TreePartType type, Color color, int gridX, int gridY, int health) {
        super(gridX, gridY, color);

        this.type = type;
        this.health = health;
    }
}
