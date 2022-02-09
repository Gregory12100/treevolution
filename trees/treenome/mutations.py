from grid.grid_point import GridPoint


class PossibleAdditiveMutation:
    def __init__(self, part_type, x, y):
        # position relative to seed (seed is 0, 0)
        self.xy = GridPoint(x, y)

        # tree part type
        self.part_type = part_type

    def __str__(self):
        return f'{self.part_type.value}, {self.xy.x}, {self.xy.y}'



