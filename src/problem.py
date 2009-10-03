#!/usr/bin/env python
import gflags
import itertools
import logging
import random

import csptree
import proxysolver
import relation
import relation.gen

gflags.DEFINE_enum('solver', 'python', ['proxy', 'python', 'c'],
                   'Problem solver to use')
gflags.DEFINE_integer('problemdegree', 12, 'Degree of generated problems')
gflags.DEFINE_string('showproxysolution', False,
                     'Show proxy solution and real solution messages')

FLAGS = gflags.FLAGS

class Clause(object):
  """Represents a CSP problem clause."""

  def __init__(self, number, list_of_vars):
    """Initialize a clause.

    Args:
       number: (int) problem (relation) number
       list_of_vars: (list) list of clause variables like v1, v2, v3
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
  def __init__(self, buyer, list_of_vars, clauselist, challengeid, seller,
               problemnumber, price):
    self.buyer = int(buyer)
    self.vars = list(list_of_vars)
    self.clauses = []
    self.AddClauses(clauselist)
    self.challengeid = int(challengeid)
    self.seller = int(seller)
    self.problemnumber = int(problemnumber)
    self.price = float(price)

  def __str__(self):
    return ('Problem(num=%d, vars=%s, price=%s, seller=%s clauses=%s)'
            % (self.problemnumber, str(self.vars), self.price, self.seller,
               str(self.clauses)))

  def __repr__(self):
    return str(self)

  def AddClauses(self, clauselist):
    for clause in clauselist:
      self.AddClause(Clause(clause[0], clause[1:]))

  def AddClause(self, clause):
    self.clauses.append(clause)

  def GetProvide(self):
    return ('provide[%s %s %d]'
            % (' '.join(self.vars),
               ' '.join([x.GetProvideBlob() for x in self.clauses]),
               self.challengeid))

  def GetProvided(self):
    return ('provided[%d %s %s %d %d (%d ) %0.8f]'
            % (self.buyer, ' '.join(self.vars),
               ' '.join([x.GetProvideBlob() for x in self.clauses]),
               self.challengeid, self.seller, self.problemnumber, self.price))

  def RealSolve(self):
    if self.problemnumber%2:
      logging.info('Special Case Solve: All False!')
      values = [0]*len(self.vars)
      fsat = len(self.clauses)
    elif self.problemnumber >= 128:
      logging.info('Special Case Solve: All True!')
      values = [1]*len(self.vars)
      fsat = len(self.clauses)
    else:
      c_p = relation.Problem(tuple(self.vars),
                            [x.GetTuple() for x in self.clauses])
      fsat, values = c_p.solve()
      fsat = 0
      for x in self.clauses:
        if self.IsClauseSat(x, values):
          fsat += 1
        
    return fsat, values

  def GetVariableValue(self, var, solution):
    ind = self.vars.index(var)
    return solution[ind]

  def IsClauseSat(self, clause, solution):
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

  def GetPySolution(self):
    perc, sat, solution = self.PySolve()
    profit = perc-self.price
    logging.info('Solved %d %0.3f%% (%0.3f - %0.3f = %0.3f profit)'
                  % (sat, perc*100, perc, self.price, profit))
    logging.info('Using solution: %s' % str(solution))
    if profit < 0:
      logging.error('Lost money on %s' % str(self))
    s = csptree.csptree.CreateSolution(self.vars, solution)
    return 'solve[[ %s ] %d]' % (str(s), self.challengeid)

  def GetCSolution(self):
    fsat, values = self.RealSolve()
    numclauses = float(len(self.clauses))
    solperc = float(fsat)/numclauses
    profit = solperc-self.price
    logging.info('Solved %d out of %d | %0.3f - %0.3f = %0.3f profit'
                  % (fsat, numclauses, solperc, self.price, profit))
    if profit < 0:
      logging.error('Lost money on %s' % str(self))
    logging.debug('Values are: %s' % str(values))

    s = csptree.csptree.CreateSolution(self.vars, values)
    return 'solve[[ %s ] %d]' % (str(s), self.challengeid)

  def GetProxySolution(self):
    return proxysolver.ProxySolve(self)

  def DoSolve(self):
    logging.debug('Solving offer %d relation %d cost %0.3f'
                 % (self.challengeid, self.problemnumber, self.price))
    if FLAGS.solver == 'c':
      return self.GetCSolution()
    elif FLAGS.solver == 'proxy':
      return self.GetProxySolution()
    else:
      return self.GetPySolution()

  def Solve(self):
    rs = self.DoSolve()
    if FLAGS.showproxysolution:
      logging.debug('Real solution: %s' % rs)
      ps = proxysolver.ProxySolve(self)
      logging.debug('Proxy solution: %s' % ps)
    return rs

  @classmethod
  def Generate(cls, problemnumber, offerid, degree=None):
    if not degree:
      degree = FLAGS.problemdegree
    p = cls(0, ['v%d' % x for x in range(0, degree)], [], offerid, 0,
            problemnumber, 0)
    for i, j, k in relation.gen.permute3(degree):
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

