package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.Direction;
import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Mutator {

    private static final Random rand = new Random();

    public static void mutateTreenome(Treenome treenome) {
        log.debug("Mutation started for treenome {}", treenome);

        // get all the treeNAs from the treenome
        List<TreeNA> treeNAs = treenome.getTreeNAs();

        // TODO: will use this to limit how high leaves and branches can be added
        int fullTrunkHeight = treenome.getFullTrunkHeight();

        // this list will keep track of all the possible additive mutations
        List<PossibleAdditiveMutation> possibleAdditiveMutations = new ArrayList<>();

        // loop through each treeNA and determine the possible additive mutations for each
        for(TreeNA treeNA : treeNAs) {
            possibleAdditiveMutations.addAll(findPossibleAdditiveMutations(treenome, treeNA));
        }

        // loop through each of the possible additive mutations and determine whether to add them
        // based on some low probability
        List<PossibleAdditiveMutation> additiveMutationsToAdd = new ArrayList<>();
        for(PossibleAdditiveMutation mutation : possibleAdditiveMutations) {
            if(rand.nextFloat() < 0.1f) {
                // there may be multiple possible mutations in the same spot
                // check that another mutation has not already been added in this mutation's spot
                if(!checkMutationXyAlreadyOccupied(mutation, additiveMutationsToAdd)) {
                    additiveMutationsToAdd.add(mutation);
                }
            }
        }

        // instantiate the mutations as new TreeNAs
        for(PossibleAdditiveMutation mutation : additiveMutationsToAdd) {
            treeNAs.add(new TreeNA(mutation.getType(), mutation.getXy()));
        }

        // TODO: subtractive mutations
        // will have to re-check the whole treenome to make sure the body plan is still valid
        // if some parts have been isolated, then will have to remove them

        // TODO: final type of mutation would be changing one type of part into another (leaf becomes branch, etc...)
        // again, will have to re-check the whole treenome to make sure the body plan is still valid
        // if some parts have been isolated, then will have to remove them
    }

    private static Boolean checkMutationXyAlreadyOccupied(PossibleAdditiveMutation mutation, List<PossibleAdditiveMutation> mutationsAlreadyAdded) {
        for(PossibleAdditiveMutation otherMutation : mutationsAlreadyAdded) {
            if(mutation.getXy().compare(otherMutation.getXy())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all of the possible additive mutations for a given treeNA
     * Looks at all 4 adjacent neighboring grid spaces and determines if those
     * spaces are occupied by other treeNA. If they are not, then an additive mutation
     * could occur in that spot if the given treeNA is of the right type.
     * Some types can't grow anything new off of them (leaves can't grow anything).
     * Some types can only grow new things in specific directions (seed can only grow trunk up or root down).
     * Some types can only be grown in specific directions (fruit can only be grown on bottom of branch).
     *
     * @param treenome - the overall treenome we are mutating
     * @param treeNA - the treeNA to find the possible additive mutations for
     * @return - a list of the possible additive mutations
     */
    private static List<PossibleAdditiveMutation> findPossibleAdditiveMutations(Treenome treenome, TreeNA treeNA) {
        List<PossibleAdditiveMutation> possibleAdditiveMutations = new ArrayList<>();

        // leaves and fruit cannot grow anything off of themselves, so they cannot have an additive mutation
        // so return an empty list
        if(treeNA.getType() == TreePartType.LEAF || treeNA.getType() == TreePartType.FRUIT) {
            return possibleAdditiveMutations;
        }
        
        // check in all 4 directions
        possibleAdditiveMutations.addAll(checkAdjacentSpace(treenome, treeNA, Direction.UP));
        possibleAdditiveMutations.addAll(checkAdjacentSpace(treenome, treeNA, Direction.DOWN));
        possibleAdditiveMutations.addAll(checkAdjacentSpace(treenome, treeNA, Direction.LEFT));
        possibleAdditiveMutations.addAll(checkAdjacentSpace(treenome, treeNA, Direction.RIGHT));

        return possibleAdditiveMutations;
    }

    private static List<PossibleAdditiveMutation> checkAdjacentSpace(Treenome treenome, TreeNA treeNA, Direction direction) {
        List<PossibleAdditiveMutation> possibleAdditiveMutations = new ArrayList<>();

        GridPoint spaceToCheck = null;
        switch (direction) {
            case UP -> spaceToCheck = treeNA.getXy().getAdjacentUp();
            case DOWN -> spaceToCheck = treeNA.getXy().getAdjacentDown();
            case LEFT -> spaceToCheck = treeNA.getXy().getAdjacentLeft();
            case RIGHT -> spaceToCheck = treeNA.getXy().getAdjacentRight();
        }

        // if the space is already occupied, then we go ahead and return an empty list
        // no additive mutations are possible in the occupied space
        if(TreenomeUtil.isSpaceOccupied(treenome, spaceToCheck)) {
            return possibleAdditiveMutations;
        }

        // the space is not occupied, so we might be able to add a mutation there
        // depends on direction and type
        return findGrowableMutations(treeNA.getType(), direction, spaceToCheck);
    }

    private static List<PossibleAdditiveMutation> findGrowableMutations(TreePartType parentType, Direction direction, GridPoint mutationXy) {
        List<PossibleAdditiveMutation> possibleAdditiveMutations = new ArrayList<>();
        switch (parentType) {
            case SEED -> {
                switch (direction) {
                    case UP -> {
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.LEAF));
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.TRUNK));
                    }
                    case DOWN -> {
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.ROOT));
                    }
                }
            }
            case TRUNK -> {
                switch (direction) {
                    case UP -> {
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.TRUNK));
                    }
                    case LEFT, RIGHT -> {
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.LEAF));
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.BRANCH));
                    }
                }
            }
            case ROOT -> {
                switch (direction) {
                    case DOWN, LEFT, RIGHT -> {
                        possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.ROOT));
                    }
                }
            }
            case BRANCH -> {
                possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.LEAF));
                possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.BRANCH));
                if (direction == Direction.DOWN) {
                    possibleAdditiveMutations.add(new PossibleAdditiveMutation(mutationXy, TreePartType.FRUIT));
                }
            }
        }
        return possibleAdditiveMutations;
    }
}
