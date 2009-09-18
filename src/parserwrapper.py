#!/usr/bin/python
import parser


def GetContext(ctx):
  pctx = parser.PlayerContext.searchString(ctx)
  if pctx:
    return pctx[0]

