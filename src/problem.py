#!/usr/bin/env python
import gflags
import itertools
import logging
import random
import threading

import constants
import csptree
import proxysolver
import relation

gflags.DEFINE_enum('solver', 'c', ['proxy', 'python', 'c'],
                   'Problem solver to use')
gflags.DEFINE_integer('problemdegree', 13, 'Degree of generated problems')
# gflags.DEFINE_integer('iterlimit', 22,
#                       'The maximum degree to solve with iteration.')
# XXX Ideally, we would have the flag, but we want to hide this information.
#     Also, this really depends on hardware...
ITERLIMIT = 22

FLAGS = gflags.FLAGS

class Clause(object):
  """Represents a CSP problem clause."""

  def __init__(self, number, weight=1, list_of_vars=None):
    """Initialize a clause.

    :param number: (int) problem (relation) number
    :param list_of_vars: (list) list of clause variables like v1, v2, v3
    """
    self.problemnumber = int(number)
    self.weight = int(weight)
    if not list_of_vars:
      self.vars = []
    else:
      self.vars = list(list_of_vars)

  def __eq__(self, other):
    """Is this equal to other?."""
    try:
      return (self.vars == other.vars
              and self.problemnumber == other.problemnumber
              and self.weight == other.weight)
    except:
      return False

  def __str__(self):
    """Get a human readable string representing this clause."""
    return ('Clause(num=%d, weight=%d, vars=%s)'
            % (self.problemnumber, self.weight, str(self.vars)))

  def __repr__(self):
    """Get a human readable representation of this clause."""
    return str(self)

  def GetTuple(self):
    """Get a Tuple representing this clause.

    Returns:
      A tuple in the form (problemnumber, var1, var2, var3, ...)
    """
    return tuple([self.problemnumber, self.weight]+self.vars)

  def GetProvideBlob(self):
    """Get a provide[] blob representing this clause.

    Used by the Problem class to generate problem provide messages.
    Returns:
      A string in the form "(problemnumber var1 var2 var3 ...)"
    """
    return '(%d {%d} %s )' % (self.problemnumber, self.weight,
                              ' '.join(self.vars))

