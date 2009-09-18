#!/usr/bin/python
import parser_history


class ListsAndThings(object):
  name = 'ListsAndThings'

  def __init__(self, things=None):
    self.things = things
    if things is None:
      self.things = []

  def add(self, thing):
    self.things.append(thing)

  def __str__(self):
    s = ''
    for x in self.things:
      s += str(x)
    return '%s[%s]' % (self.name, s)


class History(ListsAndThings):
  name = 'history'


class Round(ListsAndThings):
  name = 'round'


def GetHistory(ctx):
  hctx = parser_history.History.searchString(ctx)
  if hctx:
    return hctx


if __name__ == '__main__':
  import sys
  print GetHistory(open('./sample-history', 'r').read())
