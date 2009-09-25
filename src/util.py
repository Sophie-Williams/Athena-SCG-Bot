#!/usr/bin/env python
import logging
import urllib
import urllib2
import urlparse

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
