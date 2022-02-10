import unittest

from trees.tree_part import TreePartType
from trees.treenome.treena import TreeNA


class TestTreenomeUtil(unittest.TestCase):

    def test_filter_non_growable_neighbors(self):
        treena = TreeNA(TreePartType.TRUNK, 0, 2)
        # TODO: finish test case