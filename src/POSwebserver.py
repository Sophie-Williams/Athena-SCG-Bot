#!/usr/bin/python

# This code is from http://www.stillhq.com/svn/trunk/mp3server/server.py
# Using it as a super simple http server until the bugs with the DemeterF http
# library are fixed.

import asyncore
import base64
import cStringIO
import datetime
import os
import re
import socket
import sys
import time
import types
import urllib
import uuid
import gflags

import game


FLAGS = gflags.FLAGS
gflags.DEFINE_string('ip', '0.0.0.0', 'Bind to this IP')
gflags.DEFINE_integer('port', 8080, 'Bind to this port')

gflags.DEFINE_boolean('showrequest', False,
                      'Show the content of HTTP request')
gflags.DEFINE_boolean('showpost', False, 'Show the content of POST operations')
gflags.DEFINE_boolean('showresponse', False,
                      'Show the content of response headers')

running = True
uuid = uuid.uuid4()

requests = {}
skips = {}
bytes = 0


def htc(m):
  return chr(int(m.group(1),16))


def urldecode(url):
  url = url.replace('+', ' ')
  rex = re.compile('%([0-9a-hA-H][0-9a-hA-H])',re.M)
  return rex.sub(htc,url)


class http_server(asyncore.dispatcher):
  """Listen for new client connections, which are then handed off to
     another class
  """

  def __init__(self, ip, port):
    self.ip= ip
    self.port = port
    self.logfile = open('log', 'a')

    asyncore.dispatcher.__init__(self)
    self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
    self.bind((ip, port))
    self.listen(5)

  def log(self, msg, console=True):
    """Write a log line."""

    l = '%s %s %s\n' %(datetime.datetime.now(), repr(self.addr), msg)
    
    if console:
      sys.stdout.write(l)

    self.logfile.write(l)
    self.logfile.flush()

  def writable(self):
    return 0

  def handle_read(self):
    pass

  def readable(self):
    return self.accepting

  def handle_connect(self):
    pass

  def handle_accept(self):
    conn, addr = self.accept()
    handler = http_handler(conn, addr, self.log)


class http_handler(asyncore.dispatcher):
  """Handle a single connection"""

  def __init__(self, conn, addr, log):
    asyncore.dispatcher.__init__(self, sock=conn)
    self.addr = addr
    self.log = log

    self.buffer = ''

    # Used to track uPnP plays
    self.is_mp3 = False
    self.is_tracked = False
    self.streamed_at = None
    self.id = None

    self.cookie = None
    self.extra_headers = []

    self.client_id = 0

  def handle_read(self):
    rq = self.recv(32 * 1024)
    file = ''
    method = None
    post_data = None
    chunk = None

    for line in rq.split('\n'):
      line = line.rstrip('\r')
      self.log('REQUEST %s' % line, console=FLAGS.showrequest)

      try:
        if line.startswith('GET'):
          (_, file, _) = line.split(' ')
          method = 'GET'

        if line.startswith('POST'):
          (_, file, _) = line.split(' ')
          method = 'POST'

        if line.startswith('Range: '):
          chunk = line[7:]
        if line.startswith('Cookie: '):
          self.cookie = line[8:]

        if post_data is not None:
          post_data += '%s\r\n' % line

        if len(line) == 0 and method == 'POST' and post_data is None:
          post_data = ''
      except:
        self.senderror(400, 'Bad request')
        self.close()
        return


    if file:
      self.log('%s %s' %(method, file))

      if method == 'POST' and post_data:
        for l in post_data.split('\r\n'):
          self.log('DATA %s' % l, console=FLAGS.showpost)

      # Top URL
      if file == '/player':
        self.handleurl_player(post_data)
      else:
        self.senderror(404, '%s file not found' % file)
        self.close()

    self.log('%d bytes queued' % len(self.buffer))

  def writable(self):
    return len(self.buffer) > 0

  def handle_close(self):
    pass

  def handleurl_player(self, post_data):
    """The top level page."""
    try:
      post_data = post_data.replace('\r', '')
      data = game.Game.Play(post_data)
    except Exception, e:
      self.senderror(500, 'Something Blew Up', str(e))
      self.close()
      return

    self.sendheaders('HTTP/1.0 200 OK\n'
                     'Content-Type: text/plain\n'
                     'Content-Length: %s\n'            
                     '\n\n'
                     %(len(data)))

    self.buffer += data

  def xmlsafe(self, s):
    """Return an XML safe version of a string."""
    
    for repl in [('&', '&amp;'), ('"', '&quot;'),
                 ('<', '&lt;'), ('>', '&gt;'),
                 ("'", '')]:
      (i, o) = repl
      s = s.replace(i, o)
    return s

  def sendredirect(self, path):
    """Send a HTTP 302 redirect."""

    self.sendheaders('HTTP/1.1 302 Found\r\n'
                     'Location: %s\r\n'
                     % path)


  def senderror(self, number, msg, fullmsg=None):
    self.sendheaders('HTTP/1.1 %d %s\r\n'
                     'Content-Type: text/html\r\n\r\n'
                     %(number, msg))
    if not fullmsg:
      fullmsg = msg
    self.buffer += ('<html><head><title>MP3 server</title></head>'
                     '<body>%s</body>'
                     % fullmsg)
    self.log('Sent %d error' % number)

  def sendheaders(self, headers):
    """Send HTTP response headers."""

    for l in headers.split('\r\n'):
      self.log('RESPONSE %s' % l, console=FLAGS.showresponse)

    self.buffer += headers
  
  def handle_write(self):
    global bytes
    
    try:
      sent = self.send(self.buffer)
      bytes += sent

      self.buffer = self.buffer[sent:]
      if len(self.buffer) == 0:
        self.log('HTTP request complete')

        self.close()

    except:
      pass



def main(argv):
  global running
  global bytes
  
  # Parse flags
  try:
    argv = FLAGS(argv)
  except gflags.FlagsError, e:
    print FLAGS

  server = http_server(FLAGS.ip, FLAGS.port)

  # Start the web server, which takes over this thread
  print '%s Started listening on port %s' %(datetime.datetime.now(),
                                            FLAGS.port)

  last_summary = time.time()
  while running:
    last_event = time.time()
    asyncore.loop(timeout=10.0, count=1)

    if time.time() - last_event > 9.0:
      # We are idle
      print '%s ...' % datetime.datetime.now()
      


if __name__ == "__main__":
  main(sys.argv)
