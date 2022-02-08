import pygame

import config
from grid.grid_point import GridPoint


class GridObject:
    def __init__(self, x, y, color):
        self.xy = GridPoint(x, y)
        self.color = color
        self.surf = pygame.Surface(size=(config.CELL_WIDTH, config.CELL_HEIGHT))
        self.surf.fill(self.color)

    def draw(self, grid_surface):
        grid_surface.blit(self.surf, self.xy.as_pygame_tuple())

    def translate(self, dx, dy):
        self.xy.translate(dx, dy)
