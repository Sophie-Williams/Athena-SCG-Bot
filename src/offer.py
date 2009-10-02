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
    self.potential = 0
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)
  def __cmp__(self, other):
    return cmp(self.BEPDiff(), other.BEPDiff())

  def __repr__(self):
    return str(self)

  def IsGoodBuy(self):
    if self.bep == 1:
      return True
    elif not self.bep:
      return False
    else:
      return self.bep < self.price

  def BEPDiff(self):
    return abs(self.bep-self.price)

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
  def GetGenerateOffer(cls, ouroffered, theiroffered, justoffered, 
                       problemnumber):
    if problemnumber in ouroffered:
      return False
    elif problemnumber in theiroffered:
      return False
    elif problemnumber in justoffered:
      return False
    else:
      logging.debug('Got problem number %d to generate' % problemnumber)
      price = 1
      return cls(-1, -1, problemnumber, price)

  @classmethod
  def GenerateOffer(cls, ouroffered, theiroffered, justoffered):
    goodones = filter(lambda x: not x%2, range(128))
    badones = filter(lambda x: x%2, range(128)) + range(128,256)
    for problemnumber in goodones:
      x = cls.GetGenerateOffer(ouroffered, theiroffered, justoffered,
                               problemnumber)
      if x:
        return x
    for problemnumber in badones:
      x = cls.GetGenerateOffer(ouroffered, theiroffered, justoffered,
                               problemnumber)
      if x:
        return x

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

