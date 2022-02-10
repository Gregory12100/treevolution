import config
from environment.dirt import Dirt
from environment.sun import Sun
from trees.tree import Tree
from trees.treenome.treenome import Treenome


def init_ground():
    dirt_list = []
    for y in range(0, 10):
        for x in range(0, int(config.GRID_SIZE_X)):
            dirt_list.append(Dirt(x, y))
    return dirt_list


class SimHandler:
    def __init__(self):
        self.ground = init_ground()
        self.sun = Sun()

        self.treenome = Treenome('resources/treena_test.csv')
        self.tree = Tree(self.treenome, 50, 9, self.sun)

    def update(self):
        self.sun.shine()
        self.tree.grow()

    def draw(self, grid_surface):
        for dirt in self.ground:
            dirt.draw(grid_surface)

        self.tree.draw(grid_surface)
