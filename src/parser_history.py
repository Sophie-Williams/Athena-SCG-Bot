
__author__ = "Alex Lee <lee@ccs.neu.edu"

from pyparsing import *
from parser import String, Double, sup_lit, wrap


TransactionType = Or([Literal("BUY"), Literal("CREATE"), Literal("REOFFER"),
                      Literal("DELIVER"), Literal("FINISH")])

Price = Double 
Quality = Double

Predicate = sup_lit("pred") + String

RawMaterialInstance = String
RawMaterial = sup_lit("rm") + wrap(Predicate + RawMaterialInstance)

Outcome = String

FinishedProduct = sup_lit("finish") + wrap(RawMaterial + Outcome + Quality)

Challenge = sup_lit("challenge") + wrap(String + Price +
            Predicate + Optional(RawMaterial) + Optional(FinishedProduct))
Transaction = TransactionType + Challenge

PlayerTransaction = String + ZeroOrMore(Transaction)

Round = sup_lit("round") + wrap(ZeroOrMore(PlayerTransaction("pt")))

History = sup_lit("history") + wrap(ZeroOrMore(Round))
