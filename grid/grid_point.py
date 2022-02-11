import math

import config
from grid.direction import Direction


class GridPoint:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def as_tuple(self):
        return self.x, self.y

    # converts grid coordinates to actual screen pixel coordinates
    def as_pygame_tuple(self):
        return self.x * config.CELL_WIDTH, config.GRID_HEIGHT - (self.y + 1) * config.CELL_HEIGHT

    # translates the GridPoint
    def translate(self, dx, dy):
        self.x += dx
        self.y += dy

    # returns a new GridPoint that is offset from the original
    def translate_new(self, dx, dy):
        return GridPoint(self.x + dx, self.y + dy)

    def compare(self, other_xy):
        return self.x == other_xy.x and self.y == other_xy.y

    # true straight line distance to another point
    def distance(self, other_xy):
        return math.sqrt(pow(self.horizontal_distance(other_xy), 2) + pow(self.vertical_distance(other_xy), 2))

    def horizontal_distance(self, other_xy):
        return abs(self.x - other_xy.x)

    def vertical_distance(self, other_xy):
        return abs(self.y - other_xy.y)

    # get the grid distance between this point and another
    # note that grid distance is not the straight line distance
    def grid_distance(self, other_xy):
        return self.horizontal_distance(other_xy) + self.vertical_distance(other_xy)

    def is_horizontal_adjacent(self, other_xy):
        return self.horizontal_distance(other_xy) == 1 and self.vertical_distance(other_xy) == 0

    def is_vertical_adjacent(self, other_xy):
        return self.horizontal_distance(other_xy) == 0 and self.vertical_distance(other_xy) == 1

    def is_adjacent(self, other_xy):
        # note that != is equivalent to xor for booleans
        # practically this method would also work with just normal or
        return self.is_horizontal_adjacent(other_xy) != self.is_vertical_adjacent(other_xy)

    # returns a new adjacent grid point in the given direction
    def get_adjacent(self, direction):
        match direction:
            case Direction.UP:
                return self.translate_new(0, 1)
            case Direction.DOWN:
                return self.translate_new(0, -1)
            case Direction.LEFT:
                return self.translate_new(-1, 0)
            case Direction.RIGHT:
                return self.translate_new(1, 0)
