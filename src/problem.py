#!/usr/bin/env python
import logging
import random

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
    return fsat, values

  def Solve(self):
    logging.debug('Solving offer %d relation %d cost %0.3f'
                 % (self.challengeid, self.problemnumber, self.price))
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

  @classmethod
  def GenerateReasonablePrice(cls, problemnumber):
    logging.debug('Generating problem for price determination')
    p = cls.Generate(problemnumber, -1, 5)
    logging.debug('Solving problem')
    solved = p.RealSolve()[0]
    numclauses = float(len(p.clauses))
    logging.debug('Solved %d clauses of %d' % (solved, numclauses))
    return float(solved) / numclauses

  @staticmethod
  def GetReasonablePrice(problemnumber):
    lookup = {
      0:0.000000000000000000,
      2:0.599999999999999978,
      4:0.400000000000000022,
      6:0.599999999999999978,
      8:0.599999999999999978,
      10:0.900000000000000022,
      12:0.599999999999999978,
      14:0.900000000000000022,
      16:0.599999999999999978,
      18:0.599999999999999978,
      20:0.599999999999999978,
      22:0.599999999999999978,
      24:0.599999999999999978,
      26:0.900000000000000022,
      28:0.699999999999999956,
      30:0.900000000000000022,
      32:0.400000000000000022,
      34:0.599999999999999978,
      36:0.500000000000000000,
      38:0.699999999999999956,
      40:0.599999999999999978,
      42:0.900000000000000022,
      44:0.699999999999999956,
      46:0.900000000000000022,
      48:0.599999999999999978,
      50:0.900000000000000022,
      52:0.699999999999999956,
      54:0.900000000000000022,
      56:0.699999999999999956,
      58:0.900000000000000022,
      60:0.699999999999999956,
      62:0.900000000000000022,
      64:0.599999999999999978,
      66:0.599999999999999978,
      68:0.599999999999999978,
      70:0.699999999999999956,
      72:0.599999999999999978,
      74:0.900000000000000022,
      76:0.900000000000000022,
      78:0.900000000000000022,
      80:0.900000000000000022,
      82:0.900000000000000022,
      84:0.900000000000000022,
      86:0.900000000000000022,
      88:0.900000000000000022,
      90:0.900000000000000022,
      92:0.900000000000000022,
      94:0.900000000000000022,
      96:0.599999999999999978,
      98:0.699999999999999956,
      100:0.699999999999999956,
      102:0.699999999999999956,
      104:0.599999999999999978,
      106:0.900000000000000022,
      108:0.900000000000000022,
      110:0.900000000000000022,
      112:0.900000000000000022,
      114:0.900000000000000022,
      116:0.900000000000000022,
      118:0.900000000000000022,
      120:0.900000000000000022,
      122:0.900000000000000022,
      124:0.900000000000000022,
      126:0.900000000000000022 }
    if problemnumber%2 or problemnumber >= 128:
      return 1.0
    else:
      return lookup[problemnumber]

  @classmethod
  def Generate(cls, problemnumber, offerid, degree=None):
    if not degree:
      degree = random.randint(10,20)
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

