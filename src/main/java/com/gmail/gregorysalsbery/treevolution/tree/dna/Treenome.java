package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.Direction;
import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.TreeUtil;
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

/**
 * Treenome, genome for trees :)
 * Has methods to load from file and write back to a file
 * Determines growth sequence of tree parts
 *
 * TODO: handle mutation
 */
@Getter
@Slf4j
public class Treenome {

    private List<TreeNA> treeNAs;
    private List<TreeNA> seeds;
    private List<TreeNA> trunks;
    private List<TreeNA> roots;
    private List<TreeNA> branches;
    private List<TreeNA> leaves;

    private Random rand = new Random();

    private int currentGrowthNumber = 0;

    private List<PossibleAdditiveMutation> totalPossibleAdditiveMutations;

    /**
     * Constructor
     * Reads in the treenome data from a file and does some checking
     * to make sure its valid. Then determines the growth sequence.
     *
     * @param filepath - path of file where the treenome data is stored
     */
    public Treenome(String filepath) {
        treeNAs = new ArrayList<TreeNA>();
        seeds = new ArrayList<TreeNA>();
        trunks = new ArrayList<TreeNA>();
        roots = new ArrayList<TreeNA>();
        branches = new ArrayList<TreeNA>();
        leaves = new ArrayList<TreeNA>();

        totalPossibleAdditiveMutations = new ArrayList<>();

        try {
            loadFromCsvFile(filepath);
            checkTreeNAIsValid();
            mutate();
            checkTreeNAIsValid();
            determineGrowthSequence();
        } catch (CsvValidationException | IOException | TreeNAInvalidException | GrowthSequenceException e) {
            // if there are any exceptions, we just go ahead and quit
            // TODO: better exception handling. We wouldn't have to exit the program and probably shouldn't either.
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public int getSize() {
        return treeNAs.size();
    }

    /**
     * Loads a treenome from a csv file. The file has to be in a specific format as follows:
     *      Type X Y
     * Where type is the first letter of the tree part type (T for Trunk, etc...)
     * X is the x coordinate relative to the seed. Seed is at (0, 0) always
     * Y is the y coordinate relative to the seed.
     *
     * @param filepath - path of the file
     * @throws CsvValidationException - if there's some problem with the csv data
     * @throws IOException - if there's a problem reading the file
     */
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

    /**
     * Writes the treenome to a csv file
     *
     * @param filepath - filepath to write to
     */
    public void writeToFile(String filepath) {
        List<List<String>> dataToWrite = new ArrayList<>();
        for(TreeNA treeNA : treeNAs) {
            List<String> treeNAAsList = new ArrayList<>();
            treeNAAsList.add(treeNA.getTypeString());
            treeNAAsList.add(String.valueOf(treeNA.getXy().getX()));
            treeNAAsList.add(String.valueOf(treeNA.getXy().getY()));
            dataToWrite.add(treeNAAsList);
        }
        try {
            Util.writeListToCsv(filepath, dataToWrite);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to write treenome to file");
        }
    }

    /**
     * Does some checks on the treenome data that is read in to verify it is a valid treenome
     *
     * @throws TreeNAInvalidException - if the treenome data is not valid
     */
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

    /**
     * Determines the order in which a tree will be grown.
     * Assigns a grow number to each part
     * Starts with the seed with grow number 0 and increments up from there
     *
     * @throws GrowthSequenceException - if there was a problem determining the growth sequence for the treenome
     */
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
            // TODO: also allow weighting to different growth directions - tree prefers up instead of out
            TreeNA nextGrowth = possibleGrowths.get(rand.nextInt(possibleGrowths.size()));
            nextGrowth.setGrowNumber(growNumber);
            possibleGrowths.remove(nextGrowth);
            growNumber++;

            // add any new possible growths that the next growth has attached to it
            possibleGrowths.addAll(getGrowableNeighbors(nextGrowth));
            // remove any duplicates
            possibleGrowths = possibleGrowths.stream().distinct().collect(Collectors.toList());
        }

        // make sure all parts got a grow number
        for(TreeNA treeNA : treeNAs) {
            if(treeNA.getGrowNumber() < 0) {
                log.error("This treeNA has a growth sequence problem: {}", treeNA);
                throw new GrowthSequenceException("A treeNA was found with no grow number assigned");
            }
        }

        // sort the list by grow number
        treeNAs.sort(Comparator.comparing(TreeNA::getGrowNumber));

//        log.debug("Here is the final grow sequence");
//        for(TreeNA treeNA : treeNAs) {
//            log.debug("{} - {} at ({},{})", treeNA.getGrowNumber(), treeNA.getType(), treeNA.getXy().getX(), treeNA.getXy().getY());
//        }
//        log.debug("Total sequence length: {}", treeNAs.size());
    }

