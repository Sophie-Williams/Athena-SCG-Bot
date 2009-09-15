package scg.game;

import java.lang.reflect.Method;

import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

public class LocalPlayerProxy extends PlayerProxy implements PlayerProxyI {

    private final Object player;
    private Method method = null;

    public LocalPlayerProxy(PlayerSpec spec, Object player) {
        super(spec);
        this.player = player;
        if (player != null) {
            try {
                this.method = player.getClass().getMethod("playerResponse", HTTPReq.class);
            } catch (Exception e) {
                throw new RuntimeException("incorrect player type");
            }
        }

    }

    @Override
    public HTTPResp contactPlayer(HTTPReq req) throws Exception{
        if (player != null && method != null) {
            return (HTTPResp) method.invoke(player, req);
        }
        throw new RuntimeException("Player unable to take turn");
    }
}
