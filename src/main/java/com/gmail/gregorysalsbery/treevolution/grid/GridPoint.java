package com.gmail.gregorysalsbery.treevolution.grid;

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

    public int getGridDistanceBetween(GridPoint other) {
        return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
    }

    public boolean compare(int otherX, int otherY) {
        return x == otherX && y == otherY;
    }
}
