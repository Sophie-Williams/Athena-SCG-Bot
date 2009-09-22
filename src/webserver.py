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

class RegisterHandler(object):
  def GET(self):
    raise web.seeother('/static/register.html')

  def POST(self):
    req = web.input(host='', team='', password='')
    return game.doregistration(req['host'], 8080, req['team'], req['password'])

class GameHandler(object):
  def GET(self):
    return 'Game Data Here'

  def POST(self):
    ctx = game.PlayerContext.fromString(web.data())
    print ctx
    reply = ('playertrans[\n\t'
              '101\n\t'
              'offer[(68) 0.4587002152887071]\n\t'
              'offer[(6 ) 0.5791155954286649]\n'
              ']')
    return reply

class OkHandler(object):
  def GET(self):
    return 'OK'
  POST = GET

if __name__ == '__main__':
    app.run()
