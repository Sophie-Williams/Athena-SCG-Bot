#!/usr/bin/python
import logging
import urllib
import urllib2
import urlparse

import parser

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


class PlayerContext(object):
  def __init__(self, config=None, their_offered=None,
               our_offered=None, accepted=None, 
               provided=None, playerid=None):
    self.their_offered = their_offered
    self.our_offered = our_offered
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

  def AcceptTask(self):
    logging.debug('Running AcceptTask')
    logging.debug(self.context.their_offered.asDict())

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
    otheroffers = list(self.context.their_offered) + list(self.context.our_offered)
    while otheroffers:
      id, bla, blah, price = otheroffers[:4]
      otheroffers = otheroffers[4:]
      self.offers.append('reoffer[%d %0.8f]' % (int(id), float(price)-0.01))

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
