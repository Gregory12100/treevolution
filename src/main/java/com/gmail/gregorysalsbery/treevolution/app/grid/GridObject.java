package com.gmail.gregorysalsbery.treevolution.app.grid;

import com.gmail.gregorysalsbery.treevolution.app.util.Config;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public abstract class GridObject {

    private GridPoint xy;

    private Color color;
    private boolean visible;

    public GridObject(int gridX, int gridY, Color color) {
        this.xy = new GridPoint(gridX, gridY);
        this.color = color;
        this.visible = true;
    }

    public void draw(Graphics g) {
        if(visible) {
            g.setColor(color);
            ScreenPoint screenPoint = xy.getScreenPoint();
            g.fillRect((int) screenPoint.getX(), (int) screenPoint.getY(), Config.CELL_SIZE, Config.CELL_SIZE);
        }
    }
}
