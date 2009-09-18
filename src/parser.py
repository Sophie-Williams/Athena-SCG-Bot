#!/usr/bin/python
"""Parser for SCG in CS4500.

Fuck this shit.
"""
__author__ = "Will Nowak <wan@ccs.neu.edu>"

from pyparsing import *

def wrap(expr, open_char="[", close_char="]"):
  return Suppress(Literal(open_char)) + expr + Suppress(Literal(close_char))


def sup_lit(expr):
  """A Suppressed Literal"""
  return Suppress(Literal(expr))

# Some basic values
String = QuotedString(quoteChar='"')
Double = Combine(Word(nums) + "." + Word(nums))
Integer = Word(nums)
L = sup_lit('\n')


gamekind = String
PlayerID = Integer
Double = Combine(Word( nums ) + "." + Word( nums ))

Var = Word(alphanums)


#Clause = "(" <relnum> int *s <vars> List(Var) ")".
Clause = sup_lit('(') + Integer + ZeroOrMore(Var) + sup_lit(')')

#Problem =  <vars> List(Var) *s <clauses> List(Clause).
Problem = ZeroOrMore(Var) + sup_lit(' ') + ZeroOrMore(Clause)
ProblemType = ("(" + ZeroOrMore(Integer("pt")) + ")")

OfferedChallenge = Suppress(Literal("offered["))
AcceptedChallenge = (Suppress("accepted[") + PlayerID("challengee"))
ProvidedChallenge = (Suppress("provided[") + PlayerID("challengee"))
SolvedChallenge = (Suppress("solved[") + PlayerID("challengee")
                   + Problem("instance"))

Challenge = (Or([OfferedChallenge, AcceptedChallenge, ProvidedChallenge,
                 SolvedChallenge]) + Integer("key") + PlayerID("challenger")
             + ProblemType("pred") + Double("price") + "]")

Objective = Literal("[]")
Predicate = Literal("[]")
ChallengeList = Suppress("(") + ZeroOrMore(Challenge) + Suppress(")")
ourOffered = ChallengeList
theirOffered = ChallengeList
accepted = ChallengeList
provided = ChallengeList

OfferTrans = ( L + Suppress("offer[") + ProblemType("pred") + Double("price") )
AcceptTrans = ( L + Suppress("accept[") + Integer("challengeid") )
ProvideTrans = ( L + Suppress("provide[") + Integer("challengeid") )
SolveTrans = ( L + Suppress("solve[") + Integer("challengeid") )
ReofferTrans = ( L + Suppress("reoffer[") + Integer("challengeid")
                + Double("price") ) 

Transaction = Or([OfferTrans, AcceptTrans, ReofferTrans, ProvideTrans,
                  SolveTrans]) + Suppress("]")

L = Suppress(LineEnd())
Config = (Suppress(Literal("config[")) + L
          + Suppress(Literal("gamekind:")) + gamekind('gamekind') + L
          + Suppress(Literal("turnduration:")) + Integer('turnduration') + L
          + Suppress(Literal("mindecrement:")) + Double('mindecrement') + L
          + Suppress(Literal("initacc:")) + Double('initacc') + L
          + Suppress(Literal("objective:")) + Objective('objective') + L
          + Suppress(Literal("predicate:")) + Predicate('predicate') + L
          + Suppress(Literal("numrounds:")) + Integer('numrounds') + L
          + Suppress(Literal("profitfactor:")) + Double('profitfactor') + L
          + Suppress(Literal("otrounds:")) + Integer('otrounds')
          + Suppress(Literal("]"))
         )

PlayerContext = (Suppress(Literal("context[")) + L
                 + Config("config") + L
                 + PlayerID("playerid") + L
                 + Double("balance") + L
                 + Integer("currentround") + L
                 + ourOffered("ourOffered") + L
                 + theirOffered("ourOffered") + L
                 + accepted("ourOffered") + L
                 + provided("ourOffered") + L
                + Suppress(Literal("]"))
                 )
ListOfTrans = ZeroOrMore(Transaction)

PlayerTrans = (Suppress(Literal("playertrans[")) + L
               + PlayerID("playerid") + L
               + ListOfTrans("ts"))
