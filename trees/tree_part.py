from enum import Enum

from grid.grid_object import GridObject


class TreePartType(Enum):
    SEED = 'S'
    TRUNK = 'T'
    BRANCH = 'B'
    LEAF = 'L'
    FRUIT = 'F'
    ROOT = 'R'


def determine_part_color(part_type):
    match part_type:
        case TreePartType.SEED:
            return 80, 100, 40
        case TreePartType.TRUNK, TreePartType.BRANCH:
            return 80, 50, 50
        case TreePartType.LEAF:
            return 10, 120, 50
        case TreePartType.ROOT:
            return 100, 40, 50
        case TreePartType.FRUIT:
            return 200, 40, 40


class TreePart(GridObject):
    def __init__(self, part_type, x, y, color):
        self.parent_tree = None
        self.part_type = part_type
        self.color = determine_part_color(part_type)
        super().__init__(x, y, color)

    # shortcut method to get x coordinate of the tree part
    def get_x(self):
        return self.xy.x

    # shortcut method to get y coordinate of the tree part
    def get_y(self):
        return self.xy.y

    # determines what happens when the tree part gets sunlight
    def hit_by_light(self, light_level):
        # light level is at least 1 and max 10
        match self.part_type:
            case TreePartType.SEED:
                # seed will block all light but give tree energy
                self.parent_tree.obtain_energy(light_level)
                light_level = 0
            case TreePartType.LEAF:
                # leaf will block part of the light but give tree energy
                self.parent_tree.obtain_energy(light_level)
                light_level -= 3
            case TreePartType.TRUNK, TreePartType.ROOT:
                # trunk and root will block all light
                light_level = 0
            case TreePartType.BRANCH, TreePartType.FRUIT:
                # branch and fruit will block part of the light
                light_level -= 5
        # we return whatever left over light there is to be used farther down the column
        return light_level

    def __str__(self):
        return f'{self.part_type.value}, {self.xy.x}, {self.xy.y}'
