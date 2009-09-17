package scg;

public interface Constants{
    /** Path the RegServer Listens On */
    public static final String REG_PATH_ENTRY = "/register";
    /** Path the Player Listens On */
    public static final String PLAYER_PATH_ENTRY = "/player";
    /** Default PreRegistration Port */
    public static final int DEF_PREREG_PORT = 7000;
    /** Default Registration Port */
    public static final int DEF_REG_PORT = 7005;
    /** Used for automatic Player Address Registration, Pulled from the Socket
     *    over which registration is done. */
    public static final String REG_AUTO = "auto";
    
    
    // FILES
    /** Hashed Passwords File */
    public static final String PASS_FILE    = "files/passwords.pwd";
    /** Players File... the result of running standalone Registration */
    public static final String PLAYERS_FILE = "files/players.txt";
    /** Hashed Passwords File */
    public static final String PREPASS_FILE = "files/passwords.txt";
    /** Team Members File */
    public static final String PRETEAM_FILE = "files/teams.txt";

    /** Admin History output file prefix */
    public static final String HISTORY_FILE_PREFIX = "files/history/history";
    /** Plain text files... */
    public static final String HISTORY_FILE_SUFFIX = ".txt";
    /** Admin configuration file */
    public static final String CONFIG_FILE = "files/config.txt";
    
    
    /** Password Argument Name */
    public static final String PASS_URL_ARG = "password";

    /** Server HTTP Header Key*/
    public static final String SERVER_KEY = "Server";
    /** Admin's Reg/PreReg server name for HTTP headers */
    public static final String ADMIN_SERVER_NAME = "SCGAdmin";

}
