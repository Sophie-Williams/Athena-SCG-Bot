#!/usr/bin/env python
import problem
for x in range(128):
  if x%2:
    continue
  print '  %d:%0.18f,' % (x, problem.Problem.GetReasonablePrice(x))
