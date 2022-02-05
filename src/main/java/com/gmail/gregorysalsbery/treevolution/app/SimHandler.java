package com.gmail.gregorysalsbery.treevolution.app;

import com.gmail.gregorysalsbery.treevolution.environment.Dirt;
import com.gmail.gregorysalsbery.treevolution.environment.Sun;
import com.gmail.gregorysalsbery.treevolution.generation.Generation;
import com.gmail.gregorysalsbery.treevolution.tree.dna.Treenome;
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

    private int stepCount = 0;

    private int generationCount = 0;

    private int numTrees = 50;

    public SimHandler() {
        dirts = createGround(Config.GROUND_DEPTH);
        sun = new Sun();
        generation = new Generation("src/main/resources/treenaTest.csv", numTrees, sun);
    }

    public void update(float dt) {
        sun.shine();
        generation.update(dt);

        if(stepCount > 1200) {
            log.debug("End of generation {}", generationCount);
            stepCount = 0;
            Treenome bestTreenome = generation.getBestTree().getTreenome();
            String savePath = "src/main/resources/run/bestFromGen" + generationCount + ".csv";
            bestTreenome.writeToFile(savePath);
            generation = new Generation(savePath, numTrees, sun);
            generationCount++;
        } else {
            stepCount++;
        }
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
