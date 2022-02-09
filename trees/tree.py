from trees.tree_part import TreePartType


class Tree:
    def __init__(self, treenome, x, y, sun):
        self.treenome = treenome
        self.x = x
        self.y = y
        self.sun = sun

        self.parts = list()
        self.energy = 5000

    def draw(self):
        for part in self.parts:
            part.draw()

    def obtain_energy(self, amount):
        self.energy += amount

    def grow_higher(self):
        for part in self.parts:
            match part.part_type:
                case TreePartType.LEAF, TreePartType.BRANCH, TreePartType.FRUIT:
                    part.translate_up()
