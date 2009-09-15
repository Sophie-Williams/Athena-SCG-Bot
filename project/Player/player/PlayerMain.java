package player;

import java.io.IOException;
import logging.Logger;
import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.util.CLI;

/** Main Player Class, Registers a player, and sets up a server */
public class PlayerMain {

    /** Options */
    static final String HELP = "--help",
                        NO_REG = "--noreg";
   
    /** Print usage information and Quit */
    static void usage(String err){
        if(err.length()>0)
            System.err.println("\n !! "+err+"\n");
        System.err.println(" ** Usage: PlayerMain <PlayerPort> <ServerAddr> "
                + "<TeamName> <Password>\n"
                + "    Options can be:\n"
                + "      --help      : Print this message\n"
                + "      --noreg     : Skip registration, assume the Admin knows where you\n"
                + "                      are.  If you use noreg then you only need to provide\n"
                + "                      your PlayerPort #.\n\n"
                + "  * NOTE: For a real competition you cannot use 'noreg' and *must* give\n"
                + "           all arguments.\n\n"
                + "    Port : the socket port the Player listens on. Should be an integer > 1024\n"
                + "    ServerAddr : Given by the Course Staff. This may be a host name/URL or\n"
                + "                   an IP Address.\n"
                + "    TeamName : must be the same as when you pre-registered.\n"
                + "    Password : same as when you pre-registered.\n\n"
                + "  Typical Usages will be:\n"
                + "      java player.PlayerMain 7000 aurail.ccs.neu.edu \"MyTeam\" \"MyPass\"\n"
                + "      java player.PlayerMain 7000 --noreg\n");
        System.exit(err.length()>0?1:0);
    }
    
    /** Run the Player... */
    public static void main(String[] argArr) throws Exception{
        List<String> split[] = CLI.splitArgs(argArr),
                     options = split[CLI.OPTS],
                     args = split[CLI.ARGS];
        if(options.contains(HELP))usage("");
        
        // Is registration needed?
        boolean doreg = !options.contains(NO_REG);
        if(args.length() < 1 || (doreg && args.length() != 4)){
            usage("Not enough manditory arguments");
            return;
        }
        // Player's Port Number is first
        int port = Integer.parseInt(args.lookup(0));
        
        // Other arguments are just for registration
        String serve = doreg?args.lookup(1):"",
               team = doreg?args.lookup(2):"No Name",
               pass = doreg?args.lookup(3):"";
               
        // Create a new Logger
        Logger log = Logger.text(System.out, scg.Util.logFileName("player"));

        // Register if Needed
        if(doreg){
            if(!new Register(serve,log).doReg(team, pass, port))
                System.exit(1);
            log.event("Player '" + team + "' Registered Successfully");
        }
        // Start the Player Server... it will wait for its turn
        log.notify("Player Started for Team '" + team + "'");
        try {
            PlayerServer.run(port, log);
        } catch (IOException ie) {
            log.error("IOException: " + ie.getMessage());
        }
        log.notify("Player Shutdown");
    }
}
