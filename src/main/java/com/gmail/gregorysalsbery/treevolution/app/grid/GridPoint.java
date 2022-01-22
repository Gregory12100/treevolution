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

    public ScreenPoint getScreenPoint() {
        return new ScreenPoint(x * Config.CELL_SIZE, Config.SCREEN_SIZE_Y - (y+1) * Config.CELL_SIZE);
    }
}
