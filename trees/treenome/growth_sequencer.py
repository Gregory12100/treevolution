import random

from trees.tree_part import TreePartType
from trees.treenome import treenome_util
from trees.treenome.treena import TreeNA


def inheritable_growth_sequencer(treenome):
    # run the normal growth sequencer for the first gen
    # keep track of whether the next growth added to the sequence was dependent on the last
    # its dependent on the last if it gets added to possible growths after next growth is selected
    # two routes, it could be one of the underground parts that got added to possible list
    # or it could be a neighbor that got added
    # if it was not dependant, then it can probably be swapped ordering with the last with no consequence
    # how to handle mutation
    # additive case
    # see when the part first becomes available in the sequencer and just add it there
    # or give it some probability of being added at each step after its available...
    # subtractive case
    # could wipe out large portion of the growth sequence
    # redo sequence from that point with preference to the previous ordering with parts that still exist?
    # first step is to get this working without mutation

    # keep same growth sequence as the last time
    # but as mutations become growable, have some probability of inserting them
    # after new sequence is complete, then mutate the sequence with the above idea

    treenas = treenome.treenas

    grow_number = 0
    grow_number_offset = 0
    trunk_height = 0
    max_trunk_height = treenome.get_full_trunk_height()

    # sort the treenas by grow number
    treenas.sort(key=lambda p: p.grow_number)

    print("In inheritable growth sequencer")
    for treena in treenas:
        print(treena)

    # find all the treena that have just been added due to mutation
    mads = []
    for treena in treenas:
        if treena.grow_number == -1:
            mads.append(treena)

    # a list to keep track of mutations that are possible to be inserted at each step
    pams = []
    pams_underground = []

    # step through treenas in order of the pre-existing grow number
    # at each step, make sure the next growth is still possible
    # if it's not, throw it in a list and recheck on next step
    # I think there are some subtractive mutation scenarios that could cause that
    # then check if the additive mutations are possible yet
    for treena in treenas:
        if treena.grow_number == -1:
            # skip since this is a mutation that was just added
            continue

        # assign the build y position this is needed because branches and leaves may be built lower down and then go
        # up later to their final y as the trunk grows so we have to know where to put it at the time its built (or
        # grown)
        # in all other cases, build y is just the normal y
        match treena.part_type:
            case TreePartType.LEAF | TreePartType.BRANCH | TreePartType.FRUIT:
                build_y = treena.get_y() - max_trunk_height + trunk_height
                treena.build_y = build_y

        # if we just grew a trunk, then the trunk height increases
        if treena.part_type == TreePartType.TRUNK:
            trunk_height += 1

        # check if mutations are possible after growing this treena
        # mutations are possible if they neighbor this treena (and are not underground)
        # or if they have now just become no longer underground

        print(f'Next growth is {treena}')
        print(f'    build_y is {treena.build_y}')

        neighbors = []
        if treena.part_type == TreePartType.TRUNK:
            neighbors = treenome_util.get_growable_neighbors_for_trunk(treena, treenas, trunk_height,
                                                                       max_trunk_height)
        else:
            neighbors = treenome_util.get_growable_neighbors(treena, treenas)

        print(f'    neighbors are:')
        for neighbor in neighbors:
            print(f'        {neighbor}')

        for mad in mads:
            if mad in neighbors:
                print(f'        found neighbor mutation {mad}')


def determine_growth_sequence(treenome):
    grow_number = 0
    trunk_height = 0
    max_trunk_height = treenome.get_full_trunk_height()

    # start with the seed
    seed = treenome.get_seed()
    seed.grow_number = grow_number
    grow_number += 1

    treenas = treenome.treenas

    # list to keep track of the possible growths, start with the seed
    possible_growths = treenome_util.get_growable_neighbors(seed, treenas)

    # list to keep track of possible growths that are currently underground
    # due to the trunk not being tall enough yet
    # they are connected, however, to other possible growths that are above ground
    # will be rechecked each loop to see if they are now above ground
    # they could be above ground if the trunk grew higher
    possible_growths_underground = []

    while len(possible_growths) > 0:
        # randomly choose a next possible growth
        # TODO: allow probability weighting to different tree part types
        # TODO: also allow weighting to different growth directions - tree prefers up instead of out
        next_growth = possible_growths[random.randrange(0, len(possible_growths))]
        next_growth.grow_number = grow_number
        next_growth.grown = True
        possible_growths.remove(next_growth)
        grow_number += 1

        # assign the build y position this is needed because branches and leaves may be built lower down and then go
        # up later to their final y as the trunk grows so we have to know where to put it at the time its built (or
        # grown)
        # in all other cases, build y is just the normal y
        match next_growth.part_type:
            case TreePartType.LEAF | TreePartType.BRANCH | TreePartType.FRUIT:
                build_y = next_growth.get_y() - max_trunk_height + trunk_height
                next_growth.build_y = build_y

        # if we just grew a trunk, then the trunk height increases
        if next_growth.part_type == TreePartType.TRUNK:
            trunk_height += 1
            # have to check parts that were underground
            # they may now be above ground and should be added to possible growths if so
            for treena in possible_growths_underground:
                if not is_treena_underground(treena, trunk_height, max_trunk_height):
                    possible_growths.append(treena)
            # remove the now above ground parts from the underground list
            # which is all the parts that were added to the possible growth list
            # TODO: seems like there's probably a more efficient way to do this type of thing
            for treena in possible_growths:
                if treena in possible_growths_underground:
                    possible_growths_underground.remove(treena)

        # add any new possible growths that are attached to next_growth
        neighbors = []
        if next_growth.part_type == TreePartType.TRUNK:
            neighbors = treenome_util.get_growable_neighbors_for_trunk(next_growth, treenas, trunk_height,
                                                                       max_trunk_height)
        else:
            neighbors = treenome_util.get_growable_neighbors(next_growth, treenas)

        # place any growable neighbors that are currently underground in the possible growth underground list
        # this only applies to leaves, branches, and fruits
        for neighbor in neighbors:
            if is_treena_underground(neighbor, trunk_height, max_trunk_height):
                match neighbor.part_type:
                    case TreePartType.LEAF | TreePartType.BRANCH | TreePartType.FRUIT:
                        possible_growths_underground.append(neighbor)
        # remove from the growable neighbors if the neighbor is now in the underground list
        for treena in possible_growths_underground:
            if treena in neighbors:
                neighbors.remove(treena)
        # add any remaining growable neighbors to the possible growths list
        possible_growths.extend(neighbors)
        # remove any duplicates (I don't think there should be any, but just in case)
        possible_growths = list(set(possible_growths))

    # check that all parts got a grow number
    for treena in treenas:
        if treena.grow_number < 0:
            print(f'ERROR: TreeNA {treena} did not get a grow number assigned!')

    # sort the list by grow number
    treenas.sort(key=lambda p: p.grow_number)

    # print the resulting growth sequence for debugging
    # print("Here is the growth sequence")
    # for treena in treenas:
    #     print(f'{treena.grow_number}: {treena}')


# determine if a treeNA is currently underground
def is_treena_underground(treena: TreeNA, trunk_height: int, max_trunk_height: int):
    return treena.get_y() - (max_trunk_height - trunk_height) <= 0
