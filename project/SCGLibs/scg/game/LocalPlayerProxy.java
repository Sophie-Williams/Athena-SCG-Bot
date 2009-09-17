package scg.game;

import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;
import edu.neu.ccs.demeterf.http.server.ServerDispatch;

/** Supports the direct calling of Player handler methods */
public class LocalPlayerProxy extends PlayerProxy{
    /** The local Player ServerDispatch */
    private ServerDispatch dispatch;

    /** Create a LocalPlayerProxy from the PlayerSpec, and a Server Object/Handler */
    public LocalPlayerProxy(PlayerSpec spec, Object handler){
        super(spec);
        dispatch = ServerDispatch.create(handler);
    }

    /** Contact (dispatch to) the Player server directly */
    public HTTPResp contactPlayer(HTTPReq req){ return dispatch.handle(req); }
}
