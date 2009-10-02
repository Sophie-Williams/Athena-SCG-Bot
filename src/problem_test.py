#!/usr/bin/env python
import unittest
import problem

#TODO(wan): Write tests.
class ProblemTestCase(unittest.TestCase):

  def test_GenerateSolution(self):
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
    pblm = problem.Problem(100, ['v0', 'v1', 'v2', 'v3', 'v4'], [], 501,
                           102, 109, 1.0)
    for clause in clauses:
      pblm.AddClause(clause)

    sol = ('solve[[ (node black (v1 -> false) (node black (v3 -> false)'
           ' (node red (v4 -> false)  ) (node red (v2 -> false)  )) '
           '(node black (v0 -> false)  )) ] 501]')
    self.assertEqual(pblm.GetSolution(), sol)

if __name__ == '__main__':
  unittest.main()
