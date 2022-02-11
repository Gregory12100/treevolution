from grid.grid_point import GridPoint


class PossibleAdditiveMutation:
    def __init__(self, part_type, x, y):
        # position relative to seed (seed is 0, 0)
        self.xy = GridPoint(x, y)

        # tree part type
        self.part_type = part_type

    # shortcut method to get x coordinate of the mutation
    def get_x(self):
        return self.xy.x

    # shortcut method to get y coordinate of the mutation
    def get_y(self):
        return self.xy.y

    def __str__(self):
        return f'{self.part_type.value}, {self.xy.x}, {self.xy.y}'



