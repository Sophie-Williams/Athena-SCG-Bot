#!/usr/bin/env python
import logging
import web

import game

urls = (
  '/player', 'GameHandler',
  '/start', 'OkHandler',
  '/end', 'OkHandler',
  '/register', 'RegisterHandler',
)

app = web.application(urls, globals())
web.webapi.config.debug = False

logging.basicConfig(level=logging.DEBUG)

class RegisterHandler(object):
  def GET(self):
    raise web.seeother('/static/register.html')

  def POST(self):
    req = web.input(host='', team='', password='')
    try:
      port = int(web.ctx.host.split(':')[-1])
    except:
      port = 8080
    return game.DoRegistration(req['host'], port,
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
    app.run()
