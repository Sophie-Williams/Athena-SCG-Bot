package test.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import scg.game.Game;
import scg.game.LocalPlayerProxy;
import scg.game.Game.Player;
import scg.gen.AcceptedChallenge;
import scg.gen.Config;
import scg.gen.Objective;
import scg.gen.OfferedChallenge;
import scg.gen.PlayerContext;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;
import scg.gen.Predicate;
import scg.gen.Problem;
import scg.gen.ProblemType;
import scg.gen.ProvidedChallenge;

public class TestGameTransactionApplications {

    Config config;
    LocalPlayerProxy player1;
    LocalPlayerProxy player2;
    ArrayList<Integer> playerIds;
    Game game;

    @Before
    public void init() throws Exception{
        config = new Config("CSP", 60, 0.01, 15, new Objective(), new Predicate(), 5, 1.0, 2);
        player1 = new LocalPlayerProxy(new PlayerSpec("Player1", "127.0.0.1", 8001), null);
        player2 = new LocalPlayerProxy(new PlayerSpec("Player2", "127.0.0.1", 8002), null);
        game = new Game(config, player1, player2);
        edu.neu.ccs.demeterf.lib.List<Player> players = game.getPlayersTable();
        playerIds = new ArrayList<Integer>();
        for (Player player : players) {
            playerIds.add(player.getId());
        }
    }

    @Test
    public void offeredTransactionShowsUpInOurOffered() throws Exception{
        int pid = playerIds.get(0);
        PlayerTrans trans = PlayerTrans.parse("playertrans[ " + pid + " offer[(12 13 25) 2.0]  ]");
        trans.applyTransactions(game, false);
        PlayerContext context = game.getPlayerContext(pid, 1);
        List<OfferedChallenge> offered = context.getOurOffered().toJavaList();
        assertEquals(1, offered.size());
        assertEquals(pid, offered.get(0).getChallenger().getId());
        assertEquals(ProblemType.parse("(12 13 25)"), offered.get(0).getPred());
        assertEquals(2.0, offered.get(0).getPrice(), 0.01);

        assertEquals(context.getTheirOffered().length(), 0);
        assertEquals(context.getAccepted().length(), 0);
        assertEquals(context.getBalance(), config.getInitacc());
        assertEquals(context.getProvided().length(), 0);
    }

    @Test
    public void offeredTransactionShowsUpInTheirOffered() throws Exception{
        int pid = playerIds.get(1);
        PlayerTrans trans = PlayerTrans.parse("playertrans[ " + pid + " offer[(12 13 25) 2.0]  ]");
        trans.applyTransactions(game, false);
        PlayerContext context = game.getPlayerContext(playerIds.get(0), 1);
        List<OfferedChallenge> offered = context.getTheirOffered().toJavaList();
        assertEquals(1, offered.size());
        assertEquals(pid, offered.get(0).getChallenger().getId());
        assertEquals(ProblemType.parse("(12 13 25)"), offered.get(0).getPred());
        assertEquals(2.0, offered.get(0).getPrice(), 0.01);

        assertEquals(context.getOurOffered().length(), 0);
        assertEquals(context.getAccepted().length(), 0);
        assertEquals(context.getBalance(), config.getInitacc());
        assertEquals(context.getProvided().length(), 0);
    }

    @Test
    public void testApplyAcceptedTransaction() throws Exception{
        int challenger = playerIds.get(0);
        PlayerTrans offerTrans = PlayerTrans.parse("playertrans[ " + challenger + " offer[(12 13 25) 2.0]  ]");
        offerTrans.applyTransactions(game, false);
        int challengee = playerIds.get(1);
        PlayerContext challengerContext = game.getPlayerContext(challenger, 1);
        int offerchallengeKey = challengerContext.getOurOffered().top().getKey();
        PlayerTrans acceptTrans = PlayerTrans.parse("playertrans[ " + challengee + " accept[" + offerchallengeKey
                + "]  ]");
        acceptTrans.applyTransactions(game, false);

        PlayerContext challengerContext2 = game.getPlayerContext(challenger, 1);
        List<AcceptedChallenge> accepted = challengerContext2.getAccepted().toJavaList();
        assertEquals(1, accepted.size());
        assertEquals(challenger, accepted.get(0).getChallenger().getId());
        assertEquals(challengee, accepted.get(0).getChallengee().getId());
        assertEquals(ProblemType.parse("(12 13 25)"), accepted.get(0).getPred());
        assertEquals(2.0, accepted.get(0).getPrice(), 0.01);

        assertEquals(challengerContext2.getOurOffered().length(), 0);
        assertEquals(challengerContext2.getTheirOffered().length(), 0);
        assertEquals(challengerContext2.getBalance(), config.getInitacc() + accepted.get(0).getPrice());
        assertEquals(challengerContext2.getProvided().length(), 0);

        PlayerContext challengeeContext = game.getPlayerContext(challengee, 1);
        assertEquals(0, challengeeContext.getTheirOffered().length());
        assertEquals(0, challengeeContext.getOurOffered().length());
        assertEquals(0, challengeeContext.getAccepted().length());
        assertEquals(config.getInitacc() - accepted.get(0).getPrice(), challengeeContext.getBalance());

    }

    @Test
    public void testApplyProvideTransaction() throws Exception{
        int challenger = playerIds.get(0);
        PlayerTrans offerTrans = PlayerTrans.parse("playertrans[ " + challenger + " offer[(12 13 25) 2.0]  ]");
        offerTrans.applyTransactions(game, false);

        int challengee = playerIds.get(1);
        PlayerContext challengerContext = game.getPlayerContext(challenger, 1);
        int offerchallengeKey = challengerContext.getOurOffered().top().getKey();
        PlayerTrans acceptTrans = PlayerTrans.parse("playertrans[ " + challengee + " accept[" + offerchallengeKey
                + "]  ]");
        acceptTrans.applyTransactions(game, false);

        String problem = "v1 v2 v3 v4 v5 (12 v1 v2 v3) (13 v2 v3 v4) (25 v1 v2 v5) (12 v1 v5 v3)";
        PlayerTrans provideTrans = PlayerTrans.parse("playertrans[ " + challenger + " provide[" + problem + " "
                + offerchallengeKey + "]  ]");
        provideTrans.applyTransactions(game, false);

        PlayerContext challengerContext2 = game.getPlayerContext(challenger, 1);
        PlayerContext challengeeContext = game.getPlayerContext(challengee, 1);

        ProvidedChallenge providedChallenge = challengeeContext.getProvided().top();
        assertEquals(challenger, providedChallenge.getChallenger().getId());
        assertEquals(challengee, providedChallenge.getChallengee().getId());
        assertEquals(offerchallengeKey, providedChallenge.getKey());
        assertEquals(Problem.parse(problem), providedChallenge.getInstance());
        assertEquals(config.getInitacc() - providedChallenge.getPrice(), challengeeContext.getBalance());
        assertEquals(challengerContext2.getBalance(), config.getInitacc() + providedChallenge.getPrice());
    }

    @Test
    public void testApplySolveTransaction(){
        fail("Not yet implemented");
    }

    @Test
    public void testApplyReofferTransaction(){
        fail("Not yet implemented");
    }

}
