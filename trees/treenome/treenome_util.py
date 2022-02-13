import random

from util import util
from trees.tree_part import TreePartType, TreePart
from trees.treenome.treena import TreeNA


def load_treena_from_csv(filepath):
    rows = util.read_csv(filepath)
    treenas = []
    for row in rows:
        treenas.append(TreeNA(TreePartType(row[0]), int(row[1]), int(row[2])))
    return treenas


def write_treena_to_csv(filepath, treenas):
    rows = []
    for treena in treenas:
        row = [treena.part_type.value, treena.get_x(), treena.get_y()]
        rows.append(row)
    util.write_csv(filepath, rows)


# Does some checks on the treenome data that is read in to verify it is a valid treenome
def check_treenome_validity(treenome):
    # make sure there is only one seed
    seeds = list(filter(lambda p: p.part_type == TreePartType.SEED, treenome.treenas))
    if len(seeds) > 1:
        return False

    # already validated that there was only one seed so the seed is first element of seeds
    seed = seeds[0]

    # make sure that seed is at 0, 0
    if seed.get_x() != 0 or seed.get_y() != 0:
        return False

    # set all treena checked to false so that we can walk the treenome from the seed
    # and determine if all treena are connected and growable
    for treena in treenome.treenas:
        treena.checked = False

    walk_treenome(seed, treenome.treenas)

    for treena in treenome.treenas:
        if not treena.checked:
            print(f'ERROR: ISOLATED TREENA: {treena}')
            return False

    return True


# Checks whether or not a specific location is occupied by a treeNA in a treenome
def is_space_occupied(treenome, xy):
    for treena in treenome.treenas:
        if treena.xy.compare(xy):
            return True
    return False


# Finds the neighboring treena of a given treena
# Neighbors are immediately adjacent in the grid
def get_neighbors(treena: TreeNA, treenas: list[TreeNA]) -> list[TreeNA]:
    neighbors = []
    for other_treena in treenas:
        if treena.xy.is_adjacent(other_treena.xy):
            neighbors.append(other_treena)
    return neighbors


# Trunks get their own special neighbor finding method because of how they grow
# Trunks grow by getting inserted from the top of the seed while increasing the height of the branches and leaves
def get_neighbors_for_trunk(trunk: TreeNA, treenas: list[TreeNA], current_trunk_height: int, max_trunk_height: int) -> list[TreeNA]:
    trunk_mod_xy = trunk.xy.translate_new(0, max_trunk_height - current_trunk_height - trunk.get_y() + 1)

    neighbors = []
    for other_treena in treenas:
        # determine if other is horizontally adjacent at the appropriate trunk height
        # grabs branches and leaves that are out to the sides of the trunk
        if trunk_mod_xy.is_horizontal_adjacent(other_treena.xy):
            neighbors.append(other_treena)

        # determine if other is vertically adjacent at unmodified height
        # basically just grabs the below and above trunks
        if trunk.xy.is_vertical_adjacent(other_treena.xy):
            neighbors.append(other_treena)

    return neighbors


# Filters the list of neighbors down based on if that neighbor is growable from the input treeNA
# A neighbor is growable if it hasn't already been grown and,
# input treeNA is seed or,
# input treeNA is trunk and neighbor is trunk branch or leaf or,
# input treeNA is root and neighbor is root (root only grows root) or,
# input treeNA is branch and neighbor is branch or leaf,
# input treeNA is not leaf (leaf can't grow anything else from it)
# input treeNA is not fruit (fruit can't grow anything else from it)
def filter_non_growable_neighbors(treena: TreeNA, neighbors: list[TreeNA]) -> list[TreeNA]:
    # leaf and fruit cannot grow anything, return empty list
    match treena.part_type:
        case TreePartType.LEAF | TreePartType.FRUIT:
            return []

    # check if neighbor has previously been added to the grow sequence
    # if its already been added, then its no longer growable
    neighbors[:] = [p for p in neighbors if p.grow_number == -1]

    # seed can grow all types
    match treena.part_type:
        case TreePartType.SEED:
            return neighbors

    # root can only grow root
    # trunk can grow trunk, branch, and leaf
    # branch can grow branch, leaf, and fruit
    # TODO: should we be checking the direction of the neighbor from treena as well?
    neighbors_to_remove = []
    for neighbor in neighbors:
        match treena.part_type:
            case TreePartType.TRUNK:
                match neighbor.part_type:
                    case TreePartType.SEED | TreePartType.ROOT:
                        neighbors_to_remove.append(neighbor)
            case TreePartType.ROOT:
                if neighbor.part_type != TreePartType.ROOT:
                    neighbors_to_remove.append(neighbor)
            case TreePartType.BRANCH:
                match neighbor.part_type:
                    case TreePartType.SEED | TreePartType.ROOT | TreePartType.TRUNK:
                        neighbors_to_remove.append(neighbor)

    # do the actual removing
    for neighbor in neighbors_to_remove:
        neighbors.remove(neighbor)

    return neighbors


# handy method for creating new TreePart from TreeNA
def create_tree_part_from_treena(treena: TreeNA) -> TreePart:
    return TreePart(treena.part_type, treena.get_x(), treena.build_y)


# walk the treenome recursively starting from the input treena
# sets treena.checked to True for each treena that is reachable
# all treena with checked = False are isolated and can't be grown
def walk_treenome(treena, treenas):
    treena.checked = True
    reachable_treenas = filter_non_growable_neighbors(treena, get_neighbors(treena, treenas))
    for reachable_treena in reachable_treenas:
        if not reachable_treena.checked:
            walk_treenome(reachable_treena, treenas)


def get_random_treena_type():
    part_types = [p.value for p in TreePartType]
    return TreePartType(part_types[random.randrange(0, len(part_types))])
