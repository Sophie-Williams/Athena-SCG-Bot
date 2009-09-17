package admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import logging.Logger;
import reg.RegServer;
import scg.Util;
import scg.Constants;
import scg.game.Game;
import scg.game.HistoryFile;
import scg.game.PlayerProxyI;
import scg.gen.Config;
import scg.gen.ParseException;
import scg.gen.PlayerSpec;
import scg.gen.PlayersFile;
import edu.neu.ccs.demeterf.util.CLI;
import edu.neu.ccs.demeterf.lib.Option;

/**  */
public class AdminMain {

    /** Options */
    static final String HELP = "--help", NO_REG = "--noreg", RELATIVE_TIME = "--relative";

    /** Print usage information and Quit */
    static void usage(String err){
        if (err.length() > 0) {
            System.err.println("\n !! " + err + "\n");
        }
        System.err.println(" ** Usage: java AdminMain [Options] <Time> [PlayersFile]\n"
                + "    Options can be:\n"
                + "      --help      : Print this message\n"
                + "      --noreg     : Skip registration, use a preset players file\n"
                + "      --relative  : Use a 'relative' start time instead of an absolute\n"
                + "                     start time. Used for running a quick competition.\n\n"
                + "    Absolute Time (default) is formated as: 'mm/dd/yyyy hh:mm:ss am|pm'.\n"
                + "    Relative Time is an integer; the number of seconds before starting\n\n"
                + "    If 'noreg' is used, the location of a players info file can be\n"
                + "      given... otherwise the default is '" + Constants.PLAYERS_FILE + "'\n");
        System.exit(1);
    }

    /** Run the Admin... */
    public static void main(String[] argArr) throws Exception{
        edu.neu.ccs.demeterf.lib.List<String> split[] = CLI.splitArgs(argArr), options = split[CLI.OPTS], args = split[CLI.ARGS];
        if (options.contains(HELP)) {
            usage("");
        }
        if (args.length() < 1) {
            usage("Not enough manditory arguments");
        }

        String date = args.lookup(0);
        List<PlayerSpec> players;
        Date start;
        Logger logger;
        try {
            if (options.contains(RELATIVE_TIME)) {
                Calendar cal = new GregorianCalendar();
                cal.setTime(Util.now());
                cal.add(Calendar.SECOND, Integer.parseInt(date));
                start = cal.getTime();
            } else {
                start = Util.parseDate(date);
            }
            logger = Logger.text(System.out);
            if (options.contains(NO_REG)) {
                logger.event("Skipping Registration");
                String pfile = args.length() > 1 ? args.lookup(1) : Constants.PLAYERS_FILE;
                players = loadPlayers(pfile);
            } else {
                logger.event("Registering");
                players = RegServer.create(logger).runRegistration(Option.some(start)).toJavaList();
            }
            Util.waitUntil(start);
        } catch (java.text.ParseException pe) {
            usage("Error parsing Date");
            return;
        } catch (Exception e) {
            usage("Exception while starting up: " + e.getMessage());
            return;
        }
        Game game = new Game(loadConfig(Constants.CONFIG_FILE), wrapPlayerSpecs(players));
        logger.event("Opening History file");
        HistoryFile history = new HistoryFile(Constants.HISTORY_FILE_PREFIX, start, Constants.HISTORY_FILE_SUFFIX);
        logger.event("Competition Started");
        game.start(history);
        logger.event("Competition Complete");
    }

    private static List<? extends PlayerProxyI> wrapPlayerSpecs(List<PlayerSpec> playerSpecs){
        List<RemotePlayerProxy> remoteProxies = new ArrayList<RemotePlayerProxy>();
        for (PlayerSpec playerSpec : playerSpecs) {
            remoteProxies.add(new RemotePlayerProxy(playerSpec));
        }
        return remoteProxies;
    }

    /**
     * Load playerSpecs from a file
     */
    public static List<PlayerSpec> loadPlayers(String playerFile) throws ParseException, java.io.IOException{
        return PlayersFile.parse(new java.io.FileInputStream(playerFile)).getPlayerSpecs().toJavaList();
    }

    /**
     * Load Config from a file
     */
    public static Config loadConfig(String configFile) throws ParseException, java.io.IOException{
        return Config.parse(new java.io.FileInputStream(configFile));
    }

}
