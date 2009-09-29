#!/usr/bin/python
import logging
import random

import playercontext
import problem
import proxysolver
import relation.relation

class Game(object):
  def __init__(self, initialdata):
    self.context = playercontext.PlayerContext.FromString(initialdata)
    self.id = int(self.context.playerid)

  def RunTasks(self):
    self.replies = []
    logging.info('Current Balance: $%0.2f' % self.context.balance)
    logging.debug('Their Offered: %s' % str(self.context.their_offered))
    logging.debug('Our Offered: %s' % str(self.context.our_offered))
    logging.debug('Accepted: %s' % str(self.context.accepted))
    logging.debug('Provided: %s' % str(self.context.provided))
    logging.info('Running all tasks...')
    for x in [self.OfferTask, self.AcceptTask, self.ReofferTask,
              self.ProvideTask, self.SolveTask]:
      x()

  def AcceptTask(self):
    logging.debug('Running AcceptTask')
    otheroffers = list(self.context.their_offered)
    for offer in self.context.their_offered:
      if offer.IsGoodBuy():
        logging.info('%s is good buy' % str(offer))
        self.replies.append(offer.GetAccept())
      elif offer.price > self.context.balance:
        logging.info('%s is out of budget' % str(offer))
      else:
        logging.info('%s is bad buy' % str(offer))

    a = [x.actedon for x in self.context.their_offered]
    if True not in a:
      for offer in self.context.their_offered:
        if offer.AvoidReoffer():
          logging.info('%s shouldn\'t reoffer' % str(offer))
          self.replies.append(offer.GetAccept())

  def ProvideTask(self):
    logging.debug('Running ProvideTask')
    otheroffers = list(self.context.accepted)
    for accepted in self.context.accepted:
      if self.context.playerid != accepted.provider:
        continue
      p = problem.Problem.GenerateProblem(accepted.problemnumber,
                                          20, accepted.offerid)
      self.replies.append(p.GetProvide())

  def SolveTask(self):
    logging.debug('Running SolveTask')
    for problem in self.context.provided:
      self.replies.append(problem.Solve())

  def OfferTask(self):
    logging.debug('Running OfferTask')
    ouroffer  = [x.problemnumber for x in self.context.our_offered]
    theiroffer  = [x.problemnumber for x in self.context.their_offered]
    problemno = None
    first = None
    for x in [1,2]:
      while True:
        # We should pick better relation numbers here
        problemno = random.randint(1,255)
        if problemno in ouroffer or problemno == first:
          logging.debug('Can\'t offer %d, already offered by us' % problemno)
        elif problemno in theiroffer:
          logging.debug('Can\'t offer %d, already offered by them' % problemno)
        else:
          first = problemno
          price = 1
          logging.debug('Offering %d for %0.8f' % (problemno, price))
          self.replies.append('offer[( %d) %0.8f]' % (problemno, price))
          break

  def HaveAcceptedOffer(self):
    a = [x.actedon for x in self.context.their_offered]
    return True in a

  def ReofferTask(self):
    logging.debug('Running ReofferTask')
    if not self.HaveAcceptedOffer():
      for offer in self.context.their_offered:
        logging.debug('Reoffering their id %d' % offer.offerid)
        self.replies.append(offer.GetReoffer())

  def GenerateReply(self):
    logging.info('Generating Game Reply')
    r = 'playertrans[\n    %d\n' % self.id
    for o in self.replies:
      r += '    %s \n' % o
    r +=']\n'
    logging.info('Reply Size: %s' % len(r))
    logging.info('Replying with: %s' % r)
    return r

  @classmethod
  def Play(cls, gamedata):
    logging.debug('Got: %s' % gamedata)
    g = cls(gamedata)
    g.RunTasks()
    return g.GenerateReply()
