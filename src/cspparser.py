#!/usr/bin/python
"""Parser for SCG in CS4500.

Parses messages to be exchanged between the player and the admin.
"""
__author__ = "Will Nowak <wan@ccs.neu.edu>"

from pyparsing import (nums, alphanums, Word, Combine, Suppress, Literal,
                       ZeroOrMore, QuotedString, LineEnd, Or, Group, Forward)

def sup_lit(expr):
  """A Suppressed Literal"""
  return Suppress(Literal(expr))


def wrap(expr, open_char="[", close_char="]"):
  """
  >>> w = wrap(Word(nums))
  >>> w.searchString('[123123]')[0][0]
  '123123'
  >>> len(w.searchString('[1231 123123]')) == 0
  True
  """
  return sup_lit(open_char) + expr + sup_lit(close_char)


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


# Some basic values
String = QuotedString(quoteChar='"')
Integer = Word(nums)
Double = Combine(ZeroOrMore("-") + Word(nums) + "." + Word(nums)
                 +Group(ZeroOrMore('E'+ZeroOrMore('-')+Integer)))
L = Suppress(LineEnd())
List = ZeroOrMore

# Game syntax

# Primitive Objects
PlayerID = Integer
Var = Combine('v'+Integer)

Clause = Group(wrap(Integer("relation_number") + List(Var), "(", ")"))

Problem = Group(Group(List(Var)) + Group(List(Clause)))
ProblemType = wrap(List(Integer("pt")), "(", ")")


ChallengeCommon = ( Integer("key") + PlayerID("challenger") +
                    ProblemType("pred") + Double("price") )

OfferedChallenge = sup_lit("offered") + wrap(ChallengeCommon)
AcceptedChallenge = sup_lit("accepted") + wrap(PlayerID("challengee")
                  + ChallengeCommon)
ProvidedChallenge = (sup_lit("provided")
                     + Group(wrap(PlayerID("challengee") + Problem("instance")
                             + ChallengeCommon)))
SolvedChallenge = sup_lit("solved") + wrap(PlayerID("challengee")
                + Problem("instance") + ChallengeCommon)

Challenge = ( Or([OfferedChallenge, AcceptedChallenge, ProvidedChallenge,
                  SolvedChallenge]) )

Objective = Literal("[]")
Predicate = wrap(Integer("maxClauses"))
ChallengeList = wrap(List(Challenge), "(", ")")

TrueFalse = Or([Literal('true'), Literal('false')])
TreeColor = Or([Literal('red'), Literal('black')])
TreeNode = Forward()
TreeNode << wrap(sup_lit("node") + TreeColor +
                  Group(wrap(Var + sup_lit('->') + TrueFalse, '(',')'))
                 + ZeroOrMore(TreeNode) + ZeroOrMore(TreeNode), '(', ')')
# Describes the five transactions
OfferTrans = Suppress("offer") + wrap(Integer("challengeid") + ProblemType("pred") + Double("price"))
AcceptTrans = Suppress("accept") + wrap(Integer("challengeid"))
ProvideTrans = Suppress("provide") + wrap(Integer("challengeid"))
SolveTrans = Suppress("solve") + wrap(Integer("challengeid"))
ReofferTrans = ( Suppress("reoffer") + wrap(Integer("challengeid")
                + Double("price")) )

Transaction = Or([OfferTrans, AcceptTrans, ReofferTrans, ProvideTrans,
                  SolveTrans])
ListOfTransactions = List(L + Transaction)
PlayerTransaction = ( sup_lit("playertrans") + wrap(L
                    + PlayerID("playerid") + L
                    + ListOfTransactions("ts")) )

# Describes the config syntax
Config = (sup_lit("config") + wrap(L
          + KeyAndValue("gamekind", String) + L
          + KeyAndValue("turnduration", Integer) + L
          + KeyAndValue("mindecrement", Double) + L
          + KeyAndValue("initacc", Double) + L
          + KeyAndValue("maxOffers", Integer) + L
          + KeyAndValue("objective", Objective) + L
          + KeyAndValue("predicate", Predicate) + L
          + KeyAndValue("numrounds", Integer) + L
          + KeyAndValue("profitfactor", Double) + L
          + KeyAndValue("otrounds", Integer)
         ))

# Describes the player context syntax (sent by the admin)
PlayerContext = (sup_lit("context") + wrap(L
                 + Config("config") + L
                 + PlayerID("playerid") + L
                 + Double("balance") + L
                 + Integer("currentround") + L
                 + ChallengeList("our_offered") + L
                 + ChallengeList("their_offered") + L
                 + ChallengeList("accepted") + L
                 + ChallengeList("provided") + L
                ))
