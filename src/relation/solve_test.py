#!/usr/bin/env python
import unittest
import time
import relation
import gen
import test_data


class TestSolve(unittest.TestCase):

  def test_solve(self):
    c_p = relation.Problem(test_data.vars1, test_data.clauses1)
    fsat, values = c_p.solve()
    self.assertEquals(fsat, 468)

    c_p = relation.Problem(test_data.vars2, test_data.clauses2)
    fsat, values = c_p.solve()
    self.assertEquals(fsat, 468)

    c_p = relation.Problem(test_data.vars3, test_data.clauses3)
    fsat, values = c_p.solve()
    self.assertEquals(fsat, 6)

    c_p = relation.Problem(test_data.vars4, test_data.clauses4)
    fsat, values = c_p.solve()
    self.assertEquals(fsat, 36)

    answers = [
      792, 792, 1584, 792, 1296, 1296, 2002, 792, 1584, 1584, 2376, 1296,
      2002, 2002, 2772, 792, 1296, 1296, 2002, 1584, 2002, 2002, 2592,
      1296, 2002, 2002, 2772, 2002, 2592, 2592, 3280, 792, 1296, 1296,
      2002, 1584, 2002, 2002, 2592, 1296, 2002, 2002, 2772, 2002, 2592,
      2592, 3280, 1584, 2002, 2002, 2592, 2376, 2772, 2772, 3280, 2002,
      2592, 2592, 3280, 2772, 3280, 3280, 3888,
    ]
    i = 0

    for rn in range(2, 128, 2):
      vars, clauses = gen.gen_problem(rn, 18)
      c_p = relation.Problem(vars, clauses)
      s = time.time()
      fsat, values = c_p.solve()
      e = time.time()
      self.assertEquals(answers[i], fsat)
      i += 1
      print ('%d (%d) / %d = %f vs %f : %f seconds' % (
        rn, fsat, len(clauses), float(fsat)/len(clauses),
        relation.break_even(rn, 3), (e - s)))
#       print (float(fsat) / len(clauses)) - relation.break_even(rn, 3)

if __name__ == '__main__':
  unittest.main()
