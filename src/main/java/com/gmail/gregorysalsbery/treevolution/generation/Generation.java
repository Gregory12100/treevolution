package com.gmail.gregorysalsbery.treevolution.generation;

import com.gmail.gregorysalsbery.treevolution.environment.Sun;
import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import com.gmail.gregorysalsbery.treevolution.tree.dna.Treenome;
import com.gmail.gregorysalsbery.treevolution.util.Config;
import com.gmail.gregorysalsbery.treevolution.util.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class Generation {

    private List<Tree> trees;

    public Generation(String filepath, int numTrees, Sun sun) {
        trees = new ArrayList<>();
        for(int i=0; i<numTrees; i++) {
            Treenome treenome = new Treenome(filepath);
            trees.add(new Tree(treenome, Util.getRandomGridX(), Config.GROUND_DEPTH-1, sun));
        }
    }

    public void update(float dt) {
        for(Tree tree : trees) {
            tree.grow();
        }
    }

    public void draw(Graphics g) {
        for(Tree tree : trees) {
            tree.draw(g);
        }
    }

    public void printTrees() {
        log.debug("Here are the trees");
        trees.forEach(t -> log.debug("    {}", t));
    }

    public List<Tree> getBestTrees(int numBest) {
        List<Tree> bestTrees = trees.stream().sorted(Comparator.comparing(Tree::getScore).reversed()).limit(numBest).collect(Collectors.toList());
        log.debug("Here are the best {} trees", numBest);
        bestTrees.forEach(t -> log.debug("    {}", t));
        return bestTrees;
    }

    public Tree getBestTree() {
        return getBestTrees(1).get(0);
    }
}
