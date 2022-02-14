import random

from grid.direction import Direction
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
def get_growable_neighbors(treena: TreeNA, treenas: list[TreeNA]) -> list[TreeNA]:
    neighbors = []
    for other_treena in treenas:
        if treena.xy.is_adjacent(other_treena.xy):
            if other_treena.grow_number == -1:
                if other_treena.part_type in get_growable_types(treena, treena.xy.get_direction(other_treena.xy)):
                    neighbors.append(other_treena)
    return neighbors


# Trunks get their own special neighbor finding method because of how they grow
# Trunks grow by getting inserted from the top of the seed while increasing the height of the branches and leaves
# this also filters out the non-growable neighbors to leave only the growable ones
def get_growable_neighbors_for_trunk(trunk: TreeNA, treenas: list[TreeNA], current_trunk_height: int, max_trunk_height: int) -> list[TreeNA]:
    trunk_mod_xy = trunk.xy.translate_new(0, max_trunk_height - current_trunk_height - trunk.get_y() + 1)

    neighbors = []
    for other_treena in treenas:
        # determine if other is horizontally adjacent at the appropriate trunk height
        # grabs branches and leaves that are out to the sides of the trunk
        if trunk_mod_xy.is_horizontal_adjacent(other_treena.xy):
            if other_treena.grow_number == -1:
                if other_treena.part_type in get_growable_types(trunk, trunk_mod_xy.get_direction(other_treena.xy)):
                    neighbors.append(other_treena)

        # determine if other is vertically adjacent at unmodified height
        # basically just grabs the below and above trunks
        if trunk.xy.is_vertical_adjacent(other_treena.xy):
            if other_treena.grow_number == -1:
                if other_treena.part_type in get_growable_types(trunk, trunk.xy.get_direction(other_treena.xy)):
                    neighbors.append(other_treena)

    return neighbors


# handy method for creating new TreePart from TreeNA
def create_tree_part_from_treena(treena: TreeNA) -> TreePart:
    return TreePart(treena.part_type, treena.get_x(), treena.build_y)


# walk the treenome recursively starting from the input treena
# sets treena.checked to True for each treena that is reachable
# all treena with checked = False are isolated and can't be grown
def walk_treenome(treena, treenas):
    treena.checked = True
    reachable_treenas = get_growable_neighbors(treena, treenas)
    for reachable_treena in reachable_treenas:
        if not reachable_treena.checked:
            walk_treenome(reachable_treena, treenas)


def get_random_treena_type():
    part_types = [p.value for p in TreePartType]
    return TreePartType(part_types[random.randrange(0, len(part_types))])


# this is the set of rules that determines what can grow what in what directions
def get_growable_types(treena, direction):
    growable_types = []
    match treena.part_type:
        case TreePartType.SEED:
            match direction:
                case Direction.UP:
                    growable_types.append(TreePartType.LEAF)
                    growable_types.append(TreePartType.TRUNK)
                case Direction.DOWN:
                    growable_types.append(TreePartType.ROOT)
        case TreePartType.TRUNK:
            match direction:
                case Direction.UP:
                    growable_types.append(TreePartType.TRUNK)
                case Direction.LEFT | Direction.RIGHT:
                    growable_types.append(TreePartType.LEAF)
                    growable_types.append(TreePartType.BRANCH)
        case TreePartType.ROOT:
            match direction:
                case Direction.DOWN | Direction.LEFT | Direction.RIGHT:
                    growable_types.append(TreePartType.ROOT)
        case TreePartType.BRANCH:
            growable_types.append(TreePartType.LEAF)
            growable_types.append(TreePartType.BRANCH)
            if direction == Direction.DOWN:
                growable_types.append(TreePartType.FRUIT)
    return growable_types
