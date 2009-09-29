#!/usr/bin/env python
import logging

import relation
import relation.gen

import csptree

class Clause(object):
  def __init__(self, number, list_of_vars):
    self.problemnumber = int(number)
    self.vars = list_of_vars

  def __str__(self):
    return 'Clause(num=%d, vars=%s)' % (self.problemnumber, str(self.vars))

  def __repr__(self):
    return str(self)

  def GetTuple(self):
    return tuple([self.problemnumber]+self.vars)

  def GetProvideBlob(self):
    return '(%d %s )' % (self.problemnumber, ' '.join(self.vars))

class Problem(object):
  def __init__(self, buyer, list_of_vars, clauselist, challengeid, seller,
               problemnumber, price):
    self.buyer = int(buyer)
    self.vars = list_of_vars
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

  def Solve(self):
    logging.info('Solving offer %d relation %d' % (self.challengeid,
                                                   self.problemnumber))
    if self.problemnumber%2:
      logging.info('Special Case Solve: All False!')
      values = [0]*len(self.vars)
    elif self.problemnumber >= 128:
      logging.info('Special Case Solve: All True!')
      values = [1]*len(self.vars)
    else:
      c_p = relation.Problem(tuple(self.vars),
                            [x.GetTuple() for x in self.clauses])
      fsat, values = c_p.solve()
      logging.info('Solved %d out of %d clauses' % (fsat, len(self.clauses)))
      logging.info('Values are: %s' % str(values))

    s = csptree.csptree.CreateSolution(self.vars, values)
    return 'solve[[ %s ] %d]' % (str(s), self.challengeid)

  @classmethod
  def GenerateProblem(cls, problemnumber, degree, offerid):
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

