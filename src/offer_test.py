#!/usr/bin/env python
import unittest
import offer

class OfferTestCase(unittest.TestCase):

  def setUp(self):
    self.o1 = offer.Offer(501, 101, 3, 1.0)
    self.o2 = offer.Offer(501, 101, 3, 0.555)
    self.o3 = offer.Offer(501, 101, 0, 0.1)
    self.o4 = offer.Offer(501, 101, 247, 1.0)
    self.o5 = offer.Offer(501, 101, 128, 1.0)
    self.o6 = offer.Offer(501, 101, 22, 1.0)

  def tearDown(self):
    del self.o1
    del self.o2
    del self.o3
    del self.o4
    del self.o5
    del self.o6

  def testStr(self):
    self.assertEqual(str(self.o2),
                     'Offer(id=501, from=101, problem=3, price=0.555)')

  def testRepr(self):
    self.assertEqual(str(self.o1), repr(self.o1))

  def testIsGoodBuy(self):
    self.assertEqual(True, self.o1.IsGoodBuy())
    self.assertEqual(True, self.o2.IsGoodBuy())
    self.assertEqual(False, self.o3.IsGoodBuy())
    self.assertEqual(True, self.o4.IsGoodBuy())
    self.assertEqual(True, self.o5.IsGoodBuy())
    self.assertEqual(False, self.o6.IsGoodBuy())

if __name__ == '__main__':
  unittest.main()
