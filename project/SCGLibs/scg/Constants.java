package scg;

public interface Constants{
    /** Path the RegServer Listens On */
    public static final String REG_PATH_ENTRY = "/register";
    /** Path the Player Listens On */
    public static final String PLAYER_PATH_ENTRY = "/player";
    /** Registration Listening (incoming) Port */
    public static final int REG_PORT = 7000;
    /** Used for automatic Player Address Registration, Pulled from the Socket
     *    over which registration is done. */
    public static final String REG_AUTO = "auto";
    /** Hashed Passwords File */
    public static final String PASS_FILE = "files/passwords.pwd";
    /** Password Argument Name */
    public static final String PASS_URL_ARG = "password";

}
