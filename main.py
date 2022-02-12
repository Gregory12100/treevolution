import config
from sim_handler import SimHandler

import pygame

pygame.init()

screen = pygame.display.set_mode([config.SCREEN_WIDTH, config.SCREEN_HEIGHT])
clock = pygame.time.Clock()

sim = SimHandler()

running = True
while running:
    # get any user input
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    # update everything
    dt = clock.get_time()
    sim.update(dt)

    # paint the background
    screen.fill(config.COLOR_BACKGROUND)

    # draw everything
    sim.draw(screen)

    # update the screen
    pygame.display.flip()

    # ensure fixed fps
    clock.tick(60)

pygame.quit()
