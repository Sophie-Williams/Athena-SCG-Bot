#!/usr/bin/python
import gflags
import sys
import urllib
import urllib2

gflags.DEFINE_string('hostport', '', 'Host:port to connect to')

FLAGS = gflags.FLAGS

data = """context[
    config[

    gamekind: "CSP"
    turnduration: 60
    mindecrement: 0.01
    initacc: 100.0
    objective: []
    predicate: []
    numrounds: 15
    profitfactor: 1.0

    otrounds: 2]
    101
    100.0
    1
    ()
    ()
    ()
    ()
]"""


if __name__ == '__main__':
  FLAGS(sys.argv)
  if not FLAGS.hostport:
    print 'You need to supply a hostport!'
    sys.exit(1)
  req = urllib2.Request('http://%s/player' % (FLAGS.hostport), data)
  resp = urllib2.urlopen(req)
  print 'Sending Request:'
  print '-------------------------------------'
  print data
  print '-------------------------------------'
  print '-------------------------------------'
  print
  print 'Got reply:'
  print '-------------------------------------'
  print resp.read()
