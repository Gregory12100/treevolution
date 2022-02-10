import unittest

from grid.grid_point import GridPoint


class TestGridPoint(unittest.TestCase):

    def test_compare(self):
        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertTrue(xy1.compare(xy2))
        self.assertTrue(xy2.compare(xy1))

    def test_distance(self):
        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertEqual(0, xy1.distance(xy2))

        xy1 = GridPoint(1, 0)
        xy2 = GridPoint(1, 2)
        self.assertEqual(2, xy1.distance(xy2))

        xy1 = GridPoint(0, 0)
        xy2 = GridPoint(1, 2)
        self.assertEqual(2.23606797749979, xy1.distance(xy2))

    def test_grid_distance(self):
        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertEqual(0, xy1.grid_distance(xy2))

        xy1 = GridPoint(1, 0)
        xy2 = GridPoint(1, 2)
        self.assertEqual(2, xy1.grid_distance(xy2))

        xy1 = GridPoint(0, 0)
        xy2 = GridPoint(1, 2)
        self.assertEqual(3, xy1.grid_distance(xy2))

    def test_is_horizontal_adjacent(self):
        xy1 = GridPoint(0, 2)
        xy2 = GridPoint(1, 2)
        self.assertTrue(xy1.is_horizontal_adjacent(xy2))

        xy1 = GridPoint(0, 2)
        xy2 = GridPoint(2, 2)
        self.assertFalse(xy1.is_horizontal_adjacent(xy2))

        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertFalse(xy1.is_horizontal_adjacent(xy2))

    def test_is_vertical_adjacent(self):
        xy1 = GridPoint(1, 1)
        xy2 = GridPoint(1, 2)
        self.assertTrue(xy1.is_vertical_adjacent(xy2))

        xy1 = GridPoint(1, 0)
        xy2 = GridPoint(1, 2)
        self.assertFalse(xy1.is_vertical_adjacent(xy2))

        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertFalse(xy1.is_vertical_adjacent(xy2))

    def test_is_adjacent(self):
        xy1 = GridPoint(1, 1)
        xy2 = GridPoint(1, 2)
        self.assertTrue(xy1.is_adjacent(xy2))

        xy1 = GridPoint(0, 2)
        xy2 = GridPoint(1, 2)
        self.assertTrue(xy1.is_adjacent(xy2))

        xy1 = GridPoint(1, 0)
        xy2 = GridPoint(1, 2)
        self.assertFalse(xy1.is_adjacent(xy2))

        xy1 = GridPoint(1, 2)
        xy2 = GridPoint(1, 2)
        self.assertFalse(xy1.is_adjacent(xy2))
