package com.gmail.gregorysalsbery.treevolution.tree.dna;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Mutator {

    public static void mutateTreenome(Treenome treenome) {
        log.debug("Mutation started for treenome {}", treenome);

        // this will keep track of all the possible additive mutations
        List<PossibleAdditiveMutation> totalPossibleAdditiveMutations = new ArrayList<>();


    }
}