    /**
     * Finds all the neighbors of a tree part (treeNA)
     * Neighbors are other tree parts that are immediately adjacent
     *
     * @param treeNA - the tree part to find neighbors for
     * @return - a list of the neighbors
     */
    private List<TreeNA> getNeighbors(TreeNA treeNA) {
        List<TreeNA> neighbors = new ArrayList<>();
        for(TreeNA otherTreeNA: treeNAs) {
            // check for self, don't want to include self in its list of neighbors
            if(otherTreeNA == treeNA) {
                continue;
            }
            // determine if other is adjacent
            if(treeNA.getXy().getGridDistanceBetween(otherTreeNA.getXy()) == 1) {
                neighbors.add(otherTreeNA);
            }
        }
        return neighbors;
    }

    /**
     * Finds neighbors of a tree part via getNeighbors and then filters the list of neighbors down
     * based on if that neighbor is growable from the input treeNA
     *
     * A neighbor is growable if it hasn't already been grown and,
     * input treeNA is seed or,
     * input treeNA is trunk and neighbor is trunk branch or leaf or,
     * input treeNA is root and neighbor is root (root only grows root) or,
     * input treeNA is branch and neighbor is branch or leaf,
     * input treeNA is not leaf (leaf can't grow anything else from it)
     *
     * @param treeNA - the tree part to find neighbors for
     * @return - a list of neighbors that are growable from the input treeNA
     */
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

    /**
     * Supplies the next tree part in the growth sequence
     *
     * @return - the next tree part that the owning tree should grow
     */
    public TreePart getNextGrowth() {
        // will return the next tree part in the growth sequence
        if(currentGrowthNumber < treeNAs.size()) {
            TreeNA treeNA = treeNAs.get(currentGrowthNumber);
            currentGrowthNumber++;
            return TreeUtil.createTreePart(treeNA.getType(), treeNA.getXy());
        }
        return null;
    }

    /**
     * Supplies all of the tree parts for this treenome
     *
     * @return - a list of all tree parts for this treenome
     */
    public List<TreePart> getAllTreeParts() {
        List<TreePart> treeParts = new ArrayList<>();
        for(TreeNA treeNA : treeNAs) {
            treeParts.add(TreeUtil.createTreePart(treeNA.getType(), treeNA.getXy()));
        }
        return treeParts;
    }

    private void mutate() {
        log.debug("Mutation started");

        // clear out the possible additive mutation list
        totalPossibleAdditiveMutations.clear();

        // make sure all TreeNA have checked set to false
        treeNAs.forEach(t -> t.setChecked(false));
        // there's only one seed
        // seed is starting point for mutation algorithm
        TreeNA seed = seeds.get(0);
        mutatePart(seed);
        // reset all TreeNA to checked false
        treeNAs.forEach(t -> t.setChecked(false));

        // combine all the duplicate additive mutations
        removePossibleAdditiveMutationDuplicates();

        // print some details for debug
        log.debug("Here are the possible additive mutations:");
        for(PossibleAdditiveMutation mutation : totalPossibleAdditiveMutations) {
            log.debug("{}", mutation);
        }

        // do the actual mutation
        log.debug("Adding mutations:");
        for(PossibleAdditiveMutation mutation : totalPossibleAdditiveMutations) {
            // only instantiate the mutation with some low probability
            if(rand.nextFloat() < 0.1f) {
                // choose a type at random from the possible types
                int randomTypeIndex = rand.nextInt(mutation.getPossibleTypes().size());
                TreePartType mutationPartType = mutation.getPossibleTypes().get(randomTypeIndex);
                log.debug("Adding mutation {} of type {}", mutation, mutationPartType);
                treeNAs.add(new TreeNA(mutationPartType, mutation.getXy()));
            }
        }
    }

