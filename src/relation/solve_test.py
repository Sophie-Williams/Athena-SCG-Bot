#!/usr/bin/env python
import unittest
import time
import relation
import gen
import test_data
import sys


class TestSolve(unittest.TestCase):

  def test_solve_from_other(self):
    c_p = relation.Problem(test_data.vars1, test_data.clauses1)
    sat, values = c_p.solve()
    self.assertEquals(sat, 468)

    c_p = relation.Problem(test_data.vars2, test_data.clauses2)
    sat, values = c_p.solve()
    self.assertEquals(sat, 468)

    c_p = relation.Problem(test_data.vars3, test_data.clauses3)
    sat, values = c_p.solve()
    self.assertEquals(sat, 6)

    c_p = relation.Problem(test_data.vars4, test_data.clauses4)
    sat, values = c_p.solve()
    self.assertEquals(sat, 36)

  def test_solve_from_us(self):
    # The best we can to on a short notice.
    answers = [
      0, 705, 415, 777, 790, 1120, 765, 1260, 819, 660, 820, 819, 819, 1071,
      780, 1260, 435, 840, 501, 865, 808, 1176, 834, 1262, 819, 1056, 822,
      995, 858, 1071, 934, 1323, 780, 651, 816, 882, 840, 1135, 1086, 1326,
      1365, 1386, 1365, 1260, 1365, 1365, 1365, 1341, 840, 861, 824, 869,
      840, 1386, 980, 1335, 1365, 1386, 1365, 1156, 1386, 1365, 1323, 1386,
    ]

    i = 0
    for rn in range(0, 128, 2):
      vars, clauses = gen.gen_problem(rn, 23)

      c_p = relation.Problem(vars, clauses)

      s = time.time()
      sat, values = c_p.evergreen_aggressive()
      e = time.time()

      self.assertEquals(answers[i], sat)
      i += 1
#      diff = (float(sat) / len(clauses)) - relation.break_even(rn, 3)
#      if diff < 0:
#        sys.stdout.write('%d (%d) / %d = %f : %f seconds\n'
#                         % (rn, sat, len(clauses), diff, (e - s)))
#        sys.stdout.flush()

if __name__ == '__main__':
  unittest.main()
