from trees.tree_part import TreePartType
from trees.treenome import treenome_util, growth_sequencer, mutator


class Treenome:
    def __init__(self, filepath):
        self.treenas = treenome_util.load_treena_from_csv(filepath)

        # mutation
        mutator.mutate_treenome(self)

        # growth sequence
        growth_sequencer.determine_growth_sequence(self)
        self.current_growth_number = 0

    def get_seed(self):
        for treena in self.treenas:
            if treena.part_type == TreePartType.SEED:
                return treena
        return None

    # return the next tree part in the growth sequence
    def get_next_growth(self):
        if self.current_growth_number < len(self.treenas):
            next_growth = self.treenas[self.current_growth_number]
            self.current_growth_number += 1
            return treenome_util.create_tree_part_from_treena(next_growth)
        return None

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
