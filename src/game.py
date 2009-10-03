#!/usr/bin/python
import logging
import random

import offer
import playercontext
import problem
import relation

class Game(object):
  def __init__(self, initialdata):
    self.context = playercontext.PlayerContext.FromString(initialdata)
    self.id = int(self.context.playerid)

  def RunTasks(self):
    self.replies = []
    logging.info('Starting Balance: $%0.4f' % self.context.balance)
    logging.debug('Their Offered: %s' % str(self.context.their_offered))
    logging.debug('Our Offered: %s' % str(self.context.our_offered))
    logging.debug('Accepted: %s' % str(self.context.accepted))
    logging.debug('Provided: %s' % str(self.context.provided))
    logging.info('Running all tasks...')
    for x in [self.OfferTask, self.AcceptTask, self.ReofferTask,
              self.ProvideTask, self.SolveTask]:
      x()
    logging.info('Ending Balance: $%0.4f' % self.context.endbalance)

  def AcceptTask(self):
    logging.info('Running AcceptTask')

    # Don't buy at the end of the game
    if self.context.config.numrounds == self.context.currentround:
      return

    otheroffers = list(self.context.their_offered)
    for offer in self.context.their_offered:
      if offer.price > self.context.endbalance:
        logging.info('%s is out of budget' % str(offer))
      elif offer.IsGoodBuy():
        logging.info('%s is good buy' % str(offer))
        self.replies.append(offer.GetAccept())
        self.context.endbalance -= offer.price
      else:
        logging.info('%s is bad buy' % str(offer))

    a = [x.actedon for x in self.context.their_offered]
    if True not in a:
      noreoffer = []
      for offer in self.context.their_offered:
        if offer.AvoidReoffer():
          logging.info('%s shouldn\'t reoffer' % str(offer))
          noreoffer.append(offer)
      if noreoffer:
        noreoffer.sort()
        self.replies.append(noreoffer[0].GetAccept())

  def ProvideTask(self):
    logging.info('Running ProvideTask')
    otheroffers = list(self.context.accepted)
    for accepted in self.context.accepted:
      if self.context.playerid != accepted.provider:
        continue
      logging.info('Providing number %d for offer %d'
                   % (accepted.problemnumber, accepted.offerid))
      p = problem.Problem.Generate(accepted.problemnumber, accepted.offerid)
      self.replies.append(p.GetProvide())

  def SolveTask(self):
    logging.info('Running SolveTask')
    for problem in self.context.provided:
      self.replies.append(problem.Solve())

  def OfferTask(self):
    logging.info('Running OfferTask')
    ouroffer  = [x.problemnumber for x in self.context.our_offered]
    theiroffer  = [x.problemnumber for x in self.context.their_offered]
    justoffered = []
    for x in range(random.randint(2, self.context.config.maxoffers)):
      logging.debug('Starting offer generation run')
      o = offer.Offer.GenerateOffer(ouroffer, theiroffer, justoffered)
      logging.debug('Offering %d for %0.8f' % (o.problemnumber, o.price))
      justoffered.append(o.problemnumber)
      self.replies.append(o.GetOffer())

  def HaveAcceptedOffer(self):
    a = [x.actedon for x in self.context.their_offered]
    return True in a

  def ReofferTask(self):
    logging.info('Running ReofferTask')
    if not self.HaveAcceptedOffer():
      for offer in self.context.their_offered:
        logging.info('Reoffering their id %d' % offer.offerid)
        self.replies.append(offer.GetReoffer())

  def GenerateReply(self):
    logging.info('Generating Game Reply')
    r = 'playertrans[\n    %d\n' % self.id
    for o in self.replies:
      r += '    %s \n' % o
    r +=']\n'
    logging.info('Reply Size: %s' % len(r))
    logging.debug('Reply: %s' % r)
    return r

  @classmethod
  def Play(cls, gamedata):
    logging.debug('Got: %s' % gamedata)
    for runnumber in range(5):
      try:
        g = cls(gamedata)
        g.RunTasks()
        r = g.GenerateReply()
        return r
      except:
        logging.exception('Encountered MAJOR error during gameplay')

