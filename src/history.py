#!/usr/bin/python
import parser_history


class ListsAndThings(object):
  name = 'ListsAndThings'
  pre = ''
  delim = ' '
  innerType = None

  def __init__(self, *args):
    self.things = list(args)

  def add(self, thing):
    self.things.append(thing)

  @classmethod
  def parse(cls, lexp):
    lat = cls()
    for x in lexp:
      lat.add(cls.innerType.parse(x))
    return lat

  def __str__(self):
    """
    >>> x = ListsAndThings(1, 2, 3)
    >>> str(x)
    'ListsAndThings[1 2 3 ]'
    >>> x.add(4)
    >>> str(x)
    'ListsAndThings[1 2 3 4 ]'
    >>> str(ListsAndThings())
    'ListsAndThings[]'
    """
    s = ''
    for x in self.things:
      s += '%s%s%s' % (self.pre, str(x), self.delim)
    return '%s[%s]' % (self.name, s)


class S(object):

  @classmethod
  def parse(cls, lexp):
    return lexp


class Round(ListsAndThings):
  name = 'round'
  innerType = S


class History(ListsAndThings):
  """
  >>> x = History()
  >>> str(x)
  'history[]'
  >>> y = History(*[Round(), Round()])
  >>> str(y)
  'history[round[] round[] ]'
  """
  name = 'history'
  innerType = Round


def GetHistory(ctx):
  hctx = parser_history.History.searchString(ctx)
  if hctx:
    return History.parse(hctx)


if __name__ == '__main__':
  import sys
  print GetHistory(open('./sample-history', 'r').read())
