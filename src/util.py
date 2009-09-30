#!/usr/bin/env python
import logging
import urllib
import urllib2
import urlparse
import time

import constants


def DoRegistration(server, ourport, ourteam, ourpass):
  reghost = '%s:%s' % (server, constants.GAMEREG_PORT)
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

def setuplogging():
  msgformat = '%(asctime)s %(filename)s %(lineno)s %(levelname)s %(message)s'
  logfilename='/var/tmp/athena-%s.log' % time.time()
  # set up logging to file - see previous section for more details
  logging.basicConfig(level=logging.DEBUG,
                      format=msgformat,
                      datefmt='%m-%d %H:%M',
                      filename=logfilename,
                      filemode='w')
  # define a Handler which writes INFO messages or higher to the sys.stderr
  console = logging.StreamHandler()
  console.setLevel(logging.INFO)
  # set a format which is simpler for console use
  formatter = logging.Formatter(msgformat)
  # tell the handler to use this format
  console.setFormatter(formatter)
  # add the handler to the root logger
  logging.getLogger('').addHandler(console)
  logging.info('Starting debug log at %s' % logfilename)
