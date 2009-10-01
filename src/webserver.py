#!/usr/bin/env python
import gflags
import logging
import sys
import web
from web import wsgiserver

import game
import util

if __name__ == '__main__':
  gflags.DEFINE_string('ip', '0.0.0.0', 'IP to listen on')
  gflags.DEFINE_integer('port', 8080, 'Port number to listen on')
FLAGS = gflags.FLAGS

urls = (
  '/player', 'GameHandler',
  '/start', 'OkHandler',
  '/end', 'OkHandler',
  '/register', 'RegisterHandler',
)

app = web.application(urls, globals()).wsgifunc()
web.webapi.config.debug = False


class RegisterHandler(object):
  def GET(self):
    raise web.seeother('/static/register.html')

  def POST(self):
    req = web.input(host='', team='', password='')
    try:
      port = int(web.ctx.host.split(':')[-1])
    except:
      port = 8080
    return util.DoRegistration(req['host'], port,
                               req['team'], req['password'])

class GameHandler(object):
  def GET(self):
    raise web.seeother('http://www.google.com/search?q=your+doing+it+wrong')

  def POST(self):
    return game.Game.Play(web.data())

class OkHandler(object):
  def GET(self):
    return 'OK'
  POST = GET


if __name__ == '__main__':
  FLAGS(sys.argv)
  util.setuplogging()
  s = wsgiserver.CherryPyWSGIServer((FLAGS.ip, FLAGS.port), app,
                                    server_name='cs4500.server')
  s.start()
