#!/usr/bin/python
"""Parser for SCG in CS4500.

Fuck this shit.
"""
__author__ = "Will Nowak <wan@ccs.neu.edu>"

from pyparsing import *

gamekind = QuotedString(quoteChar='"')
integer = Word( nums )
PlayerID = integer
flo = Combine(Word( nums ) + "." + Word( nums ))


#Problem =  <vars> List(Var) *s <clauses> List(Clause).
#Clause = "(" <relnum> int *s <vars> List(Var) ")".
ProblemType = ("(" + ZeroOrMore(integer("pt")) + ")")

OfferedChallenge = Suppress(Literal("offered["))
AcceptedChallenge = (Suppress("accepted[") + PlayerID("challengee"))
ProvidedChallenge = (Suppress("provided[") + PlayerID("challengee"))
SolvedChallenge = (Suppress("solved[") + PlayerID("challengee")
                   + Problem("instance"))

Challenge = (Or([OfferedChallenge, AcceptedChallenge, ProvidedChallenge,
                 SolvedChallenge]) + integer("key") + PlayerID("challenger")
             + ProblemType("pred") + flo("price") + "]")

Objective = Literal("[]")
Predicate = Literal("[]")
ChallengeList = Suppress("(") + ZeroOrMore(Challenge) + Suppress(")")
ourOffered = ChallengeList
theirOffered = ChallengeList
accepted = ChallengeList
provided = ChallengeList

OfferTrans = ( L + Suppress("offer[") + ProblemType("pred") + flo("price") )
AcceptTrans = ( L + Suppress("accept[") + integer("challengeid") )
ProvideTrans = ( L + Suppress("provide[") + integer("challengeid") )
SolveTrans = ( L + Suppress("solve[") + integer("challengeid") )
ReofferTrans = ( L + Suppress("reoffer[") + integer("challengeid")
                + flo("price") ) 

Transaction = Or([OfferTrans, AcceptTrans, ReofferTrans, ProvideTrans,
                  SolveTrans]) + Suppress("]")

L = Suppress(LineEnd())
Config = (Suppress(Literal("config[")) + L
          + Suppress(Literal("gamekind:")) + gamekind('gamekind') + L
          + Suppress(Literal("turnduration:")) + integer('turnduration') + L
          + Suppress(Literal("mindecrement:")) + flo('mindecrement') + L
          + Suppress(Literal("initacc:")) + flo('initacc') + L
          + Suppress(Literal("objective:")) + Objective('objective') + L
          + Suppress(Literal("predicate:")) + Predicate('predicate') + L
          + Suppress(Literal("numrounds:")) + integer('numrounds') + L
          + Suppress(Literal("profitfactor:")) + flo('profitfactor') + L
          + Suppress(Literal("otrounds:")) + integer('otrounds')
          + Suppress(Literal("]"))
         )

PlayerContext = (Suppress(Literal("context[")) + L
                 + Config("config") + L
                 + PlayerID("playerid") + L
                 + flo("balance") + L
                 + integer("currentround") + L
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
