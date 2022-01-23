package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.tree.Tree;
import com.gmail.gregorysalsbery.treevolution.tree.parts.*;
import com.gmail.gregorysalsbery.treevolution.util.Util;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
    List<TreeNA> seeds;
    List<TreeNA> trunks;
    List<TreeNA> roots;
    List<TreeNA> branches;
    List<TreeNA> leaves;

    public BodyPlan(String filepath) {
        treeNAs = new ArrayList<TreeNA>();
        try {
            loadFromCsvFile(filepath);
            checkTreeNAIsValid();
            determineGrowthSequence();
        } catch (CsvValidationException | IOException | TreeNAInvalidException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void loadFromCsvFile(String filepath) throws CsvValidationException, IOException {
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

            // reset all the lists
            seeds.clear();
            trunks.clear();
            roots.clear();
            branches.clear();
            leaves.clear();
            treeNAs.clear();

            // populate the master list
            TreeNA treeNA = new TreeNA(type, Integer.parseInt(csvLine.get(1)), Integer.parseInt(csvLine.get(2)));
            treeNAs.add(new TreeNA(type, Integer.parseInt(csvLine.get(1)), Integer.parseInt(csvLine.get(2))));
            // sort out the different tree parts
            switch (treeNA.getType()) {
                case SEED -> seeds.add(treeNA);
                case TRUNK -> trunks.add(treeNA);
                case ROOT -> roots.add(treeNA);
                case BRANCH -> branches.add(treeNA);
                case LEAF -> leaves.add(treeNA);
            }
        }
    }

    public void checkTreeNAIsValid() throws TreeNAInvalidException {
        if(seeds.size() > 1) {
            throw new TreeNAInvalidException("Tree DNA contains multiple seeds!");
        } else if(seeds.size() < 1) {
            throw new TreeNAInvalidException("Tree DNA does not contain a seed!");
        }

        TreeNA seed = seeds.get(0);
        if(seed.getXy().getX() != 0 || seed.getXy().getY() != 0) {
            throw new TreeNAInvalidException("Seed is not located at (0, 0) in tree DNA!");
        }
    }

    public void determineGrowthSequence() {
        // seed is first
        TreeNA seed = seeds.get(0);
        seed.setGrowNumber(0);

        
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
