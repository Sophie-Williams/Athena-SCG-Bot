#!/usr/bin/env python
import unittest
import cspparser
import playercontext

class cspparserTestCase(unittest.TestCase):

  def test_config(self):
    s = '''config[
        gamekind: "CSP"
        turnduration: 60
        mindecrement: 0.01
        initacc: 100.0
        maxProposals: 5
        minProposals: 1
        minPropositions: 2
        objective: []
        predicate: SlowPitch [2000]
        numrounds: 10000
        profitfactor: 1.0
        otrounds: 2
        hasSecrets: true
        secretRatio: 0.4
        ]'''
    cfg = cspparser.Config.searchString(s)[0]
    self.assertEqual(cfg['maxProposals'], '5')
    self.assertEqual(cfg['minProposals'], '1')
    self.assertEqual(cfg['minPropositions'], '2')
    self.assertEqual(cfg['gamekind'], 'CSP')
    self.assertEqual(cfg['turnduration'], '60')
    self.assertEqual(cfg['initacc'], '100.0')
    self.assertEqual(cfg['predicate']['maxClauses'], '2000')
    self.assertEqual(cfg['objective'], '[]')
    self.assertEqual(cfg['numrounds'], '10000')
    self.assertEqual(cfg['profitfactor'], '1.0')
    self.assertEqual(cfg['otrounds'], '2')
    self.assertEqual(cfg['hasSecrets'], 'true')
    self.assertEqual(cfg['secretRatio'], '0.4')
    g_cfg = playercontext.Config.FromParsed(cfg)
    self.assertTrue(g_cfg is not None)

  def test_context(self):
    s = '''context[
        config[
        gamekind: "CSP"
        turnduration: 60
        mindecrement: 0.01
        initacc: 100.0
        maxProposals: 5
        minProposals: 1
        minPropositions: 2
        objective: []
        predicate: SlowPitch [2000]
        numrounds: 10000
        profitfactor: 1.0
        otrounds: 2
        hasSecrets: true
        secretRatio: 0.4
        ]
        101
        100.0
        1
        ()
        ()
        ()
        ()
    ]'''
    pctx = cspparser.PlayerContext.searchString(s)[0]
    self.assertTrue(len(pctx) > 0)
    self.assertEqual(pctx['gamekind'], 'CSP')
    self.assertEqual(pctx['turnduration'], '60')
    self.assertEqual(pctx['mindecrement'], '0.01')
    self.assertEqual(pctx['initacc'], '100.0')
    self.assertEqual(pctx['maxProposals'], '5')
    self.assertEqual(pctx['objective'], '[]')
    self.assertEqual(pctx['predicate']['maxClauses'], '2000')
    self.assertEqual(pctx['numrounds'], '10000')
    self.assertEqual(pctx['otrounds'], '2')
    self.assertEqual(pctx['playerid'], '101')
    self.assertEqual(pctx['balance'], '100.0')
    self.assertEqual(len(pctx['our_offered']), 0)
    self.assertEqual(len(pctx['their_offered']), 0)
    self.assertEqual(len(pctx['accepted']), 0)
    self.assertEqual(len(pctx['provided']), 0)

if __name__ == '__main__':
  unittest.main()
