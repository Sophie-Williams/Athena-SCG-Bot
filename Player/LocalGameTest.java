import logging.Logger;
import player.PlayerServer;
import scg.Util;
import scg.game.Game;
import scg.game.HistoryFile;
import scg.game.LocalPlayerProxy;
import scg.game.PlayerProxyI;
import scg.gen.Config;
import scg.gen.Objective;
import scg.gen.PlayerSpec;
import scg.gen.Predicate;

public class LocalGameTest {

    public static void main(String[] args) throws Exception{

        // Step1: Create a configuration object
        Config config = new Config("CSP", 60, 0.01, 15, new Objective(), new Predicate(), 5, 1.0, 2);
        // Config config = Config.parse(new java.io.FileInputStream("<config
        // file path>"));

        // Step2: Create contestants
        Logger player1Logger = Logger.text(System.out);
        PlayerServer player1 = new PlayerServer(player1Logger);
        Logger player2Logger = Logger.text(System.out);
        PlayerServer player2 = new PlayerServer(player2Logger);
        PlayerProxyI player1Proxy = new LocalPlayerProxy(new PlayerSpec("Player1", "", 0), player1);
        PlayerProxyI player2Proxy = new LocalPlayerProxy(new PlayerSpec("Player2", "", 0), player2);
        Game game = new Game(config, player1Proxy, player2Proxy);

        // Step3: create History
        HistoryFile history = new HistoryFile("history", Util.now(), ".txt");

        // Step4: run the game
        game.start(history);
    }
}
