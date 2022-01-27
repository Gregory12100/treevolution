package com.gmail.gregorysalsbery.treevolution.app;

import com.gmail.gregorysalsbery.treevolution.environment.Dirt;
import com.gmail.gregorysalsbery.treevolution.environment.Sun;
import com.gmail.gregorysalsbery.treevolution.generation.Generation;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimHandler {

    // simulation objects
    private Generation generation;
    private List<Dirt> dirts;
    private Sun sun;

    public SimHandler() {
        dirts = createGround(Config.GROUND_DEPTH);
        generation = new Generation(50);
        sun = new Sun(generation.getTrees());
    }

    public void update(float dt) {
        sun.shine();
        generation.update(dt);
//        log.debug("Tree energy: {}", generation.getTrees().get(0).getEnergy());
    }

    public void draw(Graphics g) {
        for (Dirt dirt : dirts) {
            dirt.draw(g);
        }

        generation.draw(g);
    }

    private List<Dirt> createGround(int depth) {
        List<Dirt> dirtList = new ArrayList<>();
        for(int j=0; j<depth; j++) {
            for (int i=0; i<Config.GRID_SIZE_X; i++) {
                dirtList.add(new Dirt(i, j));
            }
        }
        return dirtList;
    }
}
