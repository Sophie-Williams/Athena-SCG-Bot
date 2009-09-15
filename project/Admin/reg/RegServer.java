package reg;

import edu.neu.ccs.demeterf.http.classes.*;
import edu.neu.ccs.demeterf.http.server.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import logging.Logger;
import scg.Util;
import scg.Constants;
import scg.gen.ParseException;
import scg.gen.PasswordFile;
import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.lib.Map;

/**
 * Player/Competition Registration Server. Waits for registration requests, and
 * responds accordingly.
 * <p>
 * The registration protocol is as follows:
 * <ul>
 * <li>The Palyer sends a {@link gen.PlayerSpec PlayerSpec} as the body of a
 * POST to the RegServer, including a URL argument.</li>
 * <li>The full URL request will be: <quote>
 * <tt>RegServer.PATH_ENTRY+"?"+RegServer.PASS_URL_ARG+"="+PASSWORD</tt>
 *        </quote>
 *        where <tt>PASSWORD</tt> is the Player/Team password as registered with
 *        the Teaching Staff (to be described separately).</li>
 *        
 *     <li>If registration succeeds, an HTTP Response with code 200 (OK) will be
 *         sent back, with a short description "Player '...' has been Registered".<li>
 *  </ul>
 *  </p>
 */
@Server
public class RegServer {

    /** Listening (incoming) Port */
    @Port
    public static final int PORT = Constants.REG_PORT;
    
    /** Password List */
    private final Map<String, String> passwds;
    /** Currently registered Players */
    private List<PlayerSpec> players = List.create();
    /** Logger for the Registration Process */
    private final Logger log;

    /**
     * Create a RegServer with the given Passwords. Players/Teams will submit a
     * Passsword, which we will 'encrypt' and store to verify the registration
     * for competitions.
     */
    private RegServer(Map<String, String> ps, Logger l) {
        passwds = ps;
        log = l;
    }

    /**
     * Create a RegServer with the given Passwords. Players/Teams will submit a
     * Passsword, which we will 'encrypt' and store to verify the registration
     * for competitions.
     */
    public static RegServer create(Map<String, String> ps, Logger l){
        return new RegServer(ps, l);
    }

    /**
     * Create a RegServer with the given Logger. Passwords will be loaded from
     * the usual File.
     */
    public static RegServer create(Logger l) throws ParseException, IOException{
        PasswordFile file = PasswordFile.parse(new FileInputStream(Constants.PASS_FILE));
        return create(file.getPasswordMap(), l);
    }

    /** Main URL handler for the RegServer */
    @Path(Constants.REG_PATH_ENTRY)
    public HTTPResp registerResp(HTTPReq req, Socket sock){
        log.event("Registration Request from: "+sock.getInetAddress());
        synchronized (players) {
            try {
                PlayerSpec spec = PlayerSpec.parse(req.getBodyString());
                if (!passwds.containsKey(spec.getName())) {
                    return HTTPResp.error(log.error("Unknown Team Name '" + spec.getName() + "'"));
                }
                if (players.contains(spec.sameNamePred())) {
                    return HTTPResp.error(log.error("Player '" + spec.getName() + "' Already Registered"));
                }

                Map<String, String> args = req.urlArgs();
                if (!args.containsKey(Constants.PASS_URL_ARG)) {
                    return HTTPResp.error(log.error("No Password Given"));
                }
                if (verify(passwds.get(spec.getName()), args.get(Constants.PASS_URL_ARG))) {
                    if (spec.getAddress().equals(Constants.REG_AUTO)) {
                        spec = spec.changeAddress(sock.getInetAddress().getHostAddress());
                    }
                    players = players.push(spec);
                    log.notify("Registering '" + spec.getName() + "' at " + spec.getAddress() + ":" + spec.getPort());
                    return HTTPResp.textResponse(log.event("Player '" + spec.getName() + "' has been Registered"));
                }
                return HTTPResp.error(log.error("Password Does not Match Player '" + spec.getName() + "'"));
            } catch (ParseException pe) {
                return HTTPResp.error(log.error("Error Parsing Registration (gen.PlayerSpec)"));
            }
        }
    }

    /** Encrypt and Verify the given Password agains the known 'Hash' */
    public static boolean verify(String known, String unknownPlain){
        return known.equals(Util.encrypt(unknownPlain));
    }

    @Path()
    public HTTPResp defaultResp(HTTPReq req){
        return HTTPResp.error("Unhandled Server Path '" + req.trimmedUrl() + "'");
    }

    /** Run the registration server, until the competition starts. */
    public List<PlayerSpec> runRegistration(String startDate) throws IOException, java.text.ParseException{
        return runRegistration(Util.parseDate(startDate));
    }

    /** Run the registration server, until the competition starts. */
    public List<PlayerSpec> runRegistration(Date start) throws IOException{
        ServerThread server = Factory.create(this);
        log.event("Registration Opened");
        log.notify("Competition will start at: " + Util.printDate(start));
        Util.waitUntil(start);
        server.shutdown();
        log.event("Registration Closed");
        return players;
    }

    /** Main Test for the Server. */
    public static void main(String[] args) throws Exception{
        if (args.length != 1) {
            System.err.println(" !! Usage: java RegServer <Comp Start>");
        }
        String start = args.length > 0 ? args[0] : "7/5/2009 2:40:00 PM EDT";
        RegServer server = RegServer.create(Logger.text(System.err));
        System.err.println("\nRegistered Teams:\n" + server.runRegistration(start).toString());
        System.err.println("\n ******* DONE : " + Util.printDate(Util.now()) + " ************");
    }
}
