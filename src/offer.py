#!/usr/bin/env python
import logging
import random

import relation
import problem

class Offer(object):
  def __init__(self, offerid, playerid, problemnumber, price):
    self.offerid = int(offerid)
    self.playerid = int(playerid)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.actedon = False
    self.bep = relation.break_even(self.problemnumber, 3)
    self.potential = (self.price -
                      problem.Problem.GetReasonablePrice(self.problemnumber))
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)
  def __cmp__(self, other):
    return cmp(self.potential, other.potential)

  def __repr__(self):
    return str(self)

  def IsGoodBuy(self):
    if not self.problemnumber:
      return False
    elif self.problemnumber >= 128 or self.problemnumber%2:
      return self.price < 1
    else:
      return (problem.Problem.GetReasonablePrice(self.problemnumber) >
              self.price+0.01)

  def AvoidReoffer(self):
    return (self.price - 0.1) < 0 or (abs(self.bep-self.price) < 0.3)

  def GetReoffer(self, decrement=0.1):
    return 'reoffer[%d %0.18f]' % (self.offerid, self.price - decrement)

  def GetAccept(self):
    self.actedon = True
    return 'accept[%d]' % (self.offerid)

  def GetOffer(self):
    return 'offer[( %d) %0.8f]' % (self.problemnumber, self.price)

  @classmethod
  def GetOfferList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      outputlist.append(cls(*parsedlist[:4]))
      parsedlist = parsedlist[4:]
    return outputlist

  @classmethod
  def GenerateOffer(cls, ouroffered, theiroffered, justoffered):
    problemnumber = None
    while problemnumber is None:
      possible = random.randint(0, 255)
      if possible in ouroffered:
        continue
      elif possible in theiroffered:
        continue
      elif possible in justoffered:
        continue
      else:
        problemnumber = possible
    logging.debug('Got problem number %d to generate' % problemnumber)
    bep = problem.Problem.GetReasonablePrice(problemnumber)
    markup = 0.05
    if bep+markup > 1:
      price = 1
    else:
      price = bep+markup
    return cls(-1, -1, problemnumber, price)

class AcceptedChallenge(object):
  def __init__(self, acceptor, offerid, provider, problemnumber, price):
    self.acceptor = int(acceptor)
    self.offerid = int(offerid)
    self.provider = int(provider)
    self.problemnumber = int(problemnumber)
    self.price = float(price)

  def __str__(self):
    return ('AcceptedChallenge(acceptor=%s, offerid=%s, provider=%s,'
            ' problem=%s, price=%s)' % (self.acceptor, self.offerid,
                                        self.provider, self.problemnumber,
                                        self.price))
  def __repr__(self):
    return str(self)

  @classmethod
  def GetAcceptedChallengeList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      outputlist.append(cls(*parsedlist[:5]))
      parsedlist = parsedlist[5:]
    return outputlist

