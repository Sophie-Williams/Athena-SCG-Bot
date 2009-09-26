import random
random.seed()


def solve(rn, vars, clauses):
  return solve_random(rn, vars, clauses)


def solve_random(rn, vars, clauses):
  d = {}
  for v in vars:
    d[v] = ('true', 'false')[random.randint(0, 1)]
  return d


def solution_str(solution):
  return '[ %s ]' % make_like_a_tree(solution)


def make_like_a_tree(solution):
  i = 0
  s = ''
  for k, v in solution.items():
    s += '(node red (%s -> %s)  ' % (k, v)
    i += 1

  s += ')' * i

  return s


s = {
  'v0' : 'true',
  'v1' : 'true',
  'v2' : 'true',
  'v3' : 'true',
  'v4' : 'true',
  'v5' : 'true',
}

print make_like_a_tree(s)


      
