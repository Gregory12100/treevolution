import util
from grid.grid_point import GridPoint
from trees.tree_part import TreePartType


def load_treena_from_csv(filepath):
    rows = util.read_csv(filepath)
    treena_list = list()
    for row in rows:
        treena_list.append(TreeNA(TreePartType(row[0]), row[1], row[2]))
    return treena_list


# Tree DNA -> TreeNA
class TreeNA:
    def __init__(self, part_type, x, y):
        # position relative to seed (seed is 0, 0)
        self.xy = GridPoint(x, y)

        # tree part type
        self.part_type = part_type

        # keeps track of what y level this part gets built at
        self.build_y = -1

        # keeps track of when in the growth sequence this part is grown
        self.grow_number = -1

    # get the treena represented as a string, used to save the treenome line by line as a csv
    def __str__(self):
        return f'{self.part_type.value}, {self.xy.x}, {self.xy.y}'


class Treenome:
    def __init__(self, filepath):
        self.treena_list = load_treena_from_csv(filepath)

        # instead of a current growth number
        # may be able to keep track of next growth with an iterator of the treena list

    def get_next_growth(self):
        pass

    def get_all_growths(self):
        pass

    def get_size(self):
        return len(self.treena_list)

    def __str__(self):
        treenome_string = ''
        for treena in self.treena_list:
            treenome_string += treena.__str__() + '\n'
        return treenome_string
