package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.tree.parts.*;
import com.gmail.gregorysalsbery.treevolution.util.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class BodyPlan {

    // read in treena file
    // assign a growing order to each tree part
    // during the sim, the tree just follows the predetermined build instructions
    // first part built is always top leaf and then works way down trunk
    // when multiple things could be built, just choose randomly

    List<TreeNA> treeNAs;

    public BodyPlan(String filepath) {
        treeNAs = new ArrayList<TreeNA>();
        loadFromCsvFile(filepath);
        determineGrowthSequence();
    }

    public void loadFromCsvFile(String filepath) {
        List<List<String>> csvData = Util.readCsvToList(filepath);

        for(List<String> csvLine : csvData) {
            TreePartType type;
            switch (csvLine.get(0)) {
                case "S" -> type = TreePartType.SEED;
                case "R" -> type = TreePartType.ROOT;
                case "T" -> type = TreePartType.TRUNK;
                case "B" -> type = TreePartType.BRANCH;
                case "L" -> type = TreePartType.LEAF;
                default -> type = null;
            }
            treeNAs.add(new TreeNA(type, Integer.parseInt(csvLine.get(1)), Integer.parseInt(csvLine.get(2))));
        }
    }

    public void determineGrowthSequence() {
        // there should be one and only one seed
        // and it should be at 0, 0
        boolean foundSeed = false;
        for(TreeNA treeNA : treeNAs) {
            if(treeNA.getType() == TreePartType.SEED) {
                if(foundSeed) {
                    // we have found more than one seed
                    // that's an error
                    log.error("More than one seed found in tree DNA!");
                }
                foundSeed = true;
            }
        }
        if(!foundSeed) {
            log.error("Seed was not found in tree DNA");
        }
    }

    public TreePart getNextTreePart() {
        // will return the next tree part in the growth sequence
        return null;
    }

    public List<TreePart> getAllTreeParts() {
        List<TreePart> treeParts = new ArrayList<TreePart>();
        for(TreeNA treeNA : treeNAs) {
            switch (treeNA.getType()) {
                case SEED -> treeParts.add(new Seed(treeNA.getXy()));
                case TRUNK -> treeParts.add(new Trunk(treeNA.getXy()));
                case ROOT -> treeParts.add(new Root(treeNA.getXy()));
                case BRANCH -> treeParts.add(new Branch(treeNA.getXy()));
                case LEAF -> treeParts.add(new Leaf(treeNA.getXy()));
            }
        }
        return treeParts;
    }
}
