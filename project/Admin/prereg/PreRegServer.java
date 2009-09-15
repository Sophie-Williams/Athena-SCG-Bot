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
import scg.gen.ParseException;
import scg.gen.PasswordEntry;
import scg.gen.PasswordFile;
import scg.gen.TeamFile;
import scg.gen.TeamSpec;
import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.lib.Map;

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
    /** Admins Listening (incoming) Port */
    @Port()
    public static final int PORT = 7002;
    /** Hashed Passwords File */
    private static final String PASS_FILE = "files/passwords.txt";
    /** Team Members File */
    private static final String TEAM_FILE = "files/teams.txt";

    /** Form Arugument Names */
    private static final String FORM_TEAM = "teamname";
    private static final String FORM_PASS = "pass";
    private static final String FORM_MEMBERS = "members";

    /** Currently registered Teams */
    private List<TeamSpec> teams = List.create();
    /** Logger for the Registration Process */
    private final Logger log;

    /**
     * Create a PreRegServer with the given Teams. Pre-registration sets
     * Player/Team Passwords for future competitions.
     */
    private PreRegServer(List<TeamSpec> tms, Logger l) {
        teams = tms;
        log = l;
    }

    /**  */
    public static PreRegServer create(List<TeamSpec> tms, Logger l){
        return new PreRegServer(tms, l);
    }

    /**  */
    public static PreRegServer create(Logger l) throws IOException, ParseException{
        File teamFile = new File(TEAM_FILE);
        if(!teamFile.canWrite())
            teamFile.createNewFile();
        List<TeamSpec> ts = TeamFile.parse(new FileInputStream(TEAM_FILE)).getTeams();
        return new PreRegServer(ts, l);
    }

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
                    return HTTPResp.htmlError(errorPage(log.error(htmlTeam(newSpec.getName()) + " Already Exists")));
                }
                teams = teams.push(newSpec);
                log.event("Flushing Teams/Password Files");
                try {
                    new TeamFile(teams).write(TEAM_FILE);
                    log.event("Teams File Flushed");
                    new PasswordFile(teams.map(new List.Map<TeamSpec, PasswordEntry>() {

                        @Override
                        public PasswordEntry map(TeamSpec s){
                            return new PasswordEntry(s.getName(), s.getPasshash());
                        }
                    })).write(PASS_FILE);
                    log.event("Password File Flushed");
                } catch (Exception e) {
                    log.error("Unable to Save one of the files");
                }
                return HTTPResp.htmlResponse(htmlPage("Success", "<center><div style='textalign:center;width:"
                        + "400px;height:200px;border:solid blue 1px'><h1>Success</h1><br/><br/><h3>"
                        + log.event(htmlTeam(newSpec.getName()) + " is Registered") + "</h3></div></center>"));
            }
            return HTTPResp.htmlError(errorPage(log.error("Missing Form Arguments")));
        }
    }

    /** Main URL handler for the RegServer */
    @Path(STATUS)
    public HTTPResp statusResp(HTTPReq req, Socket sock){
        log.event("Status Request from: "+sock.getInetAddress());
        synchronized (teams) {
            return HTTPResp.htmlResponse(
                    wrap(wrap(wrap(wrap(
                            wrap("PreReg Status","title")+
                            wrap("table,td,th{ padding:5px;border:1px solid blue; }","style"),"head")+
                            wrap("Registered Teams","h3")+
                            wrap(wrap(
                                    wrap("#","th")+
                                    wrap("Name","th")+
                                    wrap("Hash","th")+
                                    wrap("Members","th"),"tr")+
                                    teams.fold(new List.Fold<TeamSpec, String>(){
                                        int count = 0;
                                        public String fold(TeamSpec t, String r){
                                            return wrap(
                                                    wrap(""+(++count),"td")+
                                                    wrap(t.getName(),"td")+
                                                    wrap(t.getPasshash(),"td")+
                                                    wrap(t.getPlayers().toString(", ",""),
                                                        "td"),"tr");
                                        }
                                    },""),
                            "table"),"center"),"body"),"html"));
        }
    }
    
    /** Wrap a Team Name */
    private String htmlTeam(String t){
        return "Team '"+wrap(t,"i")+"'";
    }
    /** Wrap a Team Name */
    private String wrap(String s, String tag){
        return "<"+tag+">"+s+"</"+tag+">";
    }
    @Path()
    public HTTPResp defaultResp(HTTPReq req){
        return HTTPResp.htmlError(errorPage("Unaccepted URL Request:<br/><br/><span style='color:red'>"
                + req.trimmedUrl() + "</span>"));
    }

    /** Present an HTML Error Page */
    public static String errorPage(String msg){
        return htmlPage("!ERROR!", "<center><div style='textalign:center;width:400px;height:"
                + "200px;border:solid red 1px'><h3>ERROR<br/><br/>" + msg + "</h3></div>");
    }

    /** Present an HTML Page */
    public static String htmlPage(String title, String body){
        return "<html><head><title>" + title + "</title></head><body>\n" + "\n" + body + "\n</body></html>";
    }

    /** Run the pre-registration server, until the "window" closes. */
    public List<TeamSpec> runPreRegistration(String startDate) throws IOException, java.text.ParseException{
        return runPreRegistration(Util.parseDate(startDate));
    }

    /** Run the pre-registration server, until the "window" closes. */
    public List<TeamSpec> runPreRegistration(Date start) throws IOException{
        ServerThread server = Factory.create(this);
        log.event("Pre Registration Opened");
        log.notify("Pre Reg will Close at: " + Util.printDate(start));
        Util.waitUntil(start);
        server.shutdown();
        log.event("Pre Registration Closed");
        return teams;
    }

    /** Main Program for the Server. */
    public static void main(String[] args) throws Exception{
        if(args.length != 1) {
            System.err.println(" !! Usage: java PreRegServer <RegClose>");
            return;
        }
        String start = args.length > 0 ? args[0] : "9/15/2009 3:00:00 pm";
        PreRegServer server = PreRegServer.create(Logger.text(System.err, Util.logFileName("prereg")));
        System.err.println("\nPre Registered Teams:\n" + server.runPreRegistration(start).toString());
        System.err.println("\n ******* DONE : " + Util.printDate(Util.now()) + " ************");
    }
}
