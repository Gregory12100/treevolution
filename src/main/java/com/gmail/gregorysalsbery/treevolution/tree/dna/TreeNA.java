package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class TreeNA {

    private GridPoint xy;

    @Setter
    private int buildY;

    private TreePartType type;

    // keep track of when this part should be grown relative to the others in the body plan
    // it will be -1 until assigned
    // 0 is highest priority
    @Setter
    private int growNumber = -1;

    public TreeNA(TreePartType type, int x, int y) {
        this.xy = new GridPoint(x, y);
        this.buildY = y;
        this.type = type;
    }

    public TreeNA(TreePartType type, GridPoint xy) {
        this.xy = xy;
        this.type = type;
    }

    public String getTypeString() {
        switch (type) {
            case SEED -> {
                return "S";
            }
            case TRUNK -> {
                return "T";
            }
            case ROOT -> {
                return "R";
            }
            case BRANCH -> {
                return "B";
            }
            case LEAF -> {
                return "L";
            }
            case FRUIT -> {
                return "F";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        return "TreeNA(" + type.toString() + " at (" + xy.getX() + ", " + xy.getY() + "))";
    }
}
