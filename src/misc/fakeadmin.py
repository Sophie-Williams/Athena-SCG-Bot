#!/usr/bin/python
import gflags
import sys
import urllib
import urllib2

gflags.DEFINE_string('hostport', '', 'Host:port to connect to')
gflags.DEFINE_string('filename', '', 'filename to send')

FLAGS = gflags.FLAGS
data1 = """context[
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
    99.99694187
    2
    (offered[ 500 101 (179 ) 0.43837239] offered[ 502 101 (22 ) 1.0] )
    (offered[ 504 100 (64 ) 0.641355797721469] offered[ 503 100 (44 ) 0.5900717715655577] )
    (accepted[100 501 101 (135 ) -0.00305813] )
    ()
]"""
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
    96.50792202863721
    16
    (offered[ 544 101 (123 ) 0.99999702] )
    (offered[ 509 100 (5 ) 0.9461886355453055] offered[ 519 100 (199 ) 0.983757353690309] offered[ 504 100 (95 ) 0.9757354285532864] offered[ 513 100 (149 ) 0.5586214247810751] offered[ 501 100 (61 ) 0.9182616953634173] offered[ 525 100 (171 ) 0.5709809476194994] offered[ 502 100 (53 ) 0.98989714] offered[ 534 100 (97 ) 0.519328072728226] offered[ 529 100 (89 ) 0.98989076] offered[ 540 100 (245 ) 0.8580443787376115] )
    ()
    (provided[101 v0 v1 v2 v3 v4  (32 v1 v2 v3 ) (32 v4 v0 v1 ) (32 v1 v2 v3 ) (32 v4 v0 v1 ) (32 v1 v2 v3 ) (32 v2 v3 v4 ) (32 v0 v1 v2 ) (32 v4 v0 v1 ) (32 v1 v2 v3 ) (32 v3 v4 v0 )  541 100 (32 ) 0.98989946] provided[101 v0 v1 v2 v3 v4  (79 v1 v2 v3 ) (79 v1 v2 v3 ) (79 v0 v1 v2 ) (79 v1 v2 v3 ) (79 v0 v1 v2 ) (79 v4 v0 v1 ) (79 v2 v3 v4 ) (79 v1 v2 v3 ) (79 v1 v2 v3 ) (79 v2 v3 v4 )  543 100 (79 ) 0.4814408193709827] provided[101 v0 v1 v2 v3 v4  (185 v1 v2 v3 ) (185 v2 v3 v4 ) (185 v2 v3 v4 ) (185 v1 v2 v3 ) (185 v1 v2 v3 ) (185 v2 v3 v4 ) (185 v2 v3 v4 ) (185 v0 v1 v2 ) (185 v4 v0 v1 ) (185 v0 v1 v2 )  542 100 (185 ) 0.22073769199178594] )
]"""

if __name__ == '__main__':
  FLAGS(sys.argv)
  if not FLAGS.hostport:
    print 'You need to supply a hostport!'
    sys.exit(1)
  data = open(FLAGS.filename).read()
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