class Problem(object):
  """Describes a problem instance."""
  def __init__(self, buyer, list_of_vars, clauselist, challengeid, seller,
               problemnumbers, price, kind='all'):
    self.buyer = int(buyer)
    self.vars = list(list_of_vars)
    self.vars.sort()
    self.clauses = []
    self.AddClauses(clauselist)
    self.challengeid = int(challengeid)
    self.seller = int(seller)
    self.problemnumbers = map(int, list(problemnumbers))
    self.problemnumbers.sort()
    self.price = float(price)
    self.solution = None
    self.profit = 0
    self.kind = kind

  def __str__(self):
    return ('Problem(num=%s, vars=%s, price=%s, seller=%s clauses=%d)'
            % (self.problemnumbers, str(self.vars), self.price, self.seller,
               len(self.clauses)))

  def __repr__(self):
    return str(self)

  def AddClauses(self, clauselist):
    """Add Clauses to this Problem instance.

    :param clauselist: list of iterables where i[0] is the problemnumber and
      i[1] is the clause weight
    """
    for clause in clauselist:
      self.AddClause(Clause(clause[0], weight=clause[1],
                            list_of_vars=clause[2:]))

  def AddClause(self, clause):
    """Add a Clause to this Problem instance.

    :param clause: A single clause object
    """
    self.clauses.append(clause)

  def GetProvide(self):
    """Get a 'provide' blob to send to the administrator for this problem."""
    if self.kind == 'secret':
      self.Solve(forprovide=True)
      solution = ' [ %s ]' % self.solution
    else:
      solution = ''
    random.shuffle(self.clauses)
    return ('provide[%s %s%s %d]'
            % (' '.join(self.vars),
               ' '.join([x.GetProvideBlob() for x in self.clauses]),
               solution,
               self.challengeid))

  def GetProvided(self):
    """Get a 'provided' blob to send to the proxysolve backend."""
    return ('provided[%d %s %s %d %d (%s ) %0.8f]'
            % (self.buyer, ' '.join(self.vars),
               ' '.join([x.GetProvideBlob() for x in self.clauses]),
               self.challengeid, self.seller,
               ' '.join(map(str, self.problemnumbers)), self.price))

  def TrivialSolve(self):
    if len(self.problemnumbers) > 1:
      return False
    fsat = len(self.clauses)
    if self.problemnumbers[0] == 0:
      logging.debug('Special Case Solve: Relation 0!')
      values = [0]*len(self.vars)
    elif self.problemnumbers[0] % 2:
      logging.debug('Special Case Solve: All False!')
      values = [0]*len(self.vars)
    elif self.problemnumbers[0] >= 128:
      logging.debug('Special Case Solve: All True!')
      values = [1]*len(self.vars)
    else:
      return False
    logging.debug('Trivially Solved # %s challenge %d'
                  % (self.problemnumbers, self.challengeid))
    return fsat, values

  def CSolve(self):
    """Solve this Problem instance using the C solver."""
    filtered, vars = self.FilterSolve()
    c_p = relation.Problem(tuple(vars),
                          [x.GetTuple() for x in self.clauses])
    if len(vars) <= ITERLIMIT: # XXX see at the top.
      logging.debug('Using Iowa Lime Sparrow Solver')
      fsat, values = c_p.solve()
    else:
      logging.debug('Using Alabama Blue Turtle Solver')
      fsat, values = c_p.evergreen_aggressive()
    if filtered:
      values = self.TransposeValues(vars, values)
    return fsat, values

  def TransposeValues(self, usedvars, values):
    self.vars.sort()
    # Create the default mapping.
    mapping = dict([(v, 0) for v in self.vars])
    # Write over with the used variables.
    for k, v in zip(usedvars, values):
      mapping[k] = v

    return [mapping[v] for v in self.vars]

  def FilterSolve(self):
    # Accumulate all variables used in clauses onto usedvars.
    usedvars = set()
    for x in self.clauses:
      usedvars.update(list(x.vars))

    # Sort usedvars to compare it with vars.
    # XXX Should we use sets instead (like set.issubset)?
    vars = sorted(self.vars)
    usedvars = sorted(usedvars)
    if vars == usedvars:
      return False, vars
    else:
      logging.info('Rewrote solve to %d vars not %d' % (len(usedvars),
                                                        len(self.vars)))
      return True, usedvars

  def GetVariableValue(self, var, solution):
    """Get the value of a variable for a given solution.
    
    :param var: string variable name
    :param solution: list of [1, 0]
    :rtype: Boolean value of the variable
    """
    ind = self.vars.index(var)
    return solution[ind]

  def IsClauseSat(self, clause, solution):
    """Given a clause and solution, determine if it is satisfied."""
    relnum = clause.problemnumber
    i = 0
    for var in clause.vars:
      val = self.GetVariableValue(var, solution)
      relnum = relation.reduce(relnum, 3, i, val)
      if relnum == 0:
        return False
      if relnum == 255:
        return True
      i += 1

  def PercentSat(self, solution):
    """Calculate the number of solved clauses for a given solution."""
    totalsat = 0
    for clause in self.clauses:
      if self.IsClauseSat(clause, solution):
        totalsat += 1
    return (totalsat, len(self.clauses))

  def PySolve(self):
    best = None
    for solution in itertools.product(range(2), repeat=len(self.vars)):
      sat, tot = self.PercentSat(solution)
      perc = float(sat)/float(tot)

      # if we satisfy everything, don't keep looping
      if sat == tot:
        return (perc, sat, solution)
      elif best is None:
        best = (perc, sat, solution)
      elif perc > best[0]: 
        best = (perc, sat, solution)

    return best

  def SetProfit(self, satisfied, forprovide=False):
    numclauses = float(len(self.clauses))
    solperc = float(satisfied) / numclauses
    self.profit = solperc - self.price
    if not forprovide:
      logging.info('Solved %d out of %d (%d vars) '
                   '| %0.3f - %0.3f = %0.3f profit'
                    % (satisfied, numclauses, len(self.vars), solperc,
                      self.price, self.profit))
      if self.profit < 0:
        logging.error('Lost money on %s' % str(self))
    return self.profit

  def CreateSolution(self, values, variables=None):
    if not variables:
      variables = self.vars
    logging.debug('Solution values are: %s' % str(values))
    s = csptree.CSPTree.CreateSolution(variables, values)
    return str(s)

  def GetTrivialSolution(self, forprovide=False):
    if not self.TrivialSolve():
      return
    fsat, values = self.TrivialSolve()
    self.SetProfit(fsat, forprovide=forprovide)
    return self.CreateSolution(values)

  def GetPySolution(self, forprovide=False):
    _, fsat, values = self.PySolve()
    self.SetProfit(fsat, forprovide=forprovide)
    return self.CreateSolution(values)

  def GetCSolution(self, forprovide=False):
    fsat, values = self.CSolve()
    self.SetProfit(fsat, forprovide=forprovide)
    return self.CreateSolution(values)

  def GetProxySolution(self):
    return proxysolver.ProxySolve(self)

  def DoSolve(self, forprovide=False):
    logging.debug('Solving offer %d relation %s cost %0.3f'
                 % (self.challengeid, self.problemnumbers, self.price))
    s = ''
    if self.TrivialSolve():
      s = self.GetTrivialSolution(forprovide=forprovide)
    elif FLAGS.solver == 'c':
      s = self.GetCSolution(forprovide=forprovide)
    elif FLAGS.solver == 'proxy':
      s = self.GetProxySolution(forprovide=forprovide)
    else:
      s = self.GetPySolution()
    return s

  def Solve(self, forprovide=False):
    if self.solution is None:
      rs = self.DoSolve(forprovide=forprovide)
      self.solution = rs
    return 'solve[[ %s ] %d]' % (str(self.solution), self.challengeid)

  @classmethod
  def GenerateFromAccepted(cls, acceptedproblem):
    return cls.Generate(acceptedproblem.problemnumbers,
                        acceptedproblem.offerid,
                        acceptedproblem.kind)

  @classmethod
  def GetVarsGenerator(cls, problemnumbers, perceived_vars=23):
    ptv = False
    if len(problemnumbers) == 1:
      ptv = Problem.GetBestPriceAndType(problemnumbers[0])
    if ptv:
      numvars = ptv[2]
      if ptv[1] == 'permutations':
        generator = itertools.permutations
      else:
        generator = itertools.combinations
    else:
      generator = itertools.combinations
      numvars = 23
      #XXX: This is a hack to always generate the appearance of 23 vars, even
      #     when we only use 13.
    if len(problemnumbers) > 1:
      generator = itertools.combinations
      numvars = 19
      # XXX: This assumes 2 different relation numbers
    return (numvars, generator, ['v%d' % x for x in range(perceived_vars)])

  @classmethod
  def Generate(cls, problemnumbers, offerid, kind, degree=None):
    numvars, clausegenerator, varslist = cls.GetVarsGenerator(problemnumbers)
    p = cls(0, varslist, [], offerid, 0, problemnumbers, 0, kind)
    max = len(problemnumbers) > 1 and 2 or 1
    for n in range(max):
      for i, j, k in clausegenerator(range(numvars), 3):
        p.AddClause(Clause(problemnumbers[n],
                           list_of_vars=['v%d' % x for x in [i, j, k]]))
    return p

  @classmethod
  def GetProblemList(cls, parsedlist):
    outputlist = []
    for problem in parsedlist:
      # buyer, list_of_vars, clauselist, challengeid,
      # seller, problemnumner, price
      newp = cls(problem[0], problem[1][0], problem[1][1], problem[2],
                 problem[3], problem[4], problem[5])
      outputlist.append(newp)
    return outputlist

  @staticmethod
  def GetPriceAndType(problemnumber):
    output = []
    for x in constants.PRICES:
      if problemnumber in constants.PRICES[x]:
        output.append((constants.PRICES[x][problemnumber],
                       'permutations', x))
    for x in constants.PRICES_COMB:
      if problemnumber in constants.PRICES_COMB[x]:
        output.append((constants.PRICES_COMB[x][problemnumber],
                       'combinations', x))
    output.sort()
    return output

  @staticmethod
  def GetBestPriceAndType(problemnumber):
    l = Problem.GetPriceAndType(problemnumber)
    if l:
      return l[0]
