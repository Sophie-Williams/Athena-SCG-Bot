#!/usr/bin/env python
import unittest

import problem

class ClauseTestCase(unittest.TestCase):
  def setUp(self):
    self.c = problem.Clause(109, ['v1', 'v2', 'v3'])
    self.d = problem.Clause(109, ['v1', 'v2', 'v3'])
    self.e = problem.Clause(110, ['v1', 'v2', 'v3'])
    self.f = problem.Clause(109, ['v0', 'v2', 'v3'])

  def tearDown(self):
    del self.c
    del self.d
    del self.e
    del self.f

  def testInit(self):
    self.assertEqual(self.c.problemnumber, 109)
    self.assertEqual(self.c.vars, ['v1', 'v2', 'v3'])
    self.assertEqual(self.e.problemnumber, 110)
    self.assertEqual(self.e.vars, ['v1', 'v2', 'v3'])
    self.assertEqual(self.f.problemnumber, 109)
    self.assertEqual(self.f.vars, ['v0', 'v2', 'v3'])

  def testEq(self):
    self.assertEqual(self.c, self.d)
    self.assertNotEqual(self.c, self.e)
    self.assertNotEqual(self.c, self.f)
  
  def testStr(self):
    self.assertEqual(str(self.c),
                     "Clause(num=109, vars=['v1', 'v2', 'v3'])")
    self.assertEqual(str(self.e),
                     "Clause(num=110, vars=['v1', 'v2', 'v3'])")
    self.assertEqual(str(self.f),
                     "Clause(num=109, vars=['v0', 'v2', 'v3'])")

  def testRepr(self):
    self.assertEqual(str(self.c), repr(self.c))
    self.assertEqual(str(self.d), repr(self.d))
    self.assertEqual(str(self.e), repr(self.e))
    self.assertEqual(str(self.f), repr(self.f))

  def testGetTuple(self):
    self.assertEqual(self.c.GetTuple(), (109, 'v1', 'v2', 'v3'))
    self.assertEqual(self.d.GetTuple(), (109, 'v1', 'v2', 'v3'))
    self.assertEqual(self.e.GetTuple(), (110, 'v1', 'v2', 'v3'))
    self.assertEqual(self.f.GetTuple(), (109, 'v0', 'v2', 'v3'))

  def testGetProvideBlob(self):
    self.assertEqual(self.c.GetProvideBlob(), '(109 v1 v2 v3 )')
    self.assertEqual(self.d.GetProvideBlob(), '(109 v1 v2 v3 )')
    self.assertEqual(self.e.GetProvideBlob(), '(110 v1 v2 v3 )')
    self.assertEqual(self.f.GetProvideBlob(), '(109 v0 v2 v3 )')


class ProblemTestCase(unittest.TestCase):

  def setUp(self):
    #provided[100 v0 v1 v2 v3 v4  (109 v4 v0 v1 ) (109 v1 v2 v3 )
    #         (109 v4 v0 v1 ) (109 v2 v3 v4 ) (109  v2 v3 v4 ) (109 v1 v2 v3 )
    #         (109 v3 v4 v0 ) (109 v1 v2 v3 ) (109 v0 v1 v2 ) (109 v4 v0 v1 )
    #         501 102 (109 ) 1.0]
    clauses = []
    clauses.append(problem.Clause(109, ['v4', 'v0', 'v1']))
    clauses.append(problem.Clause(109, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(109, ['v4', 'v0', 'v1']))
    clauses.append(problem.Clause(109, ['v2', 'v3', 'v4']))
    clauses.append(problem.Clause(109, ['v2', 'v3', 'v4']))
    clauses.append(problem.Clause(109, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(109, ['v3', 'v4', 'v0']))
    clauses.append(problem.Clause(109, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(109, ['v0', 'v1', 'v2']))
    clauses.append(problem.Clause(109, ['v4', 'v0', 'v1']))
    self.clauses = clauses
    self.pblm = problem.Problem(100, ['v0', 'v1', 'v2', 'v3', 'v4'], [], 501,
                                102, 109, 1.0)

  def tearDown(self):
    del self.clauses
    del self.pblm

  def test_GenerateSolution(self):
    for clause in self.clauses:
      self.pblm.AddClause(clause)
    sol = ('solve[[ (node black (v1 -> false) (node black (v0 -> false)'
           '  ) (node black (v3 -> false) (node red (v2 -> false)  )'
           ' (node red (v4 -> false)  ))) ] 501]')
    self.assertEqual(self.pblm.GetPySolution(), sol)

  def testCandPySolve(self):
    for clause in self.clauses:
      self.pblm.AddClause(clause)
    self.assertEqual(self.pblm.GetPySolution(), self.pblm.GetCSolution())

  def test_IsClauseSat(self):
    clauses = []
    clauses.append(problem.Clause(214, ['v0', 'v1', 'v2']))
    clauses.append(problem.Clause(214, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(214, ['v4', 'v0', 'v1']))
    clauses.append(problem.Clause(214, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(214, ['v3', 'v4', 'v0']))
    clauses.append(problem.Clause(214, ['v2', 'v2', 'v4']))
    clauses.append(problem.Clause(214, ['v1', 'v2', 'v3']))
    clauses.append(problem.Clause(214, ['v0', 'v1', 'v2']))
    clauses.append(problem.Clause(214, ['v2', 'v3', 'v4']))
    clauses.append(problem.Clause(214, ['v3', 'v4', 'v0']))
    pblm = problem.Problem(100, ['v0', 'v1', 'v2', 'v3', 'v4'], [], 501,
                           102, 214, 1.0)
    for clause in clauses:
      pblm.AddClause(clause)

    solution = [1, 1, 1, 1, 1]
    for x in clauses:
      self.assert_(pblm.IsClauseSat(x, solution))


if __name__ == '__main__':
  unittest.main()
