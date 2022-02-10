from trees.tree_part import TreePartType


class Tree:
    def __init__(self, treenome, x, y, sun):
        self.treenome = treenome
        self.x = x
        self.y = y
        self.sun = sun

        self.parts = list()
        self.energy = 10000

    def draw(self, grid_surface):
        for part in self.parts:
            part.draw(grid_surface)

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
            next_growth.translate(self.x, self.y)
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