    // this is the recursive piece of the mutation algorithm
    private void mutatePart(TreeNA treeNA) {
        if(!treeNA.isChecked()) {
            log.debug("Checking {}", treeNA);
            treeNA.setChecked(true);

            // additive mutations
            // can only mutate in a spot where there is not a neighbor
            // will have to avoid an additive mutation happening twice in the same empty spot
            // collect all potential additive mutation sites into a list
            // then remove the duplicates
            // then do the actual mutations
            log.debug("Find possible additive mutations");
            totalPossibleAdditiveMutations.addAll(findPossibleAdditiveMutations(treeNA));

            // TODO: subtractive mutations
            // will have to re-check the whole treenome to make sure the body plan is still valid
            // if some parts have been isolated, then will have to remove them

            // TODO: final type of mutation would be changing one type of part into another (leaf becomes branch, etc...)
            // again, will have to re-check the whole treenome to make sure the body plan is still valid
            // if some parts have been isolated, then will have to remove them

            // call mutatePart on the neighbors
            getNeighbors(treeNA).forEach(this::mutatePart);
        }
    }

    private List<PossibleAdditiveMutation> findPossibleAdditiveMutations(TreeNA treeNA) {
        List<TreeNA> neighbors = getNeighbors(treeNA);

        GridPoint adjacentUp = treeNA.getXy().getAdjacentUp();
        GridPoint adjacentDown = treeNA.getXy().getAdjacentDown();
        GridPoint adjacentLeft = treeNA.getXy().getAdjacentLeft();
        GridPoint adjacentRight = treeNA.getXy().getAdjacentRight();

        boolean upEmpty = true;
        boolean downEmpty = true;
        boolean leftEmpty = true;
        boolean rightEmpty = true;

        // determine which adjacent spaces are empty
        for(TreeNA neighbor : neighbors) {
            if(neighbor.getXy().compare(adjacentUp)) {
                upEmpty = false;
            } else if(neighbor.getXy().compare(adjacentDown)) {
                downEmpty = false;
            } else if(neighbor.getXy().compare(adjacentLeft)) {
                leftEmpty = false;
            } else if(neighbor.getXy().compare(adjacentRight)) {
                rightEmpty = false;
            }
        }

        List<PossibleAdditiveMutation> possibleAdditiveMutations = new ArrayList<>();
        if(upEmpty) {
            log.debug("Up is empty");
            List<TreePartType> possibleTypes = findPossibleAdditiveMutationTypes(treeNA.getType(), Direction.UP);
            if(possibleTypes.size() > 0) {
                PossibleAdditiveMutation mutation = new PossibleAdditiveMutation(adjacentUp);
                mutation.addPossibleTypes(possibleTypes);
                possibleAdditiveMutations.add(mutation);
                log.debug("Add this possible mutation: {}", mutation);
            } else {
                log.debug("There are no types that work in this direction though");
            }
        }
        if(downEmpty) {
            log.debug("Down is empty");
            List<TreePartType> possibleTypes = findPossibleAdditiveMutationTypes(treeNA.getType(), Direction.DOWN);
            if(possibleTypes.size() > 0) {
                PossibleAdditiveMutation mutation = new PossibleAdditiveMutation(adjacentDown);
                mutation.addPossibleTypes(possibleTypes);
                possibleAdditiveMutations.add(mutation);
                log.debug("Add this possible mutation: {}", mutation);
            } else {
                log.debug("There are no types that work in this direction though");
            }
        }
        if(leftEmpty) {
            log.debug("Left is empty");
            List<TreePartType> possibleTypes = findPossibleAdditiveMutationTypes(treeNA.getType(), Direction.LEFT);
            if(possibleTypes.size() > 0) {
                PossibleAdditiveMutation mutation = new PossibleAdditiveMutation(adjacentLeft);
                mutation.addPossibleTypes(possibleTypes);
                possibleAdditiveMutations.add(mutation);
                log.debug("Add this possible mutation: {}", mutation);
            } else {
                log.debug("There are no types that work in this direction though");
            }
        }
        if(rightEmpty) {
            log.debug("Right is empty");
            List<TreePartType> possibleTypes = findPossibleAdditiveMutationTypes(treeNA.getType(), Direction.RIGHT);
            if(possibleTypes.size() > 0) {
                PossibleAdditiveMutation mutation = new PossibleAdditiveMutation(adjacentRight);
                mutation.addPossibleTypes(possibleTypes);
                possibleAdditiveMutations.add(mutation);
                log.debug("Add this possible mutation: {}", mutation);
            } else {
                log.debug("There are no types that work in this direction though");
            }
        }

        // remove the mutations with no possible types
        // these get created if there is an empty spot, but there is no type that will work there
        //possibleAdditiveMutations.removeIf(m -> m.getPossibleTypes().size() < 1);

        return possibleAdditiveMutations;
    }

