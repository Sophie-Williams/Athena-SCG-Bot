package prereg;

import edu.neu.ccs.demeterf.http.classes.*;
import edu.neu.ccs.demeterf.http.server.*;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import logging.Logger;
import scg.Util;
import scg.Constants;
import scg.gen.ParseException;
import scg.gen.PasswordEntry;
import scg.gen.PasswordFile;
import scg.gen.TeamFile;
import scg.gen.TeamSpec;
import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.lib.Map;
import edu.neu.ccs.demeterf.lib.Option;

/**
 * Pre Registration Server. Accepts browser "form" for registering Team Names
 * and Passwords.
 */
@Server()
public class PreRegServer {

    /** Path the RegServer Listens On */
    public static final String PATH_ENTRY = "/preregister";
    /** Simple Status Path */
    public static final String STATUS = "/status";

    /** Form Arugument Names */
    private static final String FORM_TEAM = "teamname";
    private static final String FORM_PASS = "pass";
    private static final String FORM_MEMBERS = "members";

    /** Listening (incoming) Port */
    @Port()
    private int port;
    /** Currently registered Teams */
    private List<TeamSpec> teams = List.create();
    /** Logger for the Registration Process */
    private final Logger log;

    /**
     * Create a PreRegServer with the given Teams. Pre-registration sets
     * Player/Team Passwords for future competitions.
     */
    private PreRegServer(int p, List<TeamSpec> tms, Logger l) {
        port = p;
        teams = tms;
        log = l;
    }

    /**  */
    public static PreRegServer create(int p, List<TeamSpec> tms, Logger l){
        return new PreRegServer(p, tms, l);
    }

    /**  */
    public static PreRegServer create(int p, Logger l) throws IOException, ParseException{
        File teamFile = new File(Constants.PRETEAM_FILE);
        if(!teamFile.canWrite())
            teamFile.createNewFile();
        List<TeamSpec> ts = TeamFile.parse(new FileInputStream(Constants.PRETEAM_FILE)).getTeams();
        return new PreRegServer(p, ts, l);
    }

    /** Add the Server Header for the Admin */
    static HTTPResp addServer(HTTPResp res){
        return res.addHeader(Constants.SERVER_KEY, Constants.ADMIN_SERVER_NAME);
    }
    /** Create an Error HTML response for the browser */
    static HTTPResp errResp(String body){ return addServer(HTTPResp.htmlError(errorPage(body))); }
    /** Create an OK HTML response for the browser */
    static HTTPResp okResp(String body){ return addServer(HTTPResp.htmlResponse(body)); }
    
    /** Main URL handler for the RegServer */
    @Path(PATH_ENTRY)
    public HTTPResp registerResp(HTTPReq req, Socket sock){
        log.event("Pre-registration Request from: "+sock.getInetAddress());
        synchronized (teams) {
            Map<String, String> map = req.bodyArgs().transformValues(new List.Map<String, String>() {
                public String map(String a){ return Util.decodeURL(a); }
            });
            if (map.containsKey(FORM_TEAM) && map.containsKey(FORM_PASS) && map.containsKey(FORM_MEMBERS)) {
                TeamSpec newSpec = TeamSpec.create(map.get(FORM_TEAM), map.get(FORM_PASS), map.get(FORM_MEMBERS));
                if (teams.contains(newSpec.sameNamePred())) {
                    return errResp(log.error(htmlTeam(newSpec.getName()) + " Already Exists"));
                }
                teams = teams.push(newSpec);
                log.event("Flushing Teams/Password Files");
                try {
                    new TeamFile(teams).write(Constants.PRETEAM_FILE);
                    log.event("Teams File Flushed");
                    new PasswordFile(teams.map(new List.Map<TeamSpec, PasswordEntry>() {

                        @Override
                        public PasswordEntry map(TeamSpec s){
                            return new PasswordEntry(s.getName(), s.getPasshash());
                        }
                    })).write(Constants.PREPASS_FILE);
                    log.event("Password File Flushed");
                } catch (Exception e) {
                    log.error("Unable to Save one of the files");
                }
                return okResp(htmlPage("Success",
                        "div{ textalign:center;width:400px;height:200px;border:solid blue 1px'",
                        wrap(wrap(wrap("Success","h3")+"<br/><br/>"+
                                wrap(log.event(htmlTeam(newSpec.getName()) + " is Registered"),"h3"),
                                "div"),"center")));
            }
            return errResp(log.error("Missing Form Arguments"));
        }
    }

