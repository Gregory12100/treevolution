import config


class LightColumn:
    def __init__(self):
        self.column_of_tree_parts = list()

    def register_part(self, tree_part):
        self.column_of_tree_parts.append(tree_part)

    def shine(self, light_level):
        # sort the column in reverse order so that the highest y is first
        self.column_of_tree_parts.sort(key=lambda p: p.get_y(), reverse=True)

        # iterate down the column, applying light level appropriately
        for part in self.column_of_tree_parts:
            # if two parts happen to occupy the same space, then whichever happens to be first will get the most
            # light, the next one getting the reduced light
            light_level = part.hit_by_light(light_level)
            # no need to iterate over the tree parts below if the light level has been reduced to 0
            if light_level <= 0:
                break


class Sun:
    START_LIGHT_LEVEL = 10

    def __init__(self):
        self.light_columns = dict()
        # add a light column for each x position in the grid
        for x in range(0, config.GRID_SIZE_X):
            self.light_columns[x] = LightColumn()

    def register_part(self, tree_part):
        # add a tree part to its appropriate light column by x position
        self.light_columns[tree_part.get_x()].register_part(tree_part)

    def shine(self):
        # start shine for each light column
        for x in range(0, config.GRID_SIZE_X):
            self.light_columns[x].shine(Sun.START_LIGHT_LEVEL)