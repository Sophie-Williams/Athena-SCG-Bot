#!/usr/bin/env python
import urllib
import urllib2
import urlparse

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
  req = urllib2.Request(regurl, data)
  resp = urllib2.urlopen(req)
  for line in resp:
    if 'solve' in line:
      l = line.strip()
      return l


