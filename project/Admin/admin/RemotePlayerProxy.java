package admin;

import java.io.IOException;

import scg.game.PlayerProxy;
import scg.game.PlayerProxyI;
import scg.gen.PlayerSpec;
import edu.neu.ccs.demeterf.http.classes.HTTPReq;
import edu.neu.ccs.demeterf.http.classes.HTTPResp;

public class RemotePlayerProxy extends PlayerProxy implements PlayerProxyI {

    public RemotePlayerProxy(PlayerSpec playerSpec) {
        super(playerSpec);
    }

    @Override
    public HTTPResp contactPlayer(HTTPReq req) throws IOException{
        return req.send(getSpec().getAddress(), getSpec().getPort());
    }

}
