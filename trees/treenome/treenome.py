from trees.tree_part import TreePartType
from trees.treenome import treenome_util


class Treenome:
    def __init__(self, filepath):
        self.treenas = treenome_util.load_treena_from_csv(filepath)

        # TODO: mutation

        # TODO: growth sequence
        # instead of a current growth number
        # may be able to keep track of next growth with an iterator of the treena list

    def get_next_growth(self):
        pass

    def get_all_growths(self):
        pass

    def get_size(self):
        return len(self.treenas)

    def get_full_trunk_height(self):
        return len(list(filter(lambda p: p.part_type == TreePartType.TRUNK, self.treenas)))

    def __str__(self):
        treenome_string = ''
        for treena in self.treenas:
            treenome_string += treena.__str__() + '\n'
        return treenome_string
