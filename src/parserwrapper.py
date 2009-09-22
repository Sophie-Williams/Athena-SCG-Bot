#!/usr/bin/python
import parser


def GetContext(ctx):
  pctx = parser.PlayerContext.searchString(ctx)
  if pctx:
    return pctx


if __name__ == '__main__':
  import sys
  print GetContext(open('./sample-context', 'r').read())
