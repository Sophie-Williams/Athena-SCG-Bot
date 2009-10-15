#!/usr/bin/env python
import unittest
import playercontext

class PlayerContextTestCase(unittest.TestCase):

  def test_FromString(self):
    src = open('testdata/context1.txt').read()
    pc = playercontext.PlayerContext.FromString(src)
    self.assertEqual(pc.balance, 95.03029999999998)
    self.assertEqual(pc.playerid, 100)
    self.assertEqual(pc.currentround, 13)
    self.assertEqual(len(pc.our_offered), 0)
    self.assertEqual(len(pc.their_offered), 1)
    self.assertEqual(len(pc.accepted), 0)
    self.assertEqual(len(pc.provided), 0)

if __name__ == '__main__':
  unittest.main()