    /** Main URL handler for the RegServer */
    @Path(STATUS)
    public HTTPResp statusResp(HTTPReq req, Socket sock){
        log.event("Status Request from: "+sock.getInetAddress());
        synchronized (teams) {
            return okResp(
                    htmlPage("PreReg Status","table,td,th{ padding:5px;border:1px solid blue; }",
                            wrap(wrap("Registered Teams","h3")+
                                    wrap(wrap(
                                            wrap("#","th")+
                                            wrap("Name","th")+
                                            wrap("Hash","th")+
                                            wrap("Members","th"),"tr")+
                                            teams.reverse().fold(new List.Fold<TeamSpec, String>(){
                                                int count = 0;
                                                public String fold(TeamSpec t, String r){
                                                    return r+wrap(
                                                            wrap(""+(++count),"td")+
                                                            wrap(t.getName(),"td")+
                                                            wrap(t.getPasshash(),"td")+
                                                            wrap(t.getPlayers().toString(", ",""),
                                                            "td"),"tr");
                                                }
                                            },""),
                                    "table"),"center")));
        }
    }
    
    /** Wrap a Team Name */
    private static String htmlTeam(String t){ return "Team '"+wrap(t,"i")+"'"; }
    /** Wrap a Team Name */
    
    private static String wrap(String s, String tag){ return "<"+tag+">"+s+"</"+tag+">"; }
    /** Default response to unknown requests */
    @Path()
    public HTTPResp defaultResp(HTTPReq req){
        return HTTPResp.htmlError(errorPage("Unaccepted URL Request:<br/><br/>"+
                "<span style='color:red'>"
                + req.trimmedUrl() + "</span>"));
    }

    /** Present an HTML Error Page */
    public static String errorPage(String msg){
        return htmlPage("!ERROR!", "div{ textalign:center;width:400px;height:200px;border:solid red 1px' }",
                wrap(wrap(wrap("ERROR<br/><br/>" + msg,"h3"),"div"),"center"));
    }

    /** Present an HTML Page */
    public static String htmlPage(String title, String style, String body){
        return wrap(wrap(wrap(title,"title")+wrap(style,"style"),"head")+wrap("\n"+body, "body"), "html");
    }

    /** Run the pre-registration server, until the "window" closes. */
    public List<TeamSpec> runPreRegistration(Option<Date> start) throws IOException{
        ServerThread server = Factory.create(this);
        log.event("Pre Registration Opened");
        if(start.isSome()){
            log.notify("Pre Reg will Close at: " + Util.printDate(start.inner()));
            Util.waitUntil(start.inner());
        }else{
            System.err.print("Hit Enter to Close PreReg: ");
            System.in.read();
        }
        server.shutdown();
        log.event("Pre Registration Closed");
        return teams;
    }

    /** Main Program for the Server. */
    public static void main(String[] argStr) throws Exception{
        List<String> args = List.create(argStr); 
        if(args.contains("--help") || args.isEmpty()){
            System.err.println(" ** Usage: java prereg.PreRegServer <Port> [RegCloseTime]");
            System.err.println(" Examples: java prereg.PreRegServer 7000 '9/15/2009 3:00:00 pm'");
            System.err.println("           java prereg.PreRegServer 7005");
            return;
        }
        int port = Integer.parseInt(args.top());
        PreRegServer server = PreRegServer.create(port,
                Logger.text(System.err,Util.logFileName("prereg")));
        server.runPreRegistration(args.length()>1?
                Option.some(Util.parseDate(args.lookup(1))):Option.<Date>none());
        System.err.println(" ******* DONE : " + Util.printDate(Util.now()) + " ************");
    }
}
