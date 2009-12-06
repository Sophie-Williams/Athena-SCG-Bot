#!/usr/bin/env python
"""Forward a solve to another agent."""

import gflags
import urllib
import urllib2
import urlparse
import socket

gflags.DEFINE_string('proxysolvehost', 'login-linux.ccs.neu.edu',
                     'Machine to proxy solve requests to')
gflags.DEFINE_integer('proxysolveport', 7001,
                     'TCP port to proxy solve requests to')
FLAGS = gflags.FLAGS

TEMPLATE="""context[
    config[
    gamekind: "CSP"
    turnduration: 60
    mindecrement: 0.01
    initacc: 100.0
    maxOffers: 5
    objective: []
    predicate: [2000]
    numrounds: 15
    profitfactor: 1.0
    otrounds: 2]
    101
    99.9869263942152
    2
    ()
    ()
    ()
    (%s)
]"""


def ProxySolve(problem):
  solvehost = '%s:%d' % (FLAGS.proxysolvehost, FLAGS.proxysolveport)
  regurl = urlparse.urlunparse(('http', solvehost, '/player', '', '', ''))
  data = TEMPLATE % problem.GetProvided()
  s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s.connect(('login-linux.ccs.neu.edu', 7001))
  s.send('POST /player HTTP/1.0\r\n\r\n%s' % (data))
  d = ''
  while 1:
    d1 =  s.recv(1024)
    if not d1:
      break
    else:
      d += d1
  for line in d.split('\n'):
    if 'solve' in line:
      l = line.strip()
      return l


