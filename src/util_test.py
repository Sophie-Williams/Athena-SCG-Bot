#!/usr/bin/env python
import unittest

import constants
import util

class UtilTestCase(unittest.TestCase):

  def testGetPlayerSpec(self):
    self.assertEqual(util.GetPlayerSpec('donkeys', 8080),
                     'playerspec["donkeys" "auto" 8080]')
    self.assertEqual(util.GetPlayerSpec('peaches', 7444),
                     'playerspec["peaches" "auto" 7444]')

  def testGetRegistrationURL(self):
    self.assertEqual(util.GetRegistrationURL('aurail.ccs.neu.edu', 'cheese'),
                     ('http://aurail.ccs.neu.edu:%d/register?password=cheese'
                      % (constants.GAMEREG_PORT)))
    self.assertEqual(util.GetRegistrationURL('n0r.org', 'baddpass'),
                     ('http://n0r.org:%d/register?password=baddpass'
                      % (constants.GAMEREG_PORT)))

if __name__ == '__main__':
  unittest.main()
