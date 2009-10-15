#!/usr/bin/env python
import gflags
import logging
import random

import relation
import problem

gflags.DEFINE_float('bepbump', 0.03, 'Buy offers where `price <= bep+this`')
gflags.DEFINE_float('avoidreofferdiff', 0.3,
                    'Avoid reoffer if abs(bep-price) <= this')
FLAGS = gflags.FLAGS

class Offer(object):
  def __init__(self, offerid, playerid, problemnumber, price, kind):
    self.offerid = int(offerid)
    self.playerid = int(playerid)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.actedon = False
    self.bep = relation.break_even(self.problemnumber, 3)
    self.potential = 0
    self.kind = kind
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)
  def __repr__(self):
    return str(self)

  def __cmp__(self, other):
    return cmp(self.BEPDiff(), other.BEPDiff())

  def BEPDiff(self):
    return self.bep-self.price

  def IsGoodBuy(self):
    if self.problemnumber <= 0:
      return False
    if self.problemnumber % 2 or self.problemnumber >= 128:
      return True
    elif self.bep == 1:
      return True
    else:
      return self.bep+FLAGS.bepbump >= self.price

  def AvoidReoffer(self, mindecrement=0.1):
    return ((self.price - mindecrement) < 0
            or (abs(self.bep-self.price) <= FLAGS.avoidreofferdiff))

  def GetReoffer(self, decrement=0.1):
    """Generate a ReofferTrans from this offer.
    
    Args:
       decrement: (float) Reoffer at the current price minus this number
    """
    return 'reoffer[%d %0.18f]' % (self.offerid, self.price - decrement)

  def GetAccept(self):
    """Generate an AcceptTrans from this offer.
    Also set actedon to true so we can find out if we accepted any offers.
    """
    self.actedon = True
    return 'accept[%d]' % (self.offerid)

  def GetOffer(self):
    """Generate an OfferTrans from this offer.
    Note: -1 is a fake constant, it gets replaced by the Admin.
    """
    return ('offer[-1 %s ( %d) %0.8f]'
            % (self.kind, self.problemnumber, self.price))

  def SetPrice(self, price=None, markup=0.1):
    """Set or Generate a price."""
    if price is None:
      price = relation.break_even(self.problemnumber, 3)
    price += markup
    if price >1:
      price = 1
    self.price = price
    logging.debug('Got problem number %d to generate price @ %s'
                  % (self.problemnumber, self.price))
    return price

  @classmethod
  def GetOfferList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      logging.info(parsedlist[:5])
      outputlist.append(cls(*parsedlist[:5]))
      parsedlist = parsedlist[5:]
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
      o = cls(-1, -1, problemnumber, -1, 'all')
      price = o.SetPrice()
      return o

  @classmethod
  def GenerateOffer(cls, ouroffered, theiroffered, justoffered):
    goodones = filter(lambda x: not x%2, range(128))
    badones = filter(lambda x: x%2, range(128)) + range(128,256)
    random.shuffle(goodones)
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
  def __init__(self, acceptor, offerid, provider, problemnumber, price, kind):
    self.acceptor = int(acceptor)
    self.offerid = int(offerid)
    self.provider = int(provider)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.kind = kind

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
      logging.info(parsedlist)
      outputlist.append(cls(*parsedlist[:6]))
      parsedlist = parsedlist[6:]
    return outputlist

