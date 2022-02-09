package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.tree.dna.exceptions.GrowthSequenceException;
import com.gmail.gregorysalsbery.treevolution.tree.parts.TreePartType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class GrowthSequencer {

    private static Random rand = new Random();

    public static void determineGrowthSequence(Treenome treenome) throws GrowthSequenceException {
        int growNumber = 0;
        int trunkHeight = 0;
        final int maxTrunkHeight = treenome.getFullTrunkHeight();

        // start with the seed
        TreeNA seed = treenome.getSeeds().get(0);
        seed.setGrowNumber(growNumber);
        growNumber++;

        // list of all the treeNAs
        List<TreeNA> treeNAs = treenome.getTreeNAs();

        // list to keep track of possible growths, starts with the seed
        List<TreeNA> possibleGrowths = TreenomeUtil.filterOutNonGrowableNeighors(seed, TreenomeUtil.getNeighbors(seed, treeNAs));

        // list to keep track of possible growths that are currently underground
        // due to the trunk not being tall enough yet
        // they are connected however to other possible growths that are above ground
        // will be rechecked each loop to see if they are now above ground
        List<TreeNA> possibleGrowthsUnderground = new ArrayList<>();

        while(possibleGrowths.size() + possibleGrowthsUnderground.size() > 0) {
            log.debug("Start growth sequence loop");
            log.debug("Current trunk height is {}", trunkHeight);
            log.debug("Possible growth list is {}", possibleGrowths);
            log.debug("Possible growths underground is {}", possibleGrowthsUnderground);

            // randomly choose a next possible growth
            // TODO: allow probability weighting to different tree part types
            // TODO: also allow weighting to different growth directions - tree prefers up instead of out
            TreeNA nextGrowth = possibleGrowths.get(rand.nextInt(possibleGrowths.size()));
            nextGrowth.setGrowNumber(growNumber);
            log.debug("Next growth is {}", nextGrowth);
            possibleGrowths.remove(nextGrowth);
            growNumber++;

            // assign the build xy position
            // this is needed because branches and leaves may be built lower down and then go up later to their final xy as the trunk grows
            // so we have to know where to put it at the time its built (or grown)
            // its only the y position that matters
            if(nextGrowth.getType() == TreePartType.LEAF ||
                    nextGrowth.getType() == TreePartType.BRANCH ||
                    nextGrowth.getType() == TreePartType.FRUIT) {
                int buildY = nextGrowth.getXy().getY() - maxTrunkHeight + trunkHeight;
                log.debug("build Y will be {}", buildY);
                nextGrowth.setBuildY(buildY);
            }

            // if we just grew a trunk, then the trunk height increases
            if(nextGrowth.getType() == TreePartType.TRUNK) {
                trunkHeight++;
                // parts that were underground before may now be above ground
                // add them to the possible growths list if so
                for(TreeNA possibleGrowthUnderground : possibleGrowthsUnderground) {
                    if(!treeNAIsUnderground(possibleGrowthUnderground, trunkHeight, maxTrunkHeight)) {
                        possibleGrowths.add(possibleGrowthUnderground);
                    }
                }
                // remove the now above ground parts from the underground list
                // which is all the parts that were added to the possible growth list
                possibleGrowthsUnderground.removeIf(possibleGrowths::contains);
            }

            // add any new possible growths that the next growth has attached to it
            // if the next growth was a trunk, then we have to use the modified trunk neighbor finder method
            List<TreeNA> neighbors;
            if(nextGrowth.getType() == TreePartType.TRUNK) {
                neighbors = TreenomeUtil.getNeighborsForTrunk(nextGrowth, treeNAs, trunkHeight, maxTrunkHeight);
            } else {
                neighbors = TreenomeUtil.getNeighbors(nextGrowth, treeNAs);
            }
            // filter out the things that can't be grown based on type
            List<TreeNA> growableNeighbors = TreenomeUtil.filterOutNonGrowableNeighors(nextGrowth, neighbors);
            // move any of the growable neighbors that are underground to the possibleGrowthsUnderground list
            for(TreeNA growableNeighbor : growableNeighbors) {
                if(treeNAIsUnderground(growableNeighbor, trunkHeight, maxTrunkHeight) &&
                    growableNeighbor.getType() != TreePartType.ROOT &&
                    growableNeighbor.getType() != TreePartType.TRUNK) {
                    // growable neighbor is underground right now
                    possibleGrowthsUnderground.add(growableNeighbor);
                }
            }
            // remove from growable neighbors if the neighbor is in the underground list
            growableNeighbors.removeIf(possibleGrowthsUnderground::contains);
            // add remaining growable neighbors to the possible growths list
            possibleGrowths.addAll(growableNeighbors);
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

        // print details
        log.debug("Here is the final grow sequence");
        for(TreeNA treeNA : treeNAs) {
            log.debug("{} - {} at ({},{})", treeNA.getGrowNumber(), treeNA.getType(), treeNA.getXy().getX(), treeNA.getXy().getY());
        }
        log.debug("Total sequence length: {}", treeNAs.size());
    }

    private static boolean treeNAIsUnderground(TreeNA treeNA, int trunkHeight, int maxTrunkHeight) {
        return treeNA.getXy().getY() - (maxTrunkHeight - trunkHeight) <= 0;
    }
}
