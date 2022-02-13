import config
from environment.dirt import Dirt
from environment.sun import Sun
from trees.generation import Generation
from util.timer import Timer


def init_ground():
    dirt_list = []
    for y in range(0, config.GROUND_DEPTH):
        for x in range(0, int(config.GRID_SIZE_X)):
            dirt_list.append(Dirt(x, y))
    return dirt_list


class SimHandler:
    def __init__(self):
        self.ground = init_ground()
        self.sun = Sun()

        # create the timer that will control when a generation ends and the next one starts
        self.timer = Timer()
        self.timer.start(config.GENERATION_TIME)

        self.generation = Generation(config.GENERATION_SIZE, 'resources/treena_test.csv', self.sun)
        self.generation_count = 0

    def update(self, dt):
        self.timer.update(dt)
        if self.timer.is_time_up():
            # start a new generation
            print("Start a new generation")
            self.sun = Sun()
            filepath = f'resources/runs/best_from_gen_{self.generation_count}.csv'
            self.generation.get_best().treenome.write_to_file(filepath)
            self.generation = Generation(config.GENERATION_SIZE, filepath, self.sun)
            self.generation_count += 1
            self.timer.start(config.GENERATION_TIME)

        self.sun.shine()
        self.generation.update()

    def draw(self, grid_surface):
        for dirt in self.ground:
            dirt.draw(grid_surface)

        self.generation.draw(grid_surface)
