#!/usr/bin/python
import logging
import random
import urllib
import urllib2
import urlparse

import parser
import relation
import relation.gen

GAMEREG_PORT = 7005
NUM_VARS = 5
NUM_CLAUSES = 10

def DoRegistration(server, ourport, ourteam, ourpass):
  reghost = '%s:%s' % (server, GAMEREG_PORT)
  regurl = urlparse.urlunparse(('http', reghost, '/register', '',
                                urllib.urlencode({'password': ourpass}), ''))
  regdata = 'playerspec["%s" "auto" %d]' % (ourteam, ourport)
  req = urllib2.Request(regurl, regdata)
  try:
    resp = urllib2.urlopen(req).read()
    if str(resp) == str(ourteam):
      return '%s registration success!' % ourteam
    else:
      return '"%s" != "%s"' % (str(resp), str(ourteam))
  except Exception, e:
    logging.exception('Reg Failure at %s ' % regurl)
    return '%s registration FAILURE! (%s)' % (ourteam, str(e))

class Config(object):

  def __init__(self, gamekind=None, turnduration=None, mindecrement=None,
               initacc=None, objective=None, predicate=None, numrounds=None,
               profitfactor=None, otrounds=None):
    """
    A Config object with type enforcement.
    """
    self.gamekind = gamekind
    self.turnduration = int(turnduration)
    self.mindecrement = float(mindecrement)
    self.initacc = float(initacc)
    self.objective = objective
    self.predicate = predicate
    self.numrounds = int(numrounds)
    self.profitfactor = float(profitfactor)
    self.otrounds = int(otrounds)

  @classmethod
  def FromString(cls, input):
    ps = parser.Config.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parse_obj):
    return cls(**parse_obj.asDict())

class Offer(object):
  def __init__(self, offerid, playerid, problemnumber, price):
    self.offerid = int(offerid)
    self.playerid = int(playerid)
    self.problemnumber = int(problemnumber)
    self.price = float(price)
    self.actedon = False
  
  def __str__(self):
    return 'Offer(id=%s, from=%s, problem=%s, price=%s)' % (self.offerid,
                                                            self.playerid,
                                                            self.problemnumber,
                                                            self.price)
  def __repr__(self):
    return str(self)

  def IsGoodBuy(self):
    return relation.relation.break_even(self.problemnumber, 3) < self.price

  def GetReoffer(self, decrement=0.1):
    return 'reoffer[%d %0.8f]' % (self.offerid, self.price - decrement)

  def GetAccept(self):
    self.actedon = True
    return 'accept[ %d ]' % (self.offerid)

  @classmethod
  def GetOfferList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      outputlist.append(cls(*parsedlist[:4]))
      parsedlist = parsedlist[4:]
    return outputlist

class AcceptedChallenge(object):
  def __init__(self, acceptor, offerid, provider, problemnumber, price):
    self.acceptor = int(acceptor)
    self.offerid = int(offerid)
    self.provider = int(provider)
    self.problemnumber = int(problemnumber)
    self.price = float(price)

  def __str__(self):
    return ('AcceptedChallenge(acceptor=%s, offerid=%s, provider=%s,'
            ' problem=%s, price=%s)' % (self.acceptor, self.offerid,
                                        self.provider, self.problemnumber,
                                        self.price))
  def __repr__(self):
    return str(self)

  @classmethod
  def GetAcceptedChallengeList(cls, parsedlist):
    outputlist = []
    while parsedlist:
      outputlist.append(cls(*parsedlist[:5]))
      parsedlist = parsedlist[5:]
    return outputlist


class PlayerContext(object):
  def __init__(self, config=None, their_offered=None,
               our_offered=None, accepted=None, 
               provided=None, playerid=None, balance=None):
    self.their_offered = Offer.GetOfferList(their_offered)
    self.our_offered = Offer.GetOfferList(our_offered)
    self.accepted = AcceptedChallenge.GetAcceptedChallengeList(accepted)
    self.provided = Problem.GetProblemList(provided)
    self.config = config
    self.playerid = int(playerid)
    self.balance = float(balance)

  @classmethod
  def FromString(cls, input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])

  @classmethod
  def FromParsed(cls, parsed):
    return cls(config=Config.FromParsed(parsed.config),
               their_offered=parsed.their_offered,
               our_offered=parsed.our_offered,
               accepted=parsed.accepted, provided=parsed.provided,
               playerid=parsed.playerid, balance=parsed.balance)


class Clause(object):
  def __init__(self, number, list_of_vars):
    self.problemnumber = int(number)
    self.vars = list_of_vars

  def __str__(self):
    return 'Clause(num=%d, vars=%s)' % (self.problemnumber, str(self.vars))

  def __repr__(self):
    return str(self)

  def GetProvideBlob(self):
    return '(%d %s )' % (self.problemnumber, ' '.join(self.vars))

class Problem(object):
  def __init__(self, buyer, list_of_vars, clauselist, challengeid, seller,
               problemnumber, price):
    self.buyer = int(buyer)
    self.vars = list_of_vars
    self.clauses = []
    self.AddClauses(clauselist)
    self.challengeid = int(challengeid)
    self.seller = int(seller)
    self.problemnumber = int(problemnumber)
    self.price = float(price)

  def __str__(self):
    return ('Problem(num=%d, vars=%s, price=%s, seller=%s clauses=%s)'
            % (self.problemnumber, str(self.vars), self.price, self.seller,
               str(self.clauses)))

  def __repr__(self):
    return str(self)

  def AddClauses(self, clauselist):
    for clause in clauselist:
      self.AddClause(Clause(clause[0], clause[1:]))

  def AddClause(self, clause):
    self.clauses.append(clause)

  def GetProvide(self):
    return ('provide[%s %s %d]'
            % (' '.join(self.vars),
                ' '.join([x.GetProvideBlob() for x in self.clauses]),
                self.challengeid))


  def Solve(self):
    logging.info('Solving offer %d' % self.challengeid)

  @classmethod
  def GenerateProblem(cls, problemnumber, degree, offerid):
    p = cls(0, ['v%d' % x for x in range(0, degree)], [], offerid, 0,
            problemnumber, 0)
    for i, j, k in relation.gen.permute3(degree):
      p.AddClause(Clause(problemnumber, ['v%d' % x for x in [i, j, k]]))
    return p

  @classmethod
  def GetProblemList(cls, parsedlist):
    logging.info('Parsing: %s' % parsedlist)
    outputlist = []
    for problem in parsedlist:
      #buyer, list_of_vars, clauselist, challengeid, seller,
      # problemnumner, price
      newp = cls(problem[0], problem[1][0], problem[1][1], problem[2],
                 problem[3], problem[4], problem[5])
      outputlist.append(newp)
    return outputlist

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
