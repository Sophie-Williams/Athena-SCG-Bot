#!/usr/bin/env python
import logging
import random
import relation

class Offer(object):
  def __init__(self, offerid, playerid, problemnumber, price):
    self.offerid = int(offerid)
    self.playerid = int(playerid)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.actedon = False
    self.bep = relation.break_even(self.problemnumber, 3)
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)
  def __repr__(self):
    return str(self)

  def IsGoodBuy(self):
    if not self.problemnumber:
      return False
    elif self.problemnumber%2:
      if self.price < 1:
        return True
      else:
        return random.choice([True, False])
    elif self.problemnumber >= 128:
      if self.price < 1:
        return True
      else:
        return random.choice([True, False])
    else:
      logging.debug('Break even for %d is %08f'
                    % (self.problemnumber, self.bep))
      return (self.bep > self.price)

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
      possible = random.randint(0, 128)
      if possible%2:
        continue
      elif possible in ouroffered:
        continue
      elif possible in theiroffered:
        continue
      elif possible in justoffered:
        continue
      else:
        problemnumber = possible
    bep = relation.break_even(problemnumber, 3)
    markup = 0.5
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

