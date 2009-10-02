#!/usr/bin/env python
import offer
import parser
import problem

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
    else:
      raise Exception('Configuration not found in input string')

  @classmethod
  def FromParsed(cls, parse_obj):
    return cls(**parse_obj.asDict())


class PlayerContext(object):
  def __init__(self, config=None, their_offered=None,
               our_offered=None, accepted=None, 
               provided=None, playerid=None, currentround=None,
               balance=None):
    self.their_offered = offer.Offer.GetOfferList(their_offered)
    self.their_offered.sort()
    self.our_offered = offer.Offer.GetOfferList(our_offered)
    self.accepted = offer.AcceptedChallenge.GetAcceptedChallengeList(accepted)
    self.provided = problem.Problem.GetProblemList(provided)
    self.config = config
    self.playerid = int(playerid)
    self.currentround = int(currentround)
    self.balance = float(balance)
    self.endbalance = float(self.balance)

  @classmethod
  def FromString(cls, input):
    ps = parser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])
    else:
      raise Exception('PlayerContext not found in input string')

  @classmethod
  def FromParsed(cls, parsed):
    return cls(config=Config.FromParsed(parsed.config),
               their_offered=parsed.their_offered,
               our_offered=parsed.our_offered,
               accepted=parsed.accepted, provided=parsed.provided,
               playerid=parsed.playerid, currentround=parsed.currentround,
               balance=parsed.balance)
