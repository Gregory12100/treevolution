import random

import config
from trees.tree import Tree
from trees.treenome.treenome import Treenome


# returns an x value for where to plant the tree
# ensures that the whole tree will fit on the grid
# by checking the width of the treenome
def get_random_planting_x(treenome):
    return random.randrange(treenome.get_width_left(), config.GRID_SIZE_X - treenome.get_width_right())


class Generation:
    def __init__(self, num_trees, treenome_filepath, sun):
        self.trees = []

        for i in range(0, num_trees):
            treenome = Treenome(treenome_filepath)
            tree = Tree(treenome, get_random_planting_x(treenome), config.GROUND_DEPTH - 1, sun)
            self.trees.append(tree)

    def update(self):
        for tree in self.trees:
            tree.grow()

    def draw(self, grid_surface):
        for tree in self.trees:
            tree.draw(grid_surface)

    def get_best(self):
        self.trees.sort(key=lambda t: t.get_score(), reverse=True)
        return self.trees[0]
