package com.gmail.gregorysalsbery.treevolution.app.grid;

import lombok.Getter;

@Getter
public class ScreenPoint {

    private float x;
    private float y;

    public ScreenPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void translate(float dx, float dy) {
        x += dx;
        y += dy;
    }
}
