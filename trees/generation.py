import os.path
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
    def __init__(self, gen_number, num_trees, gene_pool, sun):
        self.gen_number = gen_number

        self.trees = []

        for i in range(0, num_trees):
            treenome = Treenome(gene_pool[random.randrange(0, len(gene_pool))])
            tree = Tree(i, treenome, get_random_planting_x(treenome), config.GROUND_DEPTH - 1, sun)
            self.trees.append(tree)

        self.save_directory = f'resources/runs/gen{self.gen_number}'

    def update(self):
        for tree in self.trees:
            tree.grow()

    def draw(self, grid_surface):
        for tree in self.trees:
            tree.draw(grid_surface)

    def get_best(self):
        self.trees.sort(key=lambda t: t.get_score(), reverse=True)
        return self.trees[0]

    def save(self):
        if not os.path.exists(self.save_directory):
            os.makedirs(self.save_directory)

        for tree in self.trees:
            tree.treenome.write_to_file(f'{self.save_directory}/tree{tree.id}.csv')

    def get_gene_pool(self):
        # add up the scores of all the trees
        # each tree will get a weighting attached to their treenome that corresponds to their score
        # higher score, higher weighting

        gene_pool = []
        for tree in self.trees:
            for i in range(0, tree.get_score()):
                gene_pool.append(f'{self.save_directory}/tree{tree.id}.csv')
        return gene_pool
