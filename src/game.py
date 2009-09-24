#!/usr/bin/python
import logging
import urllib
import urllib2
import urlparse

import parser
import relation

GAMEREG_PORT = 7005

class Objective(object):
  pass


class Predicate(object):
  pass


class Config(object):

  def __init__(self, gamekind=None, turnduration=None, mindecrement=None,
               initacc=None, objective=None, predicate=None, numrounds=None,
               profitfactor=None, otrounds=None):
    """
    A Config object with type enforcement.
    """
    self.gamekind = gamekind
    self.turnduration = int(turnduration)
    self.mindecrement = float(mindecrement)
    self.initacc = float(initacc)
    self.objective = objective
    self.predicate = predicate
    self.numrounds = int(numrounds)
    self.profitfactor = float(profitfactor)
    self.otrounds = int(otrounds)

  @classmethod
  def FromString(cls, input):
    ps = parser.Config.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parse_obj):
    return cls(**parse_obj.asDict())

class Offer(object):
  def __init__(self, offerid, playerid, problemnumber, price):
    self.offerid = int(offerid)
    self.playerid = int(playerid)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)

  def IsGoodBuy(self):
    return relation.relation.break_even(self.problemnumber, 3) < self.price

  @classmethod
  def GetOfferList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      outputlist.append(cls(*parsedlist[:4]))
      parsedlist = parsedlist[4:]
    return outputlist

class PlayerContext(object):
  def __init__(self, config=None, their_offered=None,
               our_offered=None, accepted=None, 
               provided=None, playerid=None):
    self.their_offered = Offer.GetOfferList(their_offered)
    self.our_offered = Offer.GetOfferList(our_offered)
    self.accepted = accepted
    self.provided = provided
    self.config = config
    self.playerid = int(playerid)

  @classmethod
  def FromString(cls, input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parsed):
    return cls(config=Config.FromParsed(parsed.config),
               their_offered=parsed.their_offered,
               our_offered=parsed.our_offered,
               accepted=parsed.accepted, provided=parsed.provided,
               playerid=parsed.playerid)


class PlayerTransaction(object):
  pass

def DoRegistration(server, ourport, ourteam, ourpass):
  reghost = '%s:%s' % (server, GAMEREG_PORT)
  regurl = urlparse.urlunparse(('http', reghost, '/register', '',
                                urllib.urlencode({'password': ourpass}), ''))
  regdata = 'playerspec["%s" "auto" %d]' % (ourteam, ourport)
  req = urllib2.Request(regurl, regdata)
  try:
    resp = urllib2.urlopen(req).read()
    if str(resp) == str(ourteam):
      return '%s registration success!' % ourteam
    else:
      return '"%s" != "%s"' % (str(resp), str(ourteam))
  except Exception, e:
    logging.exception('Reg Failure at %s ' % regurl)
    return '%s registration FAILURE! (%s)' % (ourteam, str(e))



class Game(object):
  def __init__(self, initialdata):
    self.context = PlayerContext.FromString(initialdata)
    self.id = int(self.context.playerid)

  def RunTasks(self):
    self.offers = []
    logging.debug('Their Offered: %s' % str(self.context.their_offered))
    logging.debug('Our Offered: %s' % str(self.context.our_offered))
    logging.debug('Accepted: %s' % str(self.context.accepted))
    logging.debug('Provided: %s' % str(self.context.provided))
    logging.info('Running all tasks...')
    for x in [self.AcceptTask, self.ProvideTask, self.SolveTask,
              self.OfferTask, self.ReofferTask]:
      x()

  #TODO(wan): Actually accept good buys
  #TODO(lee): Actually be able to solve good buys
  def AcceptTask(self):
    logging.debug('Running AcceptTask')
    otheroffers = list(self.context.their_offered)
    for offer in self.context.their_offered:
      if offer.IsGoodBuy():
        logging.info('%s is good buy' % str(offer))
      else:
        logging.info('%s is bad buy' % str(offer))

  #TODO(lee): Can we generate some problems here?
  def ProvideTask(self):
    logging.debug('Running ProvideTask')
    otheroffers = list(self.context.accepted)
    while otheroffers:
      id, bla, blah, blaah, price = otheroffers[:5]
      otheroffers = otheroffers[5:]
      self.offers.append('provide[v0 v1 v2 v3 v4  (229 v3 v4 v0 ) (229 v0 v1 v2 ) (229 v3 v4 v0 ) (229 v3 v4 v0 ) (229 v2 v3 v4 ) (229 v4 v0 v1 ) (229 v0 v1 v2 ) (229 v3 v4 v0 ) (229 v0 v1 v2 ) (229 v4 v0 v1 ) %s ]' % id)

  def SolveTask(self):
    logging.debug('Running SolveTask')

  def OfferTask(self):
    logging.debug('Running OfferTask')
    self.offers.append('offer[(229) 0.8710813713911519]')

  def ReofferTask(self):
    logging.debug('Running ReofferTask')
    otheroffers = (list(self.context.their_offered)
                   + list(self.context.our_offered))
    for offer in otheroffers:
      self.offers.append('reoffer[%d %0.8f]'
                         % (offer.offerid, offer.price - 0.1))

  def GenerateReply(self):
    logging.info('Generating Game Reply')
    r = '\nplayertrans[\n    %d\n' % self.id
    for o in self.offers:
      r += '    %s \n' % o
    r +=']\n'
    logging.info('Replying with: %s' % r)
    return r

  @classmethod
  def Play(cls, gamedata):
    logging.debug('Got: %s' % gamedata)
    g = cls(gamedata)
    g.RunTasks()
    return g.GenerateReply()
