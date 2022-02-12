import config
from trees.tree import Tree
from trees.treenome.treenome import Treenome


class Generation:
    def __init__(self, num_trees, treenome_filepath, sun):
        self.trees = []

        for i in range(0, num_trees):
            treenome = Treenome(treenome_filepath)
            treenome.mutate()
            treenome.determine_growth_sequence()
            tree = Tree(treenome, 100, config.GROUND_DEPTH-1, sun)
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
