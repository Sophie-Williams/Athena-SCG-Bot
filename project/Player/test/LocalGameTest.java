package test;
import logging.Logger;
import player.PlayerServer;
import scg.*;
import scg.game.*;
import scg.gen.*;
import edu.neu.ccs.demeterf.lib.List;

/** Simple setup for a local (single machine) run without using network sockets */
public class LocalGameTest {

    public static void main(String[] args) throws Exception{
        // 0) A bit of configuration
        final int numRounds  = 3,
                  numPlayers = 2;
        double initialAccnt = 5.0;
        
        
        // 1) Create a configuration
        Config config =
            // By Hand...
            new Config("CSP", 60, 0.01, initialAccnt,
                    new Objective(), new Predicate(), numRounds, 1.0, 2);
            // Or from a file...
            //   Config.parse(new java.io.FileInputStream(Constants.CONFIG_FILE));

        // 2) Create some players
        final Logger logger = Logger.text(System.out);
        List<PlayerProxy> players = List.buildlist(new List.Build<PlayerProxy>(){
            public PlayerProxy build(int i){
                return new LocalPlayerProxy(new PlayerSpec("Player"+i, "", 0), new PlayerServer(logger));
            }
        }, numPlayers);
        
        // 3) Create a Game instance
        Game game = new Game(config, players.toJavaList());

        // 4) Create a History file
        HistoryFile history = new HistoryFile("history", Util.now(), ".txt");

        // 5) Run the competition
        game.start(history);
    }
}
