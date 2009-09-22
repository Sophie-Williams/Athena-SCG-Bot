
__author__ = "Alex Lee <lee@ccs.neu.edu"

from pyparsing import *
from parser import String, Double, sup_lit, wrap


TransactionType = Or([Literal("BUY"), Literal("CREATE"), Literal("REOFFER"),
                      Literal("DELIVER"), Literal("FINISH")])


class QString(str):

  def __init__(self, s):
    self.s = s

  def __str__(self):
    return '"%s"' % self.s

  @classmethod
  def parse_action(cls, tokens):
    tokens[0] = cls(tokens[0])
String.setParseAction(QString.parse_action)


def tokens_to_float(tokens):
  tokens[0] = float(tokens[0])
Double.setParseAction(tokens_to_float)


class O_Challenge(object):
  def __init__(self, s, price, pred, raw_m=None, f_p=None):
    self.s = s
    self.price = price
    self.pred = pred
    self.raw_m = raw_m
    self.f_p = f_p


def xyz(tokens):
  c = O_Challenge(*tokens)
  for x in range(1, len(tokens)):
    del tokens[x]
  tokens[0] = c

Price = Double("price")
Quality = Double("quality")


Predicate = sup_lit("pred") + String("predicate")

RawMaterialInstance = String
RawMaterial = sup_lit("rm") + wrap(Predicate + RawMaterialInstance)

Outcome = String

FinishedProduct = sup_lit("finish") + wrap(RawMaterial + Outcome + Quality)

Challenge = sup_lit("challenge") + wrap(String + Price +
            Predicate + Optional(RawMaterial)("raw_m") + Optional(FinishedProduct))("f_p")
Transaction = TransactionType + Challenge

PlayerTransaction = String + ZeroOrMore(Transaction)

Round = sup_lit("round") + wrap(ZeroOrMore(PlayerTransaction("pt")))

def history_parse(tokens):
  print tokens

History = sup_lit("history") + wrap(ZeroOrMore(Round))
History.setParseAction(history_parse)

