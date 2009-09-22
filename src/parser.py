#!/usr/bin/python
"""Parser for SCG in CS4500.

Fuck this shit.
"""
__author__ = "Will Nowak <wan@ccs.neu.edu>"

from pyparsing import *

def wrap(expr, open_char="[", close_char="]"):
  """
  >>> w = wrap(Word(nums))
  >>> w.searchString('[123123]')[0][0]
  '123123'
  >>> len(w.searchString('[1231 123123]')) == 0
  True
  """
  return Suppress(Literal(open_char)) + expr + Suppress(Literal(close_char))


def sup_lit(expr):
  """A Suppressed Literal"""
  return Suppress(Literal(expr))


def KeyAndValue(key, value_type, sep=':'):
  """
  >>> kv = KeyAndValue('number', Word(nums))
  >>> kv.searchString('number:10') is not None
  True
  >>> kv.searchString('number: 10') is not None
  True
  """
  return sup_lit('%s' % key) + sup_lit(':') + value_type(key)


class QString(str):

  def __init__(self, s):
    self.s = s

  def __str__(self):
    return '"%s"' % self.s

  @classmethod
  def parse_action(cls, tokens):
    tokens[0] = cls(tokens[0])


def convert_to_int(tokens):
  tokens[0] = int(tokens[0])


# Some basic values
String = QuotedString(quoteChar='"')
# String.setParseAction(QString.parse_action)
Double = Combine(Word(nums) + "." + Word(nums))
Integer = Word(nums)
L = Suppress(LineEnd())


gamekind = String
PlayerID = Integer

Var = Word(alphanums)


#Clause = "(" <relnum> int *s <vars> List(Var) ")".
Clause = sup_lit('(') + Integer + ZeroOrMore(Var) + sup_lit(')')

#Problem =  <vars> List(Var) *s <clauses> List(Clause).
Problem = ZeroOrMore(Var) + sup_lit(' ') + ZeroOrMore(Clause)
ProblemType = ("(" + ZeroOrMore(Integer("pt")) + ")")

OfferedChallenge = sup_lit("offered[")
AcceptedChallenge = (Suppress("accepted[") + PlayerID("challengee"))
ProvidedChallenge = (Suppress("provided[") + PlayerID("challengee"))
SolvedChallenge = (Suppress("solved[") + PlayerID("challengee")
                   + Problem("instance"))

Challenge = (Or([OfferedChallenge, AcceptedChallenge, ProvidedChallenge,
                 SolvedChallenge]) + Integer("key") + PlayerID("challenger")
             + ProblemType("pred") + Double("price") + "]")

Objective = Literal("[]")
Predicate = Literal("[]")
ChallengeList = sup_lit("(") + ZeroOrMore(Challenge) + sup_lit(")")
ourOffered = ChallengeList
theirOffered = ChallengeList
accepted = ChallengeList
provided = ChallengeList

OfferTrans = Suppress("offer") + wrap(ProblemType("pred") + Double("price"))
AcceptTrans = Suppress("accept") + wrap(Integer("challengeid"))
ProvideTrans = Suppress("provide") + wrap(Integer("challengeid"))
SolveTrans = Suppress("solve") + wrap(Integer("challengeid"))
ReofferTrans = ( Suppress("reoffer") + wrap(Integer("challengeid")
                + Double("price")) )

Transaction = L + Or([OfferTrans, AcceptTrans, ReofferTrans, ProvideTrans,
                      SolveTrans])

Config = (sup_lit("config") + wrap(L
          + KeyAndValue("gamekind", gamekind) + L
          + KeyAndValue("turnduration", Integer) + L
          + KeyAndValue("mindecrement", Double) + L
          + KeyAndValue("initacc", Double) + L
          + KeyAndValue("objective", Objective) + L
          + KeyAndValue("predicate", Predicate) + L
          + KeyAndValue("numrounds", Integer) + L
          + KeyAndValue("profitfactor", Double) + L
          + KeyAndValue("otrounds", Integer)
         ))
def test_config():
  """
  >>> s = ('config[\\n' +
  ... '    gamekind: "CSP"\\n' +
  ... '    turnduration: 60\\n' +
  ... '    mindecrement: 0.01\\n' +
  ... '    initacc: 100.0\\n' +
  ... '    objective: []\\n' +
  ... '    predicate: []\\n' +
  ... '    numrounds: 10000\\n' +
  ... '    profitfactor: 1.0\\n' +
  ... '    otrounds: 2]')
  >>> cfg = Config.searchString(s)[0]
  >>> cfg['gamekind']
  'CSP'
  >>> cfg['turnduration']
  '60'
  >>> cfg['predicate']
  '[]'
  >>> cfg['otrounds']
  '2'
  """

PlayerContext = (sup_lit("context") + wrap(L
                 + Config("config") + L
                 + PlayerID("playerid") + L
                 + Double("balance") + L
                 + Integer("currentround") + L
                 + ourOffered("ourOffered") + L
                 + theirOffered("theirOffered") + L
                 + accepted("accepted") + L
                 + provided("provided") + L
                ))
def test_context():
  """
  >>> s = ('context[\\n' +
  ... '    config[\\n' +
  ... '\\n' +
  ... '    gamekind: "CSP"\\n' +
  ... '    turnduration: 60\\n' +
  ... '    mindecrement: 0.01\\n' +
  ... '    initacc: 100.0\\n' +
  ... '    objective: []\\n' +
  ... '    predicate: []\\n' +
  ... '    numrounds: 10000\\n' +
  ... '    profitfactor: 1.0\\n' +
  ... '    otrounds: 2]\\n' +
  ... '    101\\n' +
  ... '    100.0\\n' +
  ... '    1\\n' +
  ... '    ()\\n' +
  ... '    ()\\n' +
  ... '    ()\\n' +
  ... '    ()\\n' +
  ... ']\\n')
  >>> pctx = PlayerContext.searchString(s)
  >>> len(pctx[0]) > 0
  True
  """
ListOfTrans = ZeroOrMore(Transaction)

PlayerTrans = (sup_lit("playertrans") + wrap(L
               + PlayerID("playerid") + L
               + ListOfTrans("ts")))
