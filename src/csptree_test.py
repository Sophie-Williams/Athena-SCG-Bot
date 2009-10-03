#!/usr/bin/env python
import unittest
import csptree

class CSPTreeTestCase(unittest.TestCase):

  def testCreateSolution1(self):
    vars = ['v1', 'v2', 'v3']
    values = [1, 0, 0]
    solution = csptree.CSPTree.CreateSolution(vars, values)
    self.assertEqual(str(solution),
                     '(node black (v2 -> false) (node red (v1 -> true)  ) '
                     '(node red (v3 -> false)  ))')

  def testCreateSolution2(self):
    vars = ['v1', 'v10', 'v2', 'v3']
    values = [1, 0, 1, 0]
    solution = csptree.CSPTree.CreateSolution(vars, values)
    self.assertEqual(str(solution),
                     '(node black (v10 -> false) (node black (v1 -> true) '
                     ' ) (node black (v2 -> true)  (node red (v3 -> false)'
                     '  )))')


if __name__ == '__main__':
  unittest.main()

