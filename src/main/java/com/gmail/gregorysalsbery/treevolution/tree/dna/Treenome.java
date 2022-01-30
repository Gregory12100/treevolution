package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.Direction;
import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
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
    private List<TreeNA> fruits;

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
        fruits = new ArrayList<TreeNA>();

        totalPossibleAdditiveMutations = new ArrayList<>();

        try {
            loadFromCsvFile(filepath);
//            TreenomeUtil.checkTreenomeIsValid(this);
//            mutate();
            TreenomeUtil.checkTreenomeIsValid(this);
            GrowthSequencer.determineGrowthSequence(this);
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

    public int getFullTrunkHeight() {
        return trunks.size();
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
                case "F" -> type = TreePartType.FRUIT;
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
                case FRUIT -> fruits.add(treeNA);
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

//    /**
//     * Determines the order in which a tree will be grown.
//     * Assigns a grow number to each part
//     * Starts with the seed with grow number 0 and increments up from there
//     *
//     * @throws GrowthSequenceException - if there was a problem determining the growth sequence for the treenome
//     */
//    private void determineGrowthSequence() throws GrowthSequenceException {
//        int growNumber = 0;
//        int currentTrunkHeight = 0;
//
//        // seed is first
//        TreeNA seed = seeds.get(growNumber);
//        seed.setGrowNumber(growNumber);
//        growNumber++;
//
//        // determine next set of possible growths
//        List<TreeNA> possibleGrowths = TreenomeUtil.filterOutNonGrowableNeighors(seed, TreenomeUtil.getNeighbors(seed, treeNAs));
//        while(possibleGrowths.size() > 0) {
//            // randomly choose a next possible growth
//            // TODO: allow probability weighting to different tree part types
//            // TODO: also allow weighting to different growth directions - tree prefers up instead of out
//            TreeNA nextGrowth = possibleGrowths.get(rand.nextInt(possibleGrowths.size()));
//            nextGrowth.setGrowNumber(growNumber);
//            possibleGrowths.remove(nextGrowth);
//            growNumber++;
//
//            // if we just grew a trunk, then the trunk height increases
//            if(nextGrowth.getType() == TreePartType.TRUNK) {
//                currentTrunkHeight++;
//            }
//
//            // add any new possible growths that the next growth has attached to it
//            List<TreeNA> neighbors = TreenomeUtil.getNeighbors(nextGrowth, treeNAs);
//            possibleGrowths.addAll(TreenomeUtil.filterOutNonGrowableNeighors(nextGrowth, neighbors));
//            // remove any duplicates
//            possibleGrowths = possibleGrowths.stream().distinct().collect(Collectors.toList());
//        }
//
//        // make sure all parts got a grow number
//        for(TreeNA treeNA : treeNAs) {
//            if(treeNA.getGrowNumber() < 0) {
//                log.error("This treeNA has a growth sequence problem: {}", treeNA);
//                throw new GrowthSequenceException("A treeNA was found with no grow number assigned");
//            }
//        }
//
//        // sort the list by grow number
//        treeNAs.sort(Comparator.comparing(TreeNA::getGrowNumber));
//
////        log.debug("Here is the final grow sequence");
////        for(TreeNA treeNA : treeNAs) {
////            log.debug("{} - {} at ({},{})", treeNA.getGrowNumber(), treeNA.getType(), treeNA.getXy().getX(), treeNA.getXy().getY());
////        }
////        log.debug("Total sequence length: {}", treeNAs.size());
//    }

    // TODO: modified trunk growing
    // planning how to do modified trunk growing
    // will have to keep track of current trunk height as build numbers are being assigned
    // current trunk height is just the number of trunks that have been built up to now
    // modify (or make new function) get neighbors for trunk
    // the neighbors will be y offset by full trunk height minus current trunk height
    // branches and leaves will have to remember a new xy of where they actually get built
    // mutations will have to allow for inserting trunks at the bottom, thereby moving all parts y up one

    // TODO: inheritance for the growth sequence
    // planning how to do inheritance for the growth sequence
    // the growth sequence is probably just as important as the final body plan and should be part of what can evolve
    // need to find a way to have it inheritable and mutatable

    // TODO: limit branch and leaf height to at or below the full trunk height

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

            GridPoint buildXy = new GridPoint(treeNA.getXy().getX(), treeNA.getBuildY());

            return TreenomeUtil.createTreePart(treeNA.getType(), buildXy);
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
            treeParts.add(TreenomeUtil.createTreePart(treeNA.getType(), treeNA.getXy()));
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
            TreenomeUtil.getNeighbors(treeNA, treeNAs).forEach(this::mutatePart);
        }
    }

    private List<PossibleAdditiveMutation> findPossibleAdditiveMutations(TreeNA treeNA) {
        List<TreeNA> neighbors = TreenomeUtil.getNeighbors(treeNA, treeNAs);

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
                if(mutationDirection == Direction.DOWN) {
                    possibleAdditiveMutationTypes.add(TreePartType.FRUIT);
                }
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
