from grid.grid_point import GridPoint


class TreeNA:
    # Tree DNA -> TreeNA
    def __init__(self, part_type, x, y):
        # position relative to seed (seed is 0, 0)
        self.xy = GridPoint(x, y)

        # tree part type
        self.part_type = part_type

        # keeps track of what y level this part gets built at
        self.build_y = self.get_y()

        # keeps track of when in the growth sequence this part is grown
        self.grow_number = -1

        # flag used for recursive treenome continuity algoritm
        self.checked = False

    # shortcut method to get x coordinate of the treena
    def get_x(self):
        return self.xy.x

    # shortcut method to get y coordinate of the treena
    def get_y(self):
        return self.xy.y

    # get the treena represented as a string, used to save the treenome line by line as a csv
    def __str__(self):
        return f'{self.part_type.value}, {self.xy.x}, {self.xy.y}'
