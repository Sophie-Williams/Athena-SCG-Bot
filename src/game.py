#!/usr/bin/python
"""Athena agent for CS4500.


ROFL:ROFL:LOL:ROFL:ROFL
           |
  L   /---------
 LOL===       []\
  L    \         \
        \_________\
          |     |
       -------------/

Core gameplay logic
"""

import gflags
import logging
import random
import time

import offer
import playercontext
import problem
import relation

gflags.DEFINE_integer('offercount', 2,
                      'New offers to generate each round')
FLAGS = gflags.FLAGS

class Game(object):
  """Contain core gameplay logic in this class."""

  REPLYTYPES = ['offer', 'reoffer', 'accept', 'provide', 'solve']

  def __init__(self, initialdata):
    # Get the game context
    self.context = playercontext.PlayerContext.FromString(initialdata)
    self.context.their_offered.sort()
    self.context.their_offered.reverse()
    # Store our player id locally
    self.id = int(self.context.playerid)
    # We make this a list of tuples, ('type', 'reply') 
    self.replies = []

  def CountReplyType(self, typelist):
    """Count the number of replies of a given type or types.

    Args:
       typelist: list of strings: List of REPLYTYPES

    Returns:
       integer
    """
    count = 0
    for x in self.replies:
      if x[0] in typelist:
        count += 1
    return count

  def NumProposals(self):
    """Number of proposals currently on the table."""
    return self.CountReplyType(['offer', 'reoffer'])

  def NumPropositions(self):
    """Number of propositions currently on the table."""
    return self.CountReplyType(['accept', 'reoffer'])

  def ReachedMaxProposals(self):
    """Have we offered enough proposals?"""
    if self.NumProposals() >= self.context.config.maxproposals:
      logging.info('Quitting offering because we reached maxProposals')
      return True
    else:
      return False

  def AddReply(self, replytype, reply):
    """Add a reply to the stack.

    Args:
       replytype: (string) type of reply to add
       reply: (string) the reply itself
    """
    if replytype in self.REPLYTYPES:
      logging.debug('Adding reply type "%s": "%s"' % (replytype, reply))
      self.replies.append((replytype, reply))
    else:
      logging.error('Invalid reply type "%s": "%s"' % (replytype, reply))

  def LogGameState(self):
    """Do some game state logging."""
    logging.info('Round %d Starting Balance: $%0.4f'
                 % (self.context.currentround, self.context.balance))
    logging.debug('Their Offered: %s' % str(self.context.their_offered))
    logging.debug('Our Offered: %s' % str(self.context.our_offered))
    logging.debug('Accepted: %s' % str(self.context.accepted))
    logging.debug('Provided: %s' % str(self.context.provided))

  def MetPercentOfferedSecrets(self, secretcount):
    """Determine if we met the secret percentage rule."""
    if not self.context.config.hassecrets:
      return True
    else:
      if not self.CountReplyType(['offer']):
        return False
      else:
        perc = float(secretcount) / float(self.CountReplyType(['offer']))
        return not perc < self.context.config.secretratio

  def OfferTask(self):
    logging.info('Running OfferTask')
    ouroffer    = [x.problemnumbers[0] for x in self.context.our_offered]
    theiroffer  = [x.problemnumbers[0] for x in self.context.their_offered]
    justoffered = []
    secretcount = 0
    for x in range(FLAGS.offercount):
      if not self.MetPercentOfferedSecrets(secretcount):
        kind = 'secret'
        secretcount += 1
      else:
        kind = 'all'
      o = offer.Offer.GenerateOffer(ouroffer, theiroffer, justoffered, kind)
      logging.debug('Offering %s for %0.8f' % (o.problemnumbers, o.price))
      justoffered.append(o.problemnumbers[0])
      self.AddReply('offer', o.GetOffer())

      if self.ReachedMaxProposals():
        break
  
  def AcceptTask(self):
    logging.info('Running AcceptTask')
    for offer in self.context.their_offered:
      # Meet their needs
      if self.NumPropositions() >= self.context.config.minpropositions:
        logging.info('Met minimum propositions requirement. (%d/%d)'
                     % (self.NumPropositions(),
                        self.context.config.minpropositions))
        break

      if offer.IsGoodBuy():
        logging.info('%s is good buy' % str(offer))
        self.AddReply('accept', offer.GetAccept())
        self.context.endbalance -= offer.price
      else:
        logging.info('%s is bad buy' % str(offer))

  def ReofferTask(self):
    logging.info('Running ReofferTask')
    if not self.CountReplyType(['accept']):
      for offer in self.context.their_offered:
        logging.info('Reoffering their id %d' % offer.offerid)
        self.AddReply('reoffer', offer.GetReoffer())
        break

  def ProvideTask(self):
    logging.info('Running ProvideTask')
    otheroffers = list(self.context.accepted)
    for accepted in self.context.accepted:
      if self.context.playerid != accepted.provider:
        continue
      logging.info('Providing number %s for offer %d'
                   % (accepted.problemnumbers, accepted.offerid))
      p = problem.Problem.GenerateFromAccepted(accepted)
      self.AddReply('provide', p.GetProvide())

  def SolveTask(self):
    logging.info('Running SolveTask')
    solvestart = time.time()
    for problem in self.context.provided:
      self.AddReply('solve', problem.Solve())
      self.context.endbalance += problem.profit
    if self.CountReplyType(['solve']):
      logging.info('Solves took %s seconds' % (time.time() - solvestart))

  def GenerateReply(self):
    """Generate a string reply packet for the administrator."""
    logging.info('Generating Game Reply')
    r = 'playertrans[\n    %d\n' % self.id
    for replytype, reply in self.replies:
      r += '    %s \n' % reply
    r +=']\n'
    logging.info('Reply Size: %s' % len(r))
    logging.debug('Reply: %s' % r)
    return r

  def RunTasks(self):
    """Actually run the game."""
    starttime = time.time()
    self.LogGameState()
    logging.info('Running all tasks...')
    map(lambda x: x(), [self.OfferTask, self.AcceptTask, self.ReofferTask,
                        self.ProvideTask, self.SolveTask])
    logging.info('Ending Balance: $%0.4f' % self.context.endbalance)
    endtime = time.time()
    logging.info('Gameplay took %s' % (endtime - starttime))

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

    return 'Well, shit.'
