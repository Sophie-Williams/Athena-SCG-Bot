package player;

import edu.neu.ccs.demeterf.http.classes.*;
import edu.neu.ccs.demeterf.http.server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import logging.Logger;
import scg.Util;
import scg.gen.PlayerContext;
import edu.neu.ccs.demeterf.lib.*;

/** Server class for the player. Dispatches to HTTP methods with context. */
@Server
public class PlayerServer {

    /** Path the player server listens for */
    public static final String EntryPath = "/player";
    /** Default port for the Player */
    public static final int DEFAULT_PORT = 8000;

    /** Port number for this player instance */
    @Port
    private final int port;
    /** logger instance for the player */
    private final Logger log;
   
    public PlayerServer(Logger l){ this(DEFAULT_PORT, l); }
    public PlayerServer(int p, Logger l){ port = p; log = l; }

    /** Handle an Admin request at the EntryPath */
    @Path(EntryPath)
    public HTTPResp playerResponse(HTTPReq req){
        try {
            // Get the player context from the body of the request
            PlayerContext pContext = PlayerContext.parse(req.getBodyString());
            // Run the Player
            return createResponse(pContext);
        } catch (Exception e) {
            // Error Creating the Player's Context
            log.error("Exception: "+e.getMessage());
            log.error("Request Body:\n"+req.getBodyString()+"\n");
            return HTTPResp.error(""+e);
        }
    }

    /** Default Handler for other paths */
    @Path
    public HTTPResp defaultResponse(){
        return HTTPResp.error("Unknown Request");
    }

    /** Formulate a player response for a given PlyerContext */
    public HTTPResp createResponse(PlayerContext ctx){
        return HTTPResp.textResponse(Player.create(ctx, log).play().toString());
    }

    /** Clear message for Linux terminals */ 
    static String clearer = List.create(0x1b,0x5b,0x48,0x1b,0x5b,0x32,0x4a,0x00)
    .map(new List.Map<Integer, Character>(){
        public Character map(Integer i){ return (char)(int)i; }
    }).toString("","");
    
    /** Main method to run the PlayerServer. Will be called by Player Main. */
    public static void run(int port, Logger log) throws IOException{
        // Create a new Server
        ServerThread server = Factory.create(new PlayerServer(port,log));
        
        // Buffer text from the terminal
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String inpt = "";
        do {
            System.out.print("\n ** Type 'exit' to shutdown: ");
            System.out.flush();
            inpt = input.readLine().trim();
            // Respond to (limited) input commands
            if(inpt.equals("clear"))
                System.err.print(clearer);
        } while (!inpt.equals("exit"));
        log.event("Shutting down Player Server");
        // Kill the PlayerServer thread
        server.shutdown();
    }
}
