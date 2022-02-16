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

        self.gen_number = 0
        start_gene_pool = ['resources/treenome_starter1.csv']
        print(f"Start generation {self.gen_number}")
        self.generation = Generation(self.gen_number, config.GENERATION_SIZE, start_gene_pool, self.sun)

    def update(self, dt):
        self.timer.update(dt)
        if self.timer.is_time_up():
            # start a new generation
            self.generation.save()
            gene_pool = self.generation.get_gene_pool()

            best_tree = self.generation.get_best()
            print(f'-best tree had score of {best_tree.get_score()}')

            self.sun = Sun()
            self.gen_number += 1
            print(f"Start generation {self.gen_number}")
            self.generation = Generation(self.gen_number, config.GENERATION_SIZE, gene_pool, self.sun)
            self.timer.start(config.GENERATION_TIME)

        self.sun.shine()
        self.generation.update()

    def draw(self, screen, fonts):
        # FIXME: just noting that this is a terribly inefficient way to do the ground
        # could just be one big ugly brown rectangle
        # unless I go forward with the plan to make each piece of dirt have some resource for the trees
        for dirt in self.ground:
            dirt.draw(screen)

        self.generation.draw(screen)

        # draw some text on the screen
        screen.blit(fonts['title'].render('Treevolution', False, (0, 0, 0)), (10, 0))
        screen.blit(fonts['info'].render(f'generation: {self.gen_number}', False, (0, 0, 0)), (10, 40))
        screen.blit(fonts['info'].render(f'time to next: {int(self.timer.remaining_time)}', False, (0, 0, 0)), (10, 58))
