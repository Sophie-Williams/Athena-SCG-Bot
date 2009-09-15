package scg.game;

import scg.gen.PlayerContext;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;

public interface PlayerProxyI {

    PlayerSpec getSpec();

    PlayerTrans takeTurn(PlayerContext currentPlayerContext) throws Exception;

}
