from enum import Enum

from grid.grid_object import GridObject


class TreePartType(Enum):
    seed = 0
    trunk = 1
    branch = 2
    leaf = 3
    fruit = 4
    root = 5


class TreePart(GridObject):
    def __init__(self, x, y, color):
        super().__init__(x, y, color)
        self.parent_tree = None

    def hit_by_light(self, light_level):

