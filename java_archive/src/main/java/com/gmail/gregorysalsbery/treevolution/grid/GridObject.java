package com.gmail.gregorysalsbery.treevolution.grid;

import com.gmail.gregorysalsbery.treevolution.util.Config;
import com.gmail.gregorysalsbery.treevolution.util.Util;
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
            g.fillRect(Util.gridXToScreenX(xy.getX()), Util.gridYToScreenY(xy.getY()), Config.CELL_SIZE, Config.CELL_SIZE);
        }
    }

    public void translate(int dx, int dy) {
        xy.translate(dx, dy);
    }

    public void translateUp() {
        xy.translateUp();
    }
}
