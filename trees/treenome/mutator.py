import random

from grid.direction import Direction
from trees.tree_part import TreePartType
from trees.treenome import treenome_util
from trees.treenome.mutations import PossibleAdditiveMutation
from trees.treenome.treena import TreeNA


def mutate_treenome(treenome):
    # subtractive mutations
    subtractive_mutation(treenome)

    # additive mutations
    additive_mutation(treenome)

    # TODO: part type change mutation


def additive_mutation(treenome):
    # TODO: limit how high leaves and branches can be added

    # get treenas
    treenas = treenome.treenas

    # a list to keep track of all the possible additive mutations (PAMs)
    pams = []

    # find possible additive mutations for each treena
    for treena in treenas:
        pams.extend(find_possible_additive_mutations(treenome, treena))

    # go through all of the possible additive mutations and
    # decide whether to add them based on some low probability
    # mutations to add (MADs)
    mads = []
    for pam in pams:
        if random.random() < 0.1:
            # there may be multiple possible mutations at the same location
            # make sure that another mutation has not already been added in this mutation's spot
            if not check_mutation_xy_already_occupied(pam, mads):
                mads.append(pam)

    # instantiate the mutations as new TreeNAs in the treenome
    print('Adding the following mutations:')
    for mutation in mads:
        print(mutation)
        treenas.append(TreeNA(mutation.part_type, mutation.get_x(), mutation.get_y()))


# checks if a grid space has already been taken by a previously added mutation
def check_mutation_xy_already_occupied(possible_mutation, added_mutations):
    for added_mutation in added_mutations:
        if possible_mutation.xy.compare(added_mutation.xy):
            return True
    return False


# Finds all of the possible additive mutations for a given treeNA
# Looks at all 4 adjacent neighboring grid spaces and determines if those
# spaces are occupied by other treeNA. If they are not, then an additive mutation
# could occur in that spot if the given treeNA is of the right type.
# Some types can't grow anything new off of them (leaves can't grow anything).
# Some types can only grow new things in specific directions (seed can only grow trunk up or root down).
# Some types can only be grown in specific directions (fruit can only be grown on bottom of branch).
def find_possible_additive_mutations(treenome, treena):
    # possible additive mutation list
    pams = []

    # leaves and fruit can't grow anything off of themselves, so they can't have an additive mutation
    # so return an empty list
    if treena.part_type == TreePartType.LEAF or treena.part_type == TreePartType.FRUIT:
        return pams

    # check for possible mutations in each of the 4 directions
    pams.extend(find_growable_mutations(treenome, treena, Direction.UP))
    pams.extend(find_growable_mutations(treenome, treena, Direction.DOWN))
    pams.extend(find_growable_mutations(treenome, treena, Direction.LEFT))
    pams.extend(find_growable_mutations(treenome, treena, Direction.RIGHT))

    return pams


def find_growable_mutations(treenome, treena, direction):
    # possible additive mutations list
    pams = []

    # get the adjacent space in the given direction
    mutation_point = treena.xy.get_adjacent(direction)

    # if the space is already occupied then we can't have an additive mutation
    # return an empty list
    if treenome_util.is_space_occupied(treenome, mutation_point):
        return pams

    # depending on the direction and types involved, we can have an additive mutation
    match treena.part_type:
        case TreePartType.SEED:
            match direction:
                case Direction.UP:
                    pams.append(PossibleAdditiveMutation(TreePartType.LEAF, mutation_point.x, mutation_point.y))
                    pams.append(PossibleAdditiveMutation(TreePartType.TRUNK, mutation_point.x, mutation_point.y))
                case Direction.DOWN:
                    pams.append(PossibleAdditiveMutation(TreePartType.ROOT, mutation_point.x, mutation_point.y))
        case TreePartType.TRUNK:
            match direction:
                case Direction.UP:
                    pams.append(PossibleAdditiveMutation(TreePartType.TRUNK, mutation_point.x, mutation_point.y))
                case Direction.LEFT | Direction.RIGHT:
                    pams.append(PossibleAdditiveMutation(TreePartType.LEAF, mutation_point.x, mutation_point.y))
                    pams.append(PossibleAdditiveMutation(TreePartType.BRANCH, mutation_point.x, mutation_point.y))
        case TreePartType.ROOT:
            match direction:
                case Direction.DOWN | Direction.LEFT | Direction.RIGHT:
                    pams.append(PossibleAdditiveMutation(TreePartType.ROOT, mutation_point.x, mutation_point.y))
        case TreePartType.BRANCH:
            pams.append(PossibleAdditiveMutation(TreePartType.LEAF, mutation_point.x, mutation_point.y))
            pams.append(PossibleAdditiveMutation(TreePartType.BRANCH, mutation_point.x, mutation_point.y))
            if direction == Direction.DOWN:
                pams.append(PossibleAdditiveMutation(TreePartType.FRUIT, mutation_point.x, mutation_point.y))

    return pams


def subtractive_mutation(treenome):
    treenas = treenome.treenas
    seed = treenome.get_seed()

    # set checked to false for all treena
    # this will be used later to tell which parts are still connected
    for treena in treenas:
        treena.checked = False

    # with some low probability get rid of the some of the treena
    # this will very likely cause sections of the tree to become isolated
    # if a section is isolated, the entire isolated section will be removed as well
    # so a single subtractive mutation could cause a large portion of the tree to mutate away
    # ttr = TreeNA To Remove
    ttr = []
    for treena in treenas:
        # don't allow the seed to be removed
        if treena.part_type == TreePartType.SEED:
            continue

        # low probability of removing a treena
        if random.random() < 0.01:
            ttr.append(treena)

    # do the actual removal
    for treena in ttr:
        treenas.remove(treena)

    # print for debug
    print('Subtractive mutation for the following parts:')
    for treena in ttr:
        print(treena)

    # walk the treenome from the seed to see which parts are still connected and which are now isolated
    # treena.checked will be set True for all parts that are reachable
    treenome_util.walk_treenome(seed, treenas)

    # add all the treena with checked False to the removal list
    ttr = []
    for treena in treenas:
        if not treena.checked:
            ttr.append(treena)

    # do the actual removal
    for treena in ttr:
        treenas.remove(treena)

    # print for debug
    print('The following parts became isolated and were also removed:')
    for treena in ttr:
        print(treena)
