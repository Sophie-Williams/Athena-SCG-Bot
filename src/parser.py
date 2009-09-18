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

Objective = Literal("[]")
Predicate = Literal("[]")
ourOffered = Literal("()")
theirOffered = Literal("()")
accepted = Literal("()")
provided = Literal("()")

OfferTrans = AcceptTrans = ReofferTrans = ProvideTrans = SolveTrans = Literal('&')

Transaction = Or([OfferTrans,AcceptTrans,ReofferTrans,ProvideTrans,SolveTrans])

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

PlayerContext = (Literal("context[") + L
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

PlayerTrans = (Literal("playertrans[") + L
               + PlayerID("playerid") + L
               + ListOfTrans("ts"))

c = open('/net/tmp/context.txt').read()
print PlayerContext.searchString(c)
