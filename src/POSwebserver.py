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
import logging

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

logging.basicConfig(level=logging.DEBUG,
                    format=('%(asctime)s %(filename)s %(lineno)s'
                            ' %(levelname)s %(message)s'))

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

    asyncore.dispatcher.__init__(self)
    self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
    self.bind((ip, port))
    self.listen(5)

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
    handler = http_handler(conn, addr)


class http_handler(asyncore.dispatcher):
  """Handle a single connection"""

  def __init__(self, conn, addr):
    asyncore.dispatcher.__init__(self, sock=conn)
    self.addr = addr

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
      logging.debug('REQUEST %s' % line)

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
      logging.debug('%s %s' %(method, file))

      if method == 'POST' and post_data and FLAGS.showrequest:
          logging.debug('POSTDATA %s' % l)

      # Top URL
      if file == '/player':
        self.handleurl_player(post_data)
      else:
        self.senderror(404, '%s file not found' % file)
        self.close()

    logging.debug('%d bytes queued' % len(self.buffer))

  def writable(self):
    return len(self.buffer) > 0

  def handle_close(self):
    pass

  def handleurl_player(self, post_data):
    """The top level page."""
    post_data = post_data.replace('\r', '')
    data = ''
    try:
      data = game.Game.Play(post_data)
    except Exception, e:
      logging.exception('Caught exception during gameplay')
      open('/tmp/gameplay-exception', 'w').write('%s\n\n-----\n\n%s'
                                                 % (post_data, data))
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
    logging.debug('Sent %d error' % number)

  def sendheaders(self, headers):
    """Send HTTP response headers."""
    if FLAGS.showresponse:
      logging.debug('RESPONSE %s' % headers)

    self.buffer += headers
  
  def handle_write(self):
    global bytes
    
    try:
      sent = self.send(self.buffer)
      bytes += sent

      self.buffer = self.buffer[sent:]
      if len(self.buffer) == 0:
        logging.debug('HTTP request complete')

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
  logging.info('Started listening on port %d' % FLAGS.port)

  last_summary = time.time()
  while running:
    last_event = time.time()
    asyncore.loop(timeout=10.0, count=1)

if __name__ == "__main__":
  main(sys.argv)
