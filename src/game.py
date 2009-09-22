#!/usr/bin/python
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
  def __init__(self, config=None, provided=None):
    self.config = config

  @classmethod
  def FromString(cls, input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parsed):
    return cls(config=Config.FromParsed(parsed.config),
               provided=parsed.provided)


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


def DoPlay(gamedata):
  pctx = PlayerContext.FromString(gamedata)
  return str(pctx)
