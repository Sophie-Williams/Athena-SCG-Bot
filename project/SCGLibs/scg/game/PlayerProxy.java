package scg.game;

import scg.Constants;
import scg.gen.PlayerContext;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

public abstract class PlayerProxy implements PlayerProxyI {

    private final PlayerSpec spec;

    public PlayerProxy(PlayerSpec spec) {
        this.spec = spec;
    }

    public PlayerSpec getSpec(){
        return spec;
    }

    public PlayerTrans takeTurn(PlayerContext currentPlayerContext) throws Exception{
        HTTPReq req = HTTPReq.Post(Constants.PLAYER_PATH_ENTRY, currentPlayerContext.toString());
        HTTPResp res = contactPlayer(req);
        if (res.isError()) {
            throw new RuntimeException("HTTP Error: " + res.getBodyString());
        }
        PlayerTrans trans = PlayerTrans.parse(res.getBodyString());
        return trans;
    }

    public abstract HTTPResp contactPlayer(HTTPReq req) throws Exception;

}
