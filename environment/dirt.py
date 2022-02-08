import config
from grid.grid_object import GridObject


class Dirt(GridObject):
    def __init__(self, x, y):
        super().__init__(x, y, config.COLOR_DIRT)
