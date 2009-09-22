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
  def __init__(self, config=None, offered=None, provided=None, accepted=None, 
               solved=None):
    self.offered = offered
    self.provided = provided
    self.accepted = accepted
    self.solved = solved
    self.config = config

  @classmethod
  def FromString(cls, input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parsed):
    return cls(config=Config.FromParsed(parsed.config),
               provided=parsed.provided, offered=parsed.offered,
               accepted=parsed.accepted, solved=parsed.solved)


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
  except urllib2.HTTPError, e:
    return '%s registration FAILURE! (%s)' % (ourteam, str(e))

class Game(object):
  def __init__(self, initialdata):
    self.context = PlayerContext.FromString(initialdata)

  def RunTasks(self):
    logging.info('Running all tasks...')
    for x in [self.AcceptTask, self.ProvideTask, self.SolveTask,
              self.OfferTask, self.ReofferTask]:
      x()

  def AcceptTask(self):
    logging.debug('Running AcceptTask')
    logging.debug('Available: %s' % str(self.context.offered))
    logging.debug('Available: %s' % str(self.context.provided))

  def ProvideTask(self):
    logging.debug('Running ProvideTask')

  def SolveTask(self):
    logging.debug('Running SolveTask')

  def OfferTask(self):
    logging.debug('Running OfferTask')

  def ReofferTask(self):
    logging.debug('Running ReofferTask')

  def GenerateReply(self):
    logging.info('Generating Game Reply')
    return 'playertrans[]'

  @classmethod
  def Play(cls, gamedata):
    g = cls(gamedata)
    g.RunTasks()
    return g.GenerateReply()
