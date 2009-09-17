package player;

import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

import logging.Logger;
import scg.Constants;
import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.lib.Map;
import edu.neu.ccs.demeterf.util.CLI;

/** Register a Player with the Administrator */
public class Register {

    /** The Registration Server Address, e.g., "blaster.ccs.neu.edu" */
    final String regServer;
    /** The Registration Server Port */
    final int regPort;
    /** A logger instance */
    final Logger log;

    /** Construct a PlayerMain*/
    Register(String reg, Logger l) {
        if(reg.contains(":")){
            int idx = reg.indexOf(":"); 
            regServer = reg.substring(0,idx);
            regPort = Integer.parseInt(reg.substring(idx+1));
        }else{
            regServer = reg;
            regPort = Constants.DEF_REG_PORT;
        }
        log = l;
    }

    /** Print usage information and Quit */
    static void usage(String err){
        System.err.println("\n !! "+err+"\n\n"
                + " ** usage: java Register <PlayerPort> <RegServerAddr> "
                + "<TeamName> <Password>\n\n"
                + " Typical Usage(s) will be:\n"
                + "    java player.Register 7000 aurail.ccs.neu.edu:8000 \"MyTeam\" \"MyPass\"\n"
                + "    java player.Register 7000 aurail.ccs.neu.edu \"MyTeam\" \"MyPass\"\n");
        System.exit(1);
    }
    
    /** Register a Player... */
    public static void main(String[] argArr) throws Exception{
        List<String> args = CLI.splitArgs(argArr)[CLI.ARGS];
        if(args.length() != 4){
            usage("Not enough manditory arguments");
            return;
        }
        // Port the Player will be waiting on
        int port = Integer.parseInt(args.lookup(0));
        
        String regserve = args.lookup(1),
               teamName = args.lookup(2),
               pass = args.lookup(3);
        
        // Setup a logger to StdOut, and a file
        Logger log = Logger.text(System.out, scg.Util.logFileName("player"));

        // New register instance
        Register reg = new Register(regserve, log);
        // If registration fails, just exit
        if(!reg.doReg(teamName, pass, port)){
            log.shutdown();
            System.exit(1);    
        }
        // Otherwise, Success!
        log.event("Player '" + teamName + "' Registered Successfully");
        log.shutdown();
    }

    /** Register this Player with the Administrator's registration server */
    public HTTPResp register(String name, String pass, int port) throws Exception{
        String URL = Constants.REG_PATH_ENTRY + "?" + Constants.PASS_URL_ARG + "=" + pass;
        HTTPResp res = HTTPReq.Post(URL, "" + new PlayerSpec(name, Constants.REG_AUTO, port))
        .send(regServer, regPort);
        return res;
    }

    /** Try to register the player, and check the response */
    boolean doReg(String team, String pass, int port){
        try {
            // Check the registration 
            HTTPResp res = register(team, pass, port);
            // Error response
            if (res.isError()){
                log.error("Couldn't Register Team '" + team + "' with Password '" + pass + "'");
                Map<String,String> headers = res.getHeaders();
                // See if the Server identified itself...
                if(headers.containsKey(Constants.SERVER_KEY)){
                    String server = headers.get(Constants.SERVER_KEY);
                    if(server.equals(Constants.ADMIN_SERVER_NAME))
                        log.error("Reason: " + res.getBody());
                    else
                        log.error("Unknown Server Type: " + server);
                }
                return false;
            }
            return (res.isOK() && res.getBodyString().equals(team));
        } catch (Exception e) {
            // Exception means the socket was bad
            log.error("Error Registering Player '" + team + "'");
            log.error("Reason: " + e);
            return false;
        }
    }
}
