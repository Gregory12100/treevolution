package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.grid.GridPoint;
import com.gmail.gregorysalsbery.treevolution.tree.dna.exceptions.TreeNAInvalidException;
import com.gmail.gregorysalsbery.treevolution.tree.parts.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TreenomeUtil {

    /**
     * Does some checks on the treenome data that is read in to verify it is a valid treenome
     *
     * @throws TreeNAInvalidException - if the treenome data is not valid
     */
    public static void checkTreenomeIsValid(Treenome treenome) throws TreeNAInvalidException {
        if(treenome.getSeeds().size() > 1) {
            throw new TreeNAInvalidException("Tree DNA contains multiple seeds!");
        } else if(treenome.getSeeds().size() < 1) {
            throw new TreeNAInvalidException("Tree DNA does not contain a seed!");
        }

        TreeNA seed = treenome.getSeeds().get(0);
        if(seed.getXy().getX() != 0 || seed.getXy().getY() != 0) {
            throw new TreeNAInvalidException("Seed is not located at (0, 0) in tree DNA!");
        }

        // TODO: more validity checks
    }

    /**
     * Checks whether or not a specific location is occupied by a treeNA in a treenome
     *
     * @param treenome - treenome to check
     * @param xy - grid point to check
     * @return - true if the space is occupied, false otherwise
     */
    public static Boolean isSpaceOccupied(Treenome treenome, GridPoint xy) {
        for(TreeNA treeNA : treenome.getTreeNAs()) {
            if(treeNA.getXy().compare(xy)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all the neighbors of a treeNA
     * Neighbors are other treeNA that are immediately adjacent
     *
     * @param treeNA - the treeNA to find neighbors for
     * @param treeNAs - the treeNA list to search through
     * @return - a list of the neighbors
     */
    public static List<TreeNA> getNeighbors(TreeNA treeNA, List<TreeNA> treeNAs) {
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
     * Trunks get their own special neighbor finding method because of how they grow
     * Trunks grow by getting inserted from the top of the seed while increasing the height of the branches and leaves
     *
     * @param trunk - the trunk to find neighbors for
     * @param treeNAs - the treeNA list to search through
     * @return - a list of the neighbors for the trunk
     */
    public static List<TreeNA> getNeighborsForTrunk(TreeNA trunk, List<TreeNA> treeNAs, int currentTrunkHeight, int maxTrunkHeight) {
        GridPoint trunkModXy = trunk.getXy().translateNew(0, maxTrunkHeight - currentTrunkHeight - trunk.getXy().getY() + 1);

        log.debug("Finding neighbors for trunk {}", trunk);
        log.debug("trunk modified xy is {}", trunkModXy);

        List<TreeNA> neighbors = new ArrayList<>();
        for(TreeNA other : treeNAs) {
            // check for self, don't want to include self in its list of neighbors
            if(other == trunk) {
                continue;
            }
            // determine if other is horizontally adjacent at the appropriate trunk height
            // grabs branches and leaves that are out to the sides of the trunk
            if(trunkModXy.isHorizontalAdjacent(other.getXy())) {
                log.debug("found this horizontal neighbor {}", other);
                neighbors.add(other);
            }
            // determine if other is vertically adjacent at unmodified height
            // basically just grabs the below and above trunks
            if(trunk.getXy().isVerticalAdjacent(other.getXy())) {
                log.debug("found this vertical neighbor {}", other);
                neighbors.add(other);
            }
        }
        return neighbors;
    }

    /**
     * Filters the list of neighbors down based on if that neighbor is growable from the input treeNA
     *
     * A neighbor is growable if it hasn't already been grown and,
     * input treeNA is seed or,
     * input treeNA is trunk and neighbor is trunk branch or leaf or,
     * input treeNA is root and neighbor is root (root only grows root) or,
     * input treeNA is branch and neighbor is branch or leaf,
     * input treeNA is not leaf (leaf can't grow anything else from it)
     * input treeNA is not fruit (fruit can't grow anything else from it)
     *
     * @param treeNA - the tree part to find neighbors for
     * @param neighbors - the neighbor list to filter
     * @return - a list of neighbors that are growable from the input treeNA
     */
    public static List<TreeNA> filterOutNonGrowableNeighors(TreeNA treeNA, List<TreeNA> neighbors) {
        // leaf and fruit cannot grow anything, return empty list
        if(treeNA.getType() == TreePartType.LEAF || treeNA.getType() == TreePartType.FRUIT) {
            return new ArrayList<>();
        }

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
        // branch can grow branch and leaf and fruit
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
     * Creates a {@link TreePart} of the appropriate variety from a {@link TreePartType} and a {@link GridPoint}.
     *
     * @param type - type of TreePart to create
     * @param xy - grid position to create the TreePart at
     * @return - the created TreePart
     */
    public static TreePart createTreePart(TreePartType type, GridPoint xy) {
        switch (type) {
            case SEED -> {
                return new Seed(xy);
            }
            case TRUNK -> {
                return new Trunk(xy);
            }
            case ROOT -> {
                return new Root(xy);
            }
            case BRANCH -> {
                return new Branch(xy);
            }
            case LEAF -> {
                return new Leaf(xy);
            }
            case FRUIT -> {
                return new Fruit(xy);
            }
            default -> {
                return null;
            }
        }
    }
}
