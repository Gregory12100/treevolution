package com.gmail.gregorysalsbery.treevolution.nongrid;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import lombok.Getter;

import java.awt.*;

/**
 * For objects that don't align to the grid
 * Will use same coordinate scale, but will be continuous vs grid
 */
@Getter
public class NonGridObject {

    private Point xy;

    public NonGridObject(float x, float y) {
        this.xy = new Point(x, y);
    }

    public void translate(float dx, float dy) {
        xy.translate(dx, dy);
    }
}
