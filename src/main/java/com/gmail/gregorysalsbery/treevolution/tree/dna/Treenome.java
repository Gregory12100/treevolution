package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.tree.dna.exceptions.GrowthSequenceException;
import com.gmail.gregorysalsbery.treevolution.tree.dna.exceptions.TreeNAInvalidException;
import com.gmail.gregorysalsbery.treevolution.tree.parts.*;
import com.gmail.gregorysalsbery.treevolution.util.Util;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class Treenome {

    // read in treena file
    // assign a growing order to each tree part
    // during the sim, the tree just follows the predetermined build instructions
    // first part built is always top leaf and then works way down trunk
    // when multiple things could be built, just choose randomly

    private List<TreeNA> treeNAs;
    private List<TreeNA> seeds;
    private List<TreeNA> trunks;
    private List<TreeNA> roots;
    private List<TreeNA> branches;
    private List<TreeNA> leaves;

    private Random rand;

    private int currentGrowthNumber = 0;

    public Treenome(String filepath) {
        treeNAs = new ArrayList<TreeNA>();
        seeds = new ArrayList<TreeNA>();
        trunks = new ArrayList<TreeNA>();
        roots = new ArrayList<TreeNA>();
        branches = new ArrayList<TreeNA>();
        leaves = new ArrayList<TreeNA>();

        rand = new Random();

        try {
            loadFromCsvFile(filepath);
            checkTreeNAIsValid();
            determineGrowthSequence();
        } catch (CsvValidationException | IOException | TreeNAInvalidException | GrowthSequenceException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void loadFromCsvFile(String filepath) throws CsvValidationException, IOException {
        List<List<String>> csvData = Util.readCsvToList(filepath);

        // reset all the lists
        seeds.clear();
        trunks.clear();
        roots.clear();
        branches.clear();
        leaves.clear();
        treeNAs.clear();

        for(List<String> csvLine : csvData) {
            // determine the type
            TreePartType type;
            switch (csvLine.get(0)) {
                case "S" -> type = TreePartType.SEED;
                case "R" -> type = TreePartType.ROOT;
                case "T" -> type = TreePartType.TRUNK;
                case "B" -> type = TreePartType.BRANCH;
                case "L" -> type = TreePartType.LEAF;
                default -> type = null;
            }

            // populate the master list
            TreeNA treeNA = new TreeNA(type, Integer.parseInt(csvLine.get(1)), Integer.parseInt(csvLine.get(2)));
            treeNAs.add(treeNA);
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

    private void checkTreeNAIsValid() throws TreeNAInvalidException {
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

    private void determineGrowthSequence() throws GrowthSequenceException {
        int growNumber = 0;

        // seed is first
        TreeNA seed = seeds.get(growNumber);
        seed.setGrowNumber(growNumber);
        growNumber++;

        // determine next set of possible growths
        List<TreeNA> possibleGrowths = getGrowableNeighbors(seed);
        while(possibleGrowths.size() > 0) {
            // randomly choose a next possible growth
            // TODO: allow probability weighting to different tree part types
            TreeNA nextGrowth = possibleGrowths.get(rand.nextInt(possibleGrowths.size()));
            nextGrowth.setGrowNumber(growNumber);
            log.debug("Next growth is {} at ({},{}) with grow number {}", nextGrowth.getType(), nextGrowth.getXy().getX(), nextGrowth.getXy().getY(), growNumber);
            possibleGrowths.remove(nextGrowth);
            growNumber++;

            // add any new possible growths that the next growth has attached to it
            possibleGrowths.addAll(getGrowableNeighbors(nextGrowth));
            // remove any duplicates
            possibleGrowths = possibleGrowths.stream().distinct().collect(Collectors.toList());
        }

        log.debug("End grow number is: {}", growNumber);

        // make sure all parts got a grow number
        for(TreeNA treeNA : treeNAs) {
            if(treeNA.getGrowNumber() < 0) {
                throw new GrowthSequenceException("A treeNA was found with no grow number assigned");
            }
        }

        // sort the list by grow number
        treeNAs.sort(Comparator.comparing(TreeNA::getGrowNumber));

        log.debug("Here is the final grow sequence");
        for(TreeNA treeNA : treeNAs) {
            log.debug("{} - {} at ({},{})", treeNA.getGrowNumber(), treeNA.getType(), treeNA.getXy().getX(), treeNA.getXy().getY());
        }
        log.debug("Total sequence length: {}", treeNAs.size());
    }

    private List<TreeNA> getNeighbors(TreeNA treeNA) {
        List<TreeNA> neighbors = new ArrayList<>();
        for(TreeNA otherTreeNA: treeNAs) {
            // check for self
            if(otherTreeNA == treeNA) {
                continue;
            }
            if(treeNA.getXy().getGridDistanceBetween(otherTreeNA.getXy()) == 1) {
                neighbors.add(otherTreeNA);
            }
        }
        return neighbors;
    }

    private List<TreeNA> getGrowableNeighbors(TreeNA treeNA) {
        // leaf cannot grow anything, return empty list
        if(treeNA.getType() == TreePartType.LEAF) {
            return new ArrayList<>();
        }

        // get the neighbors
        List<TreeNA> neighbors = getNeighbors(treeNA);

        // check if neighbor has already been added to grow sequence
        // remove if so
        neighbors.removeIf(n -> n.getGrowNumber() != -1);

        // check if neighbor is correct type to be grown from this type
        // seed can grow all types
        if(treeNA.getType() == TreePartType.SEED) {
            return neighbors;
        }

        // root can only grow root
        // trunk can grow trunk branch and leaf
        // branch can grow branch and leaf
        List<TreeNA> neighborsToRemove = new ArrayList<>();
        for(TreeNA neighbor : neighbors) {
            switch (treeNA.getType()) {
                case TRUNK -> {
                    if(neighbor.getType() == TreePartType.SEED ||
                            neighbor.getType() == TreePartType.ROOT) {
                        neighborsToRemove.add(neighbor);
                    }
                }
                case ROOT -> {
                    if(neighbor.getType() != TreePartType.ROOT) {
                        neighborsToRemove.add(neighbor);
                    }
                }
                case BRANCH -> {
                    if(neighbor.getType() == TreePartType.SEED ||
                            neighbor.getType() == TreePartType.ROOT ||
                            neighbor.getType() == TreePartType.TRUNK) {
                        neighborsToRemove.add(neighbor);
                    }
                }
            }
        }
        neighbors.removeAll(neighborsToRemove);

        return neighbors;
    }

    public TreePart getNextGrowth() {
        // will return the next tree part in the growth sequence
        if(currentGrowthNumber < treeNAs.size()) {
            TreeNA treeNA = treeNAs.get(currentGrowthNumber);
            currentGrowthNumber++;
            switch (treeNA.getType()) {
                case SEED -> {
                    return new Seed(treeNA.getXy());
                }
                case TRUNK -> {
                    return new Trunk(treeNA.getXy());
                }
                case ROOT -> {
                    return new Root(treeNA.getXy());
                }
                case BRANCH -> {
                    return new Branch(treeNA.getXy());
                }
                case LEAF -> {
                    return new Leaf(treeNA.getXy());
                }
                default -> {
                    return null;
                }
            }
        }
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
