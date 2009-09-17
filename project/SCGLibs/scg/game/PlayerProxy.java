package scg.game;

import scg.Constants;
import scg.gen.PlayerContext;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

/** An extra level of indirection supporting both distributed and direct Admin/Player
 *    interactions */
public abstract class PlayerProxy implements PlayerProxyI {
    /** The Player we are a proxy for */
    private final PlayerSpec spec;

    /**  Create a Proxy for the given PlayerSpec */
    public PlayerProxy(PlayerSpec sp){ spec = sp; }

    /** Return the PlayerSpec this proxy represents */
    public PlayerSpec getSpec(){ return spec; }

    /** Take the Player turn by contacting the player and getting its response */
    public PlayerTrans takeTurn(PlayerContext currentPlayerContext){
        try{
            HTTPReq req = HTTPReq.Post(Constants.PLAYER_PATH_ENTRY, currentPlayerContext.toString());
            HTTPResp res = contactPlayer(req);
            if (res.isError()) {
                throw new RuntimeException("HTTP Error: " + res.getBodyString());
            }
            PlayerTrans trans = PlayerTrans.parse(res.getBodyString());
            return trans;
        }catch(Exception e){ throw new RuntimeException(e); }
    }
    
    /** The go-between method: Request -> Reqponse */
    public abstract HTTPResp contactPlayer(HTTPReq req);
}
