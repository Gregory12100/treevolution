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

    public void translateUp() {
        translate(0, 1);
    }

    public void translateDown() {
        translate(0, -1);
    }

    public void translateLeft() {
        translate(-1, 0);
    }

    public void translateRight() {
        translate(1, 0);
    }

    public GridPoint getAdjacentUp() {
        return new GridPoint(x, y+1);
    }

    public GridPoint getAdjacentDown() {
        return new GridPoint(x, y-1);
    }

    public GridPoint getAdjacentLeft() {
        return new GridPoint(x-1, y);
    }

    public GridPoint getAdjacentRight() {
        return new GridPoint(x+1, y);
    }

    public int getGridDistanceBetween(GridPoint other) {
        return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
    }

    public boolean compare(int otherX, int otherY) {
        return x == otherX && y == otherY;
    }

    public boolean compare(GridPoint other) {
        return compare(other.getX(), other.getY());
    }
}
