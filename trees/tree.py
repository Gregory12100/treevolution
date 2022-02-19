from grid.grid_point import GridPoint
from trees.tree_part import TreePartType


class Tree:
    def __init__(self, id, treenome, x, y, sun):
        self.id = id
        self.treenome = treenome
        self.xy = GridPoint(x, y)
        self.sun = sun

        self.parts = list()
        self.energy = 10000

    def draw(self, grid_surface):
        for part in self.parts:
            part.draw(grid_surface)

    # shortcut method to get x coordinate of the tree
    def get_x(self):
        return self.xy.x

    # shortcut method to get y coordinate of the tree
    def get_y(self):
        return self.xy.y

    def get_full_size(self):
        return self.treenome.get_size()

    def get_size(self):
        return len(self.parts)

    def is_full_grown(self):
        return self.get_size() == self.get_full_size()

    def get_height_above_seed(self):
        max_y = 0
        for part in self.parts:
            if part.get_y() > max_y:
                max_y = part.get_y()
        return max_y - self.get_y()

    def get_depth_below_seed(self):
        min_y = 0
        for part in self.parts:
            if part.get_y() < min_y:
                min_y = part.get_y()
        return self.get_y() - min_y

    def get_height(self):
        self.get_height_above_seed() + self.get_depth_below_seed()

    def get_width_left(self):
        min_x = 0
        for part in self.parts:
            if part.get_x() < min_x:
                min_x = part.get_x()
        return self.get_x() - min_x

    def get_width_right(self):
        max_x = 0
        for part in self.parts:
            if part.get_x() > max_x:
                max_x = part.get_x()
        return max_x - self.get_x()

    def get_width(self):
        return self.get_width_left() + self.get_width_right()

    def get_num_fruits(self):
        count = 0
        for part in self.parts:
            if part.part_type == TreePartType.FRUIT:
                count += 1
        return count

    def obtain_energy(self, amount):
        self.energy += amount

    def grow_higher(self):
        for part in self.parts:
            match part.part_type:
                case TreePartType.LEAF | TreePartType.BRANCH | TreePartType.FRUIT:
                    part.translate_up()

    def grow(self):
        if self.energy >= 1000:
            next_growth = self.treenome.get_next_growth()
            if next_growth is None:
                return

            # translate the next growth so that its lined up with the tree
            next_growth.translate(self.get_x(), self.get_y())
            # tell it that its now part of this tree
            next_growth.parent_tree = self
            # add it to the tree parts for this tree
            self.parts.append(next_growth)

            # let the sun know about this part so that it can sort it into a light column
            # roots don't matter though
            if next_growth.part_type != TreePartType.ROOT:
                self.sun.register_part(next_growth)

            # if we just grew a trunk, then we have to move stuff up
            if next_growth.part_type == TreePartType.TRUNK:
                self.grow_higher()

            # subtract the energy used to build the part
            self.energy -= 1000

    # FIXME: this is a not a very good scoring calculation
    # TODO: try scoring based mostly around the number of fruits the tree grows
    def get_score(self):
        # return self.energy/1000 + len(self.parts) + self.get_num_fruits()*10
        return (self.get_num_fruits() + 1) * self.get_height_above_seed()
        # return self.get_height_above_seed()

