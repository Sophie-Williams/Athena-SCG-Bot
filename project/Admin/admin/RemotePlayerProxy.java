package admin;

import java.io.IOException;

import scg.game.PlayerProxy;
import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

/** A remote proxy that implements distributed Admin/Player communication */
public class RemotePlayerProxy extends PlayerProxy{

    /** Create a proxy that sends requests over a socket to the Player */
    public RemotePlayerProxy(PlayerSpec playerSpec) {
        super(playerSpec);
    }

    /** Send the request over the wire */
    public HTTPResp contactPlayer(HTTPReq req){
        try{
            return req.send(getSpec().getAddress(), getSpec().getPort());
        }catch(IOException e){ throw new RuntimeException(e); }
    }

}
