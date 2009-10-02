#!/usr/bin/env python
import gflags
import logging
import sys
import web
import os
from web import wsgiserver

## For Django Templating
from django.template.loader import render_to_string
from django.conf import settings
settings.configure(TEMPLATE_DIRS=(os.path.join(os.getcwd(), 'static'),))

import constants
import game
import util

gflags.DEFINE_string('ip', '0.0.0.0', 'IP to listen on')
gflags.DEFINE_integer('port', 8080, 'Port number to listen on')
FLAGS = gflags.FLAGS

urls = (
  '/player', 'GameHandler',
  '/start', 'OkHandler',
  '/end', 'OkHandler',
  '/register', 'RegisterHandler',
  '/diediedie', 'DieHandler',
)

class DieHandler(object):
  def GET(self):
    logging.info('Killed by DieHandler')
    os.abort()

class RegisterHandler(object):
  def GET(self):
    return render_to_string('register.html',
                            {'host': constants.GAMEREG_HOST,
                             'teamname': constants.TEAM_NAME})

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

def apprunner(args=sys.argv):
  FLAGS(args)
  util.setuplogging()
  web.webapi.config.debug = False
  app = web.application(urls, globals()).wsgifunc()
  s = wsgiserver.CherryPyWSGIServer((FLAGS.ip, FLAGS.port), app,
                                    server_name='cs4500.server')
  s.start()

if __name__ == '__main__':
  apprunner()