    private List<TreePartType> findPossibleAdditiveMutationTypes(TreePartType parentType, Direction mutationDirection) {
        List<TreePartType> possibleAdditiveMutationTypes = new ArrayList<>();
        switch (parentType) {
            case SEED -> {
                switch (mutationDirection) {
                    case UP -> {
                        possibleAdditiveMutationTypes.add(TreePartType.LEAF);
                        possibleAdditiveMutationTypes.add(TreePartType.TRUNK);
                    }
                    case DOWN -> {
                        possibleAdditiveMutationTypes.add(TreePartType.ROOT);
                    }
                }
            }
            case TRUNK -> {
                switch (mutationDirection) {
                    case UP -> {
                        possibleAdditiveMutationTypes.add(TreePartType.LEAF);
                        possibleAdditiveMutationTypes.add(TreePartType.TRUNK);
                    }
                    case LEFT, RIGHT -> {
                        possibleAdditiveMutationTypes.add(TreePartType.LEAF);
                        possibleAdditiveMutationTypes.add(TreePartType.BRANCH);
                    }
                }
            }
            case ROOT -> {
                switch (mutationDirection) {
                    case DOWN, RIGHT, LEFT -> {
                        possibleAdditiveMutationTypes.add(TreePartType.ROOT);
                    }
                }
            }
            case BRANCH -> {
                possibleAdditiveMutationTypes.add(TreePartType.LEAF);
                possibleAdditiveMutationTypes.add(TreePartType.BRANCH);
            }
        }
        return possibleAdditiveMutationTypes;
    }

    public void removePossibleAdditiveMutationDuplicates() {
        // first have to find the duplicates
        totalPossibleAdditiveMutations.forEach(m -> m.setChecked(false));
        for(PossibleAdditiveMutation mutation1 : totalPossibleAdditiveMutations) {
            if(mutation1.isChecked()) {
                continue;
            }
            mutation1.setChecked(true);
            for(PossibleAdditiveMutation mutation2 : totalPossibleAdditiveMutations) {
                if(mutation2.isChecked()) {
                    // skip this iteration if mutation2 has already been looked at as a mutation1
                    // this also skips the self to self comparison
                    continue;
                }
                if(mutation1.getXy().compare(mutation2.getXy())) {
                    mutation2.setChecked(true);
                    mutation2.setDuplicate(true);
                    // the combining action
                    // adding all the possible types from mutation2 into mutation1's list
                    // mutation1 may have more than 1 of the same type in its list now
                    // but that may actually not be a problem
                    // can make it more likely that the type with more entries will be chosen as the mutation
                    mutation1.addPossibleTypes(mutation2.getPossibleTypes());
                }
            }
        }
        // actually do the removal of the duplicates
        totalPossibleAdditiveMutations.removeIf(PossibleAdditiveMutation::isDuplicate);
        // reset the checked flag
        totalPossibleAdditiveMutations.forEach(m -> m.setChecked(false));
    }
}
