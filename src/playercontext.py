#!/usr/bin/env python
"""Game context and configuration classes."""

import offer
import cspparser
import problem

class Config(object):
  """Represents a CSP configuration object."""

  def __init__(self, gamekind=None, turnduration=None, mindecrement=None,
               initacc=None, maxProposals=0, minProposals=0,
               minPropositions=0, objective=None, predicate=None,
               numrounds=None, profitfactor=None, otrounds=None,
               maxClauses=0, hasSecrets=False, secretRatio=0):
    """A Config object with type enforcement."""
    self.gamekind = gamekind
    self.turnduration = int(turnduration)
    self.mindecrement = float(mindecrement)
    self.initacc = float(initacc)
    self.maxoffers = self.maxproposals = int(maxProposals)
    self.minproposals = int(minProposals)
    self.minpropositions = int(minPropositions)
    self.maxclauses = int(maxClauses)
    self.objective = objective
    self.predicate = predicate
    self.numrounds = int(numrounds)
    self.profitfactor = float(profitfactor)
    self.otrounds = int(otrounds)
    if hasSecrets == 'true':
      self.hassecrets = True
    else:
      self.hassecrets = False
    self.secretratio = float(secretRatio)

  @classmethod
  def FromString(cls, input):
    """Get a config object from an input string."""
    ps = cspparser.Config.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])
    else:
      raise Exception('Configuration not found in input string')

  @classmethod
  def FromParsed(cls, parse_obj):
    """Get a config object from the parser output."""
    return cls(**parse_obj.asDict())


class PlayerContext(object):
  """Represent a CSP PlayerContext object from the administrator."""

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
    """Get a playercontext from the inputstring."""
    ps = cspparser.PlayerContext.searchString(input)
    if ps:
      return cls.FromParsed(ps[0])
    else:
      raise Exception('PlayerContext not found in input string')

  @classmethod
  def FromParsed(cls, parsed):
    """Get a playercontext from the parser."""
    return cls(config=Config.FromParsed(parsed.config),
               their_offered=parsed.their_offered,
               our_offered=parsed.our_offered,
               accepted=parsed.accepted, provided=parsed.provided,
               playerid=parsed.playerid, currentround=parsed.currentround,
               balance=parsed.balance)
