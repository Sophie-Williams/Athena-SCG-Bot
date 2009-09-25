#!/usr/bin/python
import logging
import random
import urllib
import urllib2
import urlparse

import parser
import relation
import relation.gen

class Game(object):
  def __init__(self, initialdata):
    self.context = PlayerContext.FromString(initialdata)
    self.id = int(self.context.playerid)

  def RunTasks(self):
    self.offers = []
    logging.debug('Their Offered: %s' % str(self.context.their_offered))
    logging.debug('Our Offered: %s' % str(self.context.our_offered))
    logging.debug('Accepted: %s' % str(self.context.accepted))
    logging.debug('Provided: %s' % str(self.context.provided))
    logging.info('Running all tasks...')
    for x in [self.AcceptTask, self.ProvideTask, self.SolveTask,
              self.OfferTask, self.ReofferTask]:
      x()

  #TODO(wan): Actually accept good buys
  #TODO(lee): Actually be able to solve good buys
  def AcceptTask(self):
    logging.debug('Running AcceptTask')
    otheroffers = list(self.context.their_offered)
    for offer in self.context.their_offered:
      if offer.IsGoodBuy():
        logging.info('%s is good buy' % str(offer))
        self.offers.append(offer.GetAccept())
      elif offer.price > self.context.balance:
        logging.info('%s is out of budget' % str(offer))
      else:
        logging.info('%s is bad buy' % str(offer))

  def ProvideTask(self):
    logging.debug('Running ProvideTask')
    otheroffers = list(self.context.accepted)
    for accepted in self.context.accepted:
      if self.context.playerid != accepted.provider:
        continue
      self.offers.append(Problem.GenerateProblem(accepted.problemnumber, 5,
                                                 accepted.offerid).GetProvide())

  def SolveTask(self):
    logging.debug('Running SolveTask')
    for problem in self.context.provided:
      problem.Solve()

  def OfferTask(self):
    logging.debug('Running OfferTask')
    ouroffer  = [x.problemnumber for x in self.context.our_offered]
    theiroffer  = [x.problemnumber for x in self.context.their_offered]
    problemno = None
    while True:
      problemno = random.randint(2,256) 
      if problemno in ouroffer:
        logging.debug('Can\'t offer %d, already offered by us' % problemno)
      elif problemno in theiroffer:
        logging.debug('Can\'t offer %d, already offered by them' % problemno)
      else:
        price = 1.0 - 0.00001*random.random()
        logging.debug('Offering %d for %0.8f' % (problemno, price))
        self.offers.append('offer[( %d) %0.8f]' % (problemno, price))
        break
                          

  def ReofferTask(self):
    logging.debug('Running ReofferTask')
    otheroffers = (list(self.context.their_offered)
                   + list(self.context.our_offered))
    a = [x.actedon for x in self.context.their_offered]
    if True in a:
      return
    else:
      for offer in otheroffers:
        self.offers.append(offer.GetReoffer())

  def GenerateReply(self):
    logging.info('Generating Game Reply')
    r = '\nplayertrans[\n    %d\n' % self.id
    for o in self.offers:
      r += '    %s \n' % o
    r +=']\n'
    logging.info('Replying with: %s' % r)
    return r

  @classmethod
  def Play(cls, gamedata):
    logging.debug('Got: %s' % gamedata)
    g = cls(gamedata)
    g.RunTasks()
    return g.GenerateReply()
