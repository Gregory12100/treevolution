from trees.tree_part import TreePartType
from trees.treenome import treenome_util, growth_sequencer, mutator


class Treenome:
    def __init__(self, filepath):
        self.treenas = treenome_util.load_treena_from_csv(filepath)
        self.current_growth_number = 0

        self.mutate()
        treenome_util.check_treenome_validity(self)
        # self.determine_growth_sequence()
        growth_sequencer.inheritable_growth_sequencer(self)

    def mutate(self):
        mutator.mutate_treenome(self)

    def determine_growth_sequence(self):
        growth_sequencer.determine_growth_sequence(self)

    def write_to_file(self, filepath):
        treenome_util.write_treena_to_csv(filepath, self)

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

    def get_size(self):
        return len(self.treenas)

    def get_full_trunk_height(self):
        return len(list(filter(lambda p: p.part_type == TreePartType.TRUNK, self.treenas)))

    def get_height_above_seed(self):
        max_y = 0
        for treena in self.treenas:
            if treena.get_y() > max_y:
                max_y = treena.get_y()
        return max_y

    def get_depth_below_seed(self):
        min_y = 0
        for treena in self.treenas:
            if treena.get_y() < min_y:
                min_y = treena.get_y()
        return abs(min_y)

    def get_height(self):
        self.get_height_above_seed() + self.get_depth_below_seed()

    def get_width_left(self):
        min_x = 0
        for treena in self.treenas:
            if treena.get_x() < min_x:
                min_x = treena.get_x()
        return abs(min_x)

    def get_width_right(self):
        max_x = 0
        for treena in self.treenas:
            if treena.get_x() > max_x:
                max_x = treena.get_x()
        return max_x

    def get_width(self):
        return self.get_width_left() + self.get_width_right()

    def __str__(self):
        treenome_string = ''
        for treena in self.treenas:
            treenome_string += treena.__str__() + '\n'
        return treenome_string
