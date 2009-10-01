#!/usr/bin/env python
import urllib
import urllib2
import urlparse
import socket

TEMPLATE="""context[
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
    99.9869263942152
    2
    ()
    ()
    ()
    (%s)
]"""

HOST="login-linux:7001"

def ProxySolve(problem):
  regurl = urlparse.urlunparse(('http', HOST, '/player', '', '', ''))
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


