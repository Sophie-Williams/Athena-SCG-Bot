package reg;

import edu.neu.ccs.demeterf.http.classes.*;
import scg.gen.PlayerSpec;

/** Test the Player Registration */
public class RegTest {

    public static void main(String[] args) throws Exception{
        /*
        System.err.println("'"+RegServer.encrypt("ABCDE")+"'");
        System.err.println("'"+RegServer.encrypt("12345")+"'");
        System.err.println("'"+RegServer.encrypt("VWXYZ")+"'");
        /**/
        
        HTTPResp res = HTTPReq.Post("/register?password=ABCDE",
                "" + new PlayerSpec("Team Bryan", "auto", 9000)).send(
                "pacman.ccs.neu.edu", RegServer.PORT);
        System.out.println(" RESP:\n" + res);
    }
}
