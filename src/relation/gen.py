import math


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


def permute3(vars):
  for i in range(0, vars):
    for j in range(0, vars):
      for k in range(0, vars):
        if i != j and j != k and i != k:
          yield (i, j, k)


def var(n):
  return 'v%d' % n

def problem(p_type, degree=100):
  y = ''
  for x in range(0, degree):
    y += '%s ' % var(x)
  y += ' '
  for i, j, k in permute3(degree):
    y += '(%d %d %s %s %s ) ' % (p_type, 1, var(i), var(j), var(k))
  return y

def gen_problem(p_type, degree):
  vars = map(var, range(0, degree))
  clauses = []
  for i, j, k in permute3(degree):
    clauses += [(p_type, 1, var(i), var(j), var(k))]
  return vars, clauses
