#!/usr/bin/env python
import gflags
import logging
import urllib
import urllib2
import urlparse
import time

import constants

gflags.DEFINE_enum('loglevel', 'info',
                   ['debug', 'info', 'warning', 'error', 'critical'],
                   'Display this level and higher to the console')

FLAGS = gflags.FLAGS

def GetPlayerSpec(teamname, teamport):
  return 'playerspec["%s" "auto" %d]' % (teamname, teamport)

def GetRegistrationURL(server, password):
  reghost = '%s:%s' % (server, constants.GAMEREG_PORT)
  return urlparse.urlunparse(('http', reghost, '/register', '',
                              urllib.urlencode({'password': password}), ''))

def DoRegistration(server, ourport, ourteam, ourpass):
  regdata = GetPlayerSpec(ourteam, ourport)
  regurl  = GetRegistrationURL(server, ourpass)
  req = urllib2.Request(regurl, regdata)
  try:
    resp = urllib2.urlopen(req).read()
    if str(resp) == str(ourteam):
      logging.info('Registered to %s as %s' % (server, ourteam))
      return '%s registration success!' % ourteam
    else:
      return '"%s" != "%s"' % (str(resp), str(ourteam))
  except Exception, e:
    logging.exception('Reg Failure at %s ' % regurl)
    return '%s registration FAILURE! (%s)' % (ourteam, str(e))

def setuplogging():
  msgformat = '%(asctime)s %(filename)s %(lineno)s %(levelname)s %(message)s'
  logfilename = '/var/tmp/athena-%s.log' % time.time()
  # set up logging to file - see previous section for more details
  logging.basicConfig(level=logging.DEBUG,
                      format=msgformat,
                      datefmt='%m-%d %H:%M',
                      filename=logfilename,
                      filemode='w')
  # define a Handler which writes INFO messages or higher to the sys.stderr
  console = logging.StreamHandler()
  levelmap = {'debug':    logging.DEBUG,   'info':  logging.INFO,
              'warning':  logging.WARNING, 'error': logging.ERROR,
              'critical': logging.CRITICAL}
  console.setLevel(levelmap[FLAGS.loglevel])
  # set a format which is simpler for console use
  formatter = logging.Formatter(msgformat)
  # tell the handler to use this format
  console.setFormatter(formatter)
  # add the handler to the root logger
  logging.getLogger('').addHandler(console)
  logging.info('Starting debug log at %s' % logfilename)
