#!/usr/bin/env python
import gflags
import logging
import random

import relation
import problem
import constants

gflags.DEFINE_float('bepbump', 0.03, 'Buy offers where `price <= bep+this`')
gflags.DEFINE_float('avoidreofferdiff', 0.3,
                    'Avoid reoffer if abs(bep-price) <= this')
gflags.DEFINE_float('mindecrement', 0.01,
                    'The minimum decrement for reoffers')
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
    if self.bep == 1:
      return True
    
    try:
      if self.price <= constants.PRICES[13][self.problemnumber]:
        return True
    except KeyError:
      pass
    return (self.bep + FLAGS.bepbump) >= self.price

  def AvoidReoffer(self, mindecrement=0.01):
    new_price = self.price - mindecrement

    probably_solvable = False
    try:
      probably_solvable = new_price < constants.PRICES[13][self.problemnumber]
    except KeyError:
      pass
    return (self.price - mindecrement) < 0 or probably_solvable

  def GetReoffer(self, decrement=0.01):
    """Generate a ReofferTrans from this offer.
    
    Args:
       decrement: (float) Reoffer at the current price minus this number
    """
    try:
      new_price = (constants.PRICES[13][self.problemnumber]
                   + 0.5 * decrement)
      if new_price > (self.price - decrement):
        raise
    except:
      new_price = self.price - decrement
    return 'reoffer[%d %0.18f]' % (self.offerid, new_price)

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

  def SetPrice(self, price=None, markup=0.09):
    """Set or Generate a price."""
    apply_markup = True
    if price is None:
      try:
        price = (constants.PRICES[13][self.problemnumber]
               + 0.5 * FLAGS.mindecrement)
        apply_markup = False
      except KeyError:
        price = relation.break_even(self.problemnumber, 3)
    if apply_markup:
      price += markup
    if price > 1:
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

