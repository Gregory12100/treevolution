package com.gmail.gregorysalsbery.treevolution.environment;

import com.gmail.gregorysalsbery.treevolution.grid.GridObject;

import java.awt.*;

public class Dirt extends GridObject {

    private int food;

    public Dirt(int gridX, int gridY) {
        super(gridX, gridY, new Color(100,80,60));
    }
}
