# very simple countdown timer
# use to schedule when new generations happen
class Timer:
    def __init__(self):
        self.remaining_time = 0
        self.running = False

    # start the timer counting down
    # start time is in seconds
    def start(self, start_time):
        self.remaining_time = start_time
        self.running = True

    # dt is the time since the last frame in milliseconds
    def update(self, dt):
        self.remaining_time -= dt/1000
        if self.remaining_time <= 0:
            self.running = False

    # true if the timer is done counting down
    def is_time_up(self):
        return not self.running
