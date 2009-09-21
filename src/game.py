#!/usr/bin/python
import urllib
import urllib2

import parser


class Objective(object):
  pass

class Predicate(object):
  pass

class Config(object):
  def __init__(self):
    self.gamekind = None
    self.turnduration = None
    self.mindecrement = None
    self.initacc = None
    self.objective = None
    self.predicate = None
    self.numrounds = None
    self.profitfactor = None
    self.otrounds = None

  @staticmethod
  def fromString(input):
    ps = parser.Config.searchString(input)
    if ps:
      ps = ps[0]
      c = Config()
      c.PopulateFromParsed(ps)
      return c

  def PopulateFromParsed(self, parsed):
    for x in ['gamekind', 'turnduration', 'mindecrement', 'initacc',
              'objective', 'predicate', 'numrounds', 'profitfactor',
              'otrounds']:
      print '%s %s' % (x, getattr(parsed, x))
      setattr(self, x, getattr(parsed, x))

class PlayerContext(object):
  def __init__(self):
    self.config = None

  @staticmethod
  def fromString(input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      ps = ps[0]
      c = Config()
      c.PopulateFromParsed(ps)
      return c

  def PopulateFromParsed(self, parsed):
    self.config = Config()
    self.config.PopulateFromParsed(parsed.config)

class PlayerTransaction(object):
  pass

def doregistration(server, ourport, ourteam, ourpass):
  regurl = ('http://%s:7005/register?password=%s'
            % (server, ourpass))
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
