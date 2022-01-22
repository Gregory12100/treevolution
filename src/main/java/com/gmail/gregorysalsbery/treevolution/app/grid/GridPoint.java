package com.gmail.gregorysalsbery.treevolution.app.grid;

import com.gmail.gregorysalsbery.treevolution.app.util.Config;
import lombok.Getter;

@Getter
public class GridPoint {

    private int x;
    private int y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void moveUp() {
        translate(0, 1);
    }
}
