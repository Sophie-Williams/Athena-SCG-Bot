package test.game;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import scg.game.Game;
import scg.game.LocalPlayerProxy;
import scg.game.Game.Player;
import scg.gen.Config;
import scg.gen.Objective;
import scg.gen.PlayerContext;
import scg.gen.PlayerID;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;
import scg.gen.Predicate;
import scg.gen.Transaction;
import edu.neu.ccs.demeterf.lib.List;

public class TestGameChecks {

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

    @Test(expected = RuntimeException.class)
    public void onlyCurrentPlayerCanRespond0(){
        // different playerId's
        int pid = playerIds.get(0);
        PlayerContext pc = game.getPlayerContext(pid, 1);
        PlayerTrans pt = new PlayerTrans(new PlayerID(pid + 1), List.<Transaction> create());
        pc.isLegal(pt);
    }

    @Test
    public void onlyCurrentPlayerCanRespond1(){
        // samePlayerId's
        int pid = playerIds.get(0);
        PlayerContext pc = game.getPlayerContext(pid, 1);
        PlayerTrans pt = new PlayerTrans(new PlayerID(pid), List.<Transaction> create());
        pc.isLegal(pt);
    }

    @Test
    /**
     * Offer a derivative that's not offered.
     */
    public void offerNew(){
    }

    @Test
    /**
     * Only buy from others offerings
     */
    public void acceptFromOtherOffers(){

    }

    @Test
    /**
     * Only buy from others offerings
     */
    public void eitherBuyOrReoffer(){

    }

    @Test
    /**
     * Provide All problems that are bought. Provided problems must satisfy the
     * predicate.
     */
    public void provideFromBoughtChallenges(){

    }

    @Test
    /**
     * Solve from provided problems
     */
    public void solveFromProvidedChallenges(){

    }
}
