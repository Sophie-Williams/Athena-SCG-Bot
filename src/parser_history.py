
__author__ = "Alex Lee <lee@ccs.neu.edu"

from pyparsing import *


def wrap(expr, open_char="[", close_char="]"):
  return Suppress(Literal(open_char)) + expr + Suppress(Literal(close_char))

def sup_lit(expr):
  return Suppress(Literal(expr))

String = QuotedString(quoteChar='"')
flo = Combine(Word( nums ) + "." + Word( nums ))

TransactionType = Or([Literal("BUY"), Literal("CREATE"), Literal("REOFFER"),
                      Literal("DELIVER"), Literal("FINISH")])

Price = flo 
Quality = flo

Predicate = sup_lit("pred") + String

RawMaterialInstance = String
RawMaterial = sup_lit("rm") + wrap(Predicate + RawMaterialInstance)

Outcome = String

FinishedProduct = sup_lit("finish") + wrap(RawMaterial + Outcome + Quality)

Challenge = sup_lit("challenge") + wrap(String + Price +
            Predicate + Optional(RawMaterial) + Optional(FinishedProduct))
Transaction = TransactionType + Challenge

PlayerTransaction = String + ZeroOrMore(Transaction)

Round = sup_lit("round") + wrap(ZeroOrMore(PlayerTransaction))

History = sup_lit("history") + wrap(ZeroOrMore(Round))
