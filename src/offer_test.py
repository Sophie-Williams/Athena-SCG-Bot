#!/usr/bin/env python
import unittest
import offer

class OfferTestCase(unittest.TestCase):

  def setUp(self):
    self.o1 = offer.Offer(501, 101, 3, 1.0, 'all')
    self.o2 = offer.Offer(501, 101, 3, 0.555, 'all')
    self.o3 = offer.Offer(501, 101, 0, 0.1, 'all')
    self.o4 = offer.Offer(501, 101, 247, 1.0, 'all')
    self.o5 = offer.Offer(301, 101, 128, 1.0, 'all')
    self.o6 = offer.Offer(401, 101, 22, 1.0, 'all')
    self.o7 = offer.Offer(501, 101, 22, 0.2, 'secret')

  def tearDown(self):
    del self.o1
    del self.o2
    del self.o3
    del self.o4
    del self.o5
    del self.o6
    del self.o7

  def testStr(self):
    self.assertEqual(str(self.o2),
                     'Offer(id=501, from=101, problem=3, price=0.555)')

  def testRepr(self):
    self.assertEqual(str(self.o1), repr(self.o1))

  def testIsGoodBuy(self):
    self.assertEqual(True,
                     offer.Offer(-1, -1, 2, 1.0, 'secret').IsGoodBuySecret())
    self.assertEqual(False,
                     offer.Offer(-1, -1, 2, 1.0, 'all').IsGoodBuyAll())

  def testIsGoodBuySecret(self):
    # Currently we buy all secrets...
    # TODO(lee): Improve!
    self.assertEqual(True, self.o7.IsGoodBuySecret())
    self.assertEqual(True,
                     offer.Offer(-1, -1, 1, 1.0, 'secret').IsGoodBuySecret())
    self.assertEqual(True,
                     offer.Offer(-1, -1, 2, 1.0, 'secret').IsGoodBuySecret())

  def testIsGoodBuyAll(self):
    self.assertEqual(True, self.o1.IsGoodBuyAll())
    self.assertEqual(True, self.o2.IsGoodBuyAll())
    self.assertEqual(False, self.o3.IsGoodBuyAll())
    self.assertEqual(True, self.o4.IsGoodBuyAll())
    self.assertEqual(True, self.o5.IsGoodBuyAll())
    self.assertEqual(False, self.o6.IsGoodBuyAll())
    self.assertEqual(True, self.o7.IsGoodBuyAll())

  def testGetAccept(self):
    self.assertEqual(self.o7.GetAccept(), 'accept[501]')
    self.assertEqual(self.o7.actedon, True)
    self.assertEqual(self.o6.GetAccept(), 'accept[401]')
    self.assertEqual(self.o6.actedon, True)
    self.assertEqual(self.o5.GetAccept(), 'accept[301]')
    self.assertEqual(self.o5.actedon, True)

  def testGetOffer(self):
    self.assertEqual(self.o7.GetOffer(), 'offer[-1 secret ( 22) 0.20000000]')
    self.assertEqual(self.o6.GetOffer(), 'offer[-1 all ( 22) 1.00000000]')

if __name__ == '__main__':
  unittest.main()
