import config


class GridPoint:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def as_tuple(self):
        return self.x, self.y

    def as_pygame_tuple(self):
        return self.x * config.CELL_WIDTH, config.GRID_HEIGHT - (self.y + 1) * config.CELL_HEIGHT

    def translate(self, dx, dy):
        self.x += dx
        self.y += dy


