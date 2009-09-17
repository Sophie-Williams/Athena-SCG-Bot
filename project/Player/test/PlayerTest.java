package test;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

import java.io.FileInputStream;

import player.PlayerServer;
import scg.gen.PlayerContext;

/** A Little Player Test */
public class PlayerTest {

    /** Where the PlayerServer will be */
    static final String TEST_SERVER = "127.0.0.1";

    static void pe(String s){ System.err.print(s); }
    static void p(String s){ System.out.print(s); }

    /** Simple Test Main */
    public static void main(String[] args) throws Exception{
        if (args.length != 1) {
            pe(" ! Usage: java PlayerTest <Context File>\n");
            return;
        }

        // Take a PlayerContext file from the command line
        String file =  args[0];
        // Or from a known location
        // String file = "files/context.txt";
        
        // Parse the context, generate the corresponding request
        PlayerContext ctx = PlayerContext.parse(new FileInputStream(file));
        // This request would usually come from the Administrator
        HTTPReq req = HTTPReq.Post(PlayerServer.EntryPath, ctx.toString());
        // See what the Player (currently waiting) does with the context
        HTTPResp res = req.send(TEST_SERVER, PlayerServer.DEFAULT_PORT);
        // Display the Player's transactions
        p(" Result: \n" + res.getBodyString());
    }
}
