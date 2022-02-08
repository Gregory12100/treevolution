import config
import trees.tree
from environment.dirt import Dirt


def init_ground():
    dirt_list = []
    for y in range(0, 10):
        for x in range(0, int(config.GRID_SIZE_X)):
            dirt_list.append(Dirt(x, y))
    return dirt_list


class SimHandler:
    def __init__(self):
        self.ground = init_ground()

    def update(self):
        pass

    def draw(self, grid_surface):
        for dirt in self.ground:
            dirt.draw(grid_surface)

