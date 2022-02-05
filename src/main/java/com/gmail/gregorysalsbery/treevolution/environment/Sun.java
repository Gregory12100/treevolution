package com.gmail.gregorysalsbery.treevolution.environment;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePart;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sun {

    // TODO: add shadows and make the background a bit darker under tree parts according to the light level

    private static final int START_LIGHT_LEVEL = 10;

    Map<Integer, LightColumn> lightColumns;

    public Sun() {
        lightColumns = new HashMap<>();
        for(int x=0; x<Config.GRID_SIZE_X; x++) {
            lightColumns.put(x, new LightColumn());
        }
    }

    public void registerPart(TreePart part) {
        lightColumns.get(part.getX()).registerPart(part);
    }

    public void shine() {
        for(int x=0; x<Config.GRID_SIZE_X; x++) {
            lightColumns.get(x).shine(START_LIGHT_LEVEL);
        }
    }
}
