package com.gmail.gregorysalsbery.treevolution.nongrid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void translate(float dx, float dy) {
        x += dx;
        y += dy;
    }
}
