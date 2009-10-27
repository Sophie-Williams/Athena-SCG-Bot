import math
import itertools


def max_vars(max_clauses, rank=3):
  """Return the maximum number of variables we can use, given the maximum
  number of clauses."""
  x = rank
  k = math.factorial(x)
  y = 1

  while k < max_clauses:
    x += 1
    k = (k / y) * x
    y += 1
  return x - 1


def var(n):
  return 'v%d' % n


def gen_problem(p_type, degree):
  vars = map(var, range(0, degree))
  clauses = []
  for i, j, k in itertools.combinations(range(0, degree), 3):
    clauses.append((p_type, 1, var(i), var(j), var(k)))
  return vars, clauses
