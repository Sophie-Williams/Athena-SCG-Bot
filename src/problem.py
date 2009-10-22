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
import relation.gen

gflags.DEFINE_enum('solver', 'c', ['proxy', 'python', 'c'],
                   'Problem solver to use')
gflags.DEFINE_integer('problemdegree', 13, 'Degree of generated problems')
gflags.DEFINE_string('showproxysolution', False,
                     'Show proxy solution and real solution messages')

FLAGS = gflags.FLAGS

class Clause(object):
  """Represents a CSP problem clause."""

  def __init__(self, number, list_of_vars):
    """Initialize a clause.

    :param number: (int) problem (relation) number
    :param list_of_vars: (list) list of clause variables like v1, v2, v3
    """
    self.problemnumber = int(number)
    self.vars = list_of_vars

  def __eq__(self, other):
    try:
      return (self.vars == other.vars
              and self.problemnumber == other.problemnumber)
    except:
      return False

  def __str__(self):
    """Get a human readable string representing this clause."""
    return 'Clause(num=%d, vars=%s)' % (self.problemnumber, str(self.vars))

  def __repr__(self):
    """Get a human readable representation of this clause."""
    return str(self)

  def GetTuple(self):
    """Get a Tuple representing this clause.

    Returns:
      A tuple in the form (problemnumber, var1, var2, var3, ...)
    """
    return tuple([self.problemnumber]+self.vars)

  def GetProvideBlob(self):
    """Get a provide[] blob representing this clause.

    Used by the Problem class to generate problem provide messages.
    Returns:
      A string in the form "(problemnumber var1 var2 var3 ...)"
    """
    return '(%d %s )' % (self.problemnumber, ' '.join(self.vars))

class Problem(object):
  """Describes a problem instance."""
  def __init__(self, buyer, list_of_vars, clauselist, challengeid, seller,
               problemnumber, price, kind='all'):
    self.buyer = int(buyer)
    self.vars = list(list_of_vars)
    self.clauses = []
    self.AddClauses(clauselist)
    self.challengeid = int(challengeid)
    self.seller = int(seller)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.solution = None
    self.profit = 0
    self.kind = kind

  def __str__(self):
    return ('Problem(num=%d, vars=%s, price=%s, seller=%s clauses=%d)'
            % (self.problemnumber, str(self.vars), self.price, self.seller,
               len(self.clauses)))

  def __repr__(self):
    return str(self)

  def AddClauses(self, clauselist):
    """Add Clauses to this Problem instance.

    :param clauselist: list of iterables where i[0] is the problemnumber
    """
    for clause in clauselist:
      self.AddClause(Clause(clause[0], clause[1:]))

  def AddClause(self, clause):
    """Add a Clause to this Problem instance.

    :param clause: A single clause object
    """
    self.clauses.append(clause)

  def GetProvide(self):
    """Get a 'provide' blob to send to the administrator for this problem."""
    if self.kind == 'secret':
      self.Solve()
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
    return ('provided[%d %s %s %d %d (%d ) %0.8f]'
            % (self.buyer, ' '.join(self.vars),
               ' '.join([x.GetProvideBlob() for x in self.clauses]),
               self.challengeid, self.seller, self.problemnumber, self.price))

  def RealSolve(self):
    """Solve this Problem instance using the C solver."""
    if self.problemnumber == 0:
      logging.info('Special Case Solve: Relation 0!')
      values = [0]*len(self.vars)
      fsat = len(self.clauses)
    elif self.problemnumber % 2:
      logging.info('Special Case Solve: All False!')
      values = [0]*len(self.vars)
      fsat = len(self.clauses)
    elif self.problemnumber >= 128:
      logging.info('Special Case Solve: All True!')
      values = [1]*len(self.vars)
      fsat = len(self.clauses)
    else:
      filtered, vars = self.FilterSolve()
      c_p = relation.Problem(tuple(vars),
                            [x.GetTuple() for x in self.clauses])
      fsat, values = c_p.solve()
      if filtered:
        values = self.TransposeValues(vars, values)
    return fsat, values

  def TransposeValues(self, vars, values):
    output = [0]*len(self.vars)
    self.vars.sort()
    for x in self.vars:
      if x in vars:
        i = vars.index(x)
        output[i] = values[i]
    return output

  def FilterSolve(self):
    vars = list(self.vars)
    vars.sort()
    usedvars = []
    for x in self.clauses:
      for var in x.vars:
        if var not in usedvars:
          usedvars.append(var)
    usedvars.sort()
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

  def SetProfit(self, satisfied):
    numclauses = float(len(self.clauses))
    solperc = float(satisfied)/numclauses
    self.profit = solperc-self.price
    logging.info('Solved %d out of %d | %0.3f - %0.3f = %0.3f profit'
                  % (satisfied, numclauses, solperc, self.price, self.profit))
    if self.profit < 0:
      logging.error('Lost money on %s' % str(self))
    return self.profit

  def GetPySolution(self):
    perc, sat, solution = self.PySolve()
    self.SetProfit(sat)
    logging.debug('Values are: %s' % str(solution))
    s = csptree.CSPTree.CreateSolution(self.vars, solution)
    return str(s)

  def GetCSolution(self):
    fsat, values = self.RealSolve()
    self.SetProfit(fsat)
    logging.debug('Values are: %s' % str(values))
    s = csptree.CSPTree.CreateSolution(self.vars, values)
    return str(s)

  def GetProxySolution(self):
    return proxysolver.ProxySolve(self)

  def DoSolve(self):
    logging.debug('Solving offer %d relation %d cost %0.3f'
                 % (self.challengeid, self.problemnumber, self.price))
    s = ''
    if FLAGS.solver == 'c':
      s = self.GetCSolution()
    elif FLAGS.solver == 'proxy':
      s = self.GetProxySolution()
    else:
      s = self.GetPySolution()
    return s

  def Solve(self):
    if self.solution is None:
      rs = self.DoSolve()
      if FLAGS.showproxysolution:
        logging.debug('Real solution: %s' % rs)
        ps = proxysolver.ProxySolve(self)
        logging.debug('Proxy solution: %s' % ps)
      self.solution = rs
    return 'solve[[ %s ] %d]' % (str(self.solution), self.challengeid)

  def GetSolveThread(self):
    logging.debug('Creating solve thread for %s' % str(self))
    t = threading.Thread(group=None, target=self.Solve)
    return t

  @classmethod
  def GenerateFromAccepted(cls, acceptedproblem):
    return cls.Generate(acceptedproblem.problemnumber,
                        acceptedproblem.offerid,
                        acceptedproblem.kind)


  @classmethod
  def Generate(cls, problemnumber, offerid, kind, degree=None):
    if not degree:
      degree = FLAGS.problemdegree
    p = cls(0, ['v%d' % x for x in range(13)], [], offerid, 0,
            problemnumber, 0, kind)

    ptv = Problem.GetBestPriceAndType(problemnumber)
    if ptv:
      numvars = ptv[2]
      if ptv[1] == 'permutations':
        generator = itertools.permutations
      else:
        generator = itertools.combinations
    else:
      generator = itertools.combinations
      numvars = 23

    for i, j, k in generator(range(numvars), 3):
      p.AddClause(Clause(problemnumber, ['v%d' % x for x in [i, j, k]]))
    return p

  @classmethod
  def GetProblemList(cls, parsedlist):
    outputlist = []
    for problem in parsedlist:
      #buyer, list_of_vars, clauselist, challengeid, seller,
      # problemnumner, price
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
