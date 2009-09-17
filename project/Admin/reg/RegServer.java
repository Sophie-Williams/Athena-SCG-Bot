package reg;

import edu.neu.ccs.demeterf.http.classes.*;
import edu.neu.ccs.demeterf.http.server.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
import edu.neu.ccs.demeterf.lib.Option;

/**
 * Player/Competition Registration Server. Waits for registration requests, and
 * responds accordingly.
 * <p>
 * The registration protocol is as follows:
 * <ul>
 *    <li>The Player sends a {@link scg.gen.PlayerSpec PlayerSpec} as the body of a
 *        POST to the RegServer, including a URL argument.</li>
 *    <li>The full URL request will be: 
 *        <quote>
 *           <tt>RegServer.PATH_ENTRY+"?"+RegServer.PASS_URL_ARG+"="+PASSWORD</tt>
 *        </quote>
 *        where <tt>PASSWORD</tt> is the Player/Team password as registered with
 *        the Teaching Staff (to be described separately).</li>
 *    <li>If registration succeeds, an HTTP Response with code 200 (OK) will be
 *        sent back, with a short the Player's name as a body.<li>
 *  </ul>
 *  </p>
 */
@Server
public class RegServer {
    
    /** Listening (incoming) Port */
    @Port
    private int port;
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
    private RegServer(int p, Map<String, String> ps, Logger l) {
        port = p;
        passwds = ps;
        log = l;
    }
    
    /** Create a RegServer at the given Port. */
    public static RegServer create(Logger l) throws ParseException, IOException{
        return create(Constants.DEF_REG_PORT,l);
    }
    
    /** Create a RegServer with the given Passwords at the Default Port. */
    public static RegServer create(Map<String, String> ps, Logger l){
        return create(Constants.DEF_REG_PORT,ps,l);
    }

    /**
     * Create a RegServer at the given Port, with the given Passwords. Players/Teams
     * will submit a Passsword, which we will 'encrypt' and store to verify the
     * registration for competitions.
     */
    public static RegServer create(int p, Map<String, String> ps, Logger l){
        return new RegServer(p, ps, l);
    }

    /**
     * Create a RegServer with the given Logger. Passwords will be loaded from
     * the usual File.
     */
    public static RegServer create(int port, Logger l) throws ParseException, IOException{
        PasswordFile file = PasswordFile.parse(new FileInputStream(Constants.PASS_FILE));
        return create(port, file.getPasswordMap(), l);
    }

    /** Add the Server Header for the Admin */
    static HTTPResp addServer(HTTPResp res){
        return res.addHeader(Constants.SERVER_KEY, Constants.ADMIN_SERVER_NAME);
    }
    /** Create an Error response for the Player */
    static HTTPResp errResp(String body){ return addServer(HTTPResp.error(body)); }
    /** Create an Text response for the Player */
    static HTTPResp okResp(String body){ return addServer(HTTPResp.textResponse(body)); }

    /** Main URL handler for the RegServer */
    @Path(Constants.REG_PATH_ENTRY)
    public HTTPResp registerResp(HTTPReq req, Socket sock){
        log.event("Registration Request from: "+sock.getInetAddress());
        synchronized (players) {
            try {
                PlayerSpec spec = PlayerSpec.parse(req.getBodyString());
                if (!passwds.containsKey(spec.getName())) {
                    return errResp(log.error("Unknown Team Name '" + spec.getName() + "'"));
                }
                if (players.contains(spec.sameNamePred())) {
                    return errResp(log.error("Player '" + spec.getName() + "' Already Registered"));
                }

                Map<String, String> args = req.urlArgs();
                if (!args.containsKey(Constants.PASS_URL_ARG)) {
                    return errResp(log.error("No Password Given"));
                }
                if (verify(passwds.get(spec.getName()), args.get(Constants.PASS_URL_ARG))) {
                    if (spec.getAddress().equals(Constants.REG_AUTO)) {
                        spec = spec.changeAddress(sock.getInetAddress().getHostAddress());
                    }
                    players = players.push(spec);
                    log.notify("Registering '" + spec.getName() + "' at " + spec.getAddress() + ":" + spec.getPort());
                    log.event("Player '" + spec.getName() + "' has been Registered");
                    return okResp(spec.getName());
                }
                return errResp(log.error("Password Does not Match Player '" + spec.getName() + "'"));
            } catch (ParseException pe) {
                return errResp(log.error("Error Parsing Registration (gen.PlayerSpec)"));
            }
        }
    }

    /** Encrypt and Verify the given Password agains the known 'Hash' */
    public static boolean verify(String known, String unknownPlain){
        return known.equals(Util.encrypt(unknownPlain));
    }

    @Path()
    public HTTPResp defaultResp(HTTPReq req){
        return errResp("Unhandled Server Path '" + req.trimmedUrl() + "'");
    }

    /** Run the registration server, until the competition starts. */
    public List<PlayerSpec> runRegistration(String startDate) throws IOException, java.text.ParseException{
        return runRegistration(Option.some(Util.parseDate(startDate)));
    }

    /** Run the registration server, until the competition starts. */
    public List<PlayerSpec> runRegistration(Option<Date> start) throws IOException{
        ServerThread server = Factory.create(this);
        log.event("Registration Opened");
        if(start.isSome()){
            log.notify("Competition will start at: " + Util.printDate(start.inner()));
            Util.waitUntil(start.inner());
        }else{
            System.err.print("Hit Enter to Close Reg: ");
            System.in.read();
        }
        server.shutdown();
        log.event("Registration Closed");
        return players;
    }

    /** Just the Registration */
    public static void main(String[] argStr) throws Exception{
        List<String> args = List.create(argStr); 
        if(args.contains("--help") || args.isEmpty()){
            System.err.println(" ** Usage: java reg.RegServer <Port> [CompStartTime]");
            System.err.println(" Examples: java reg.RegServer 8000 '9/15/2009 3:00:00 pm'");
            System.err.println("           java reg.RegServer 8000");
            return;
        }
        int port = Integer.parseInt(args.top());
        Logger log = Logger.text(System.err,Util.logFileName("reg"));
        RegServer server = RegServer.create(port,log);
        List<PlayerSpec> players = server.runRegistration(args.length()>1?
                        Option.some(Util.parseDate(args.lookup(1))):Option.<Date>none());
        
        try{
            PrintStream out = new PrintStream(new FileOutputStream(Constants.PLAYERS_FILE));
            out.print(players.toString("\n",""));
            out.close();
        }catch(IOException e){
            log.error("Could not write Players file: "+e);
        }   
        System.err.println("\n ******* DONE : " + Util.printDate(Util.now()) + " ************");
    }
}
