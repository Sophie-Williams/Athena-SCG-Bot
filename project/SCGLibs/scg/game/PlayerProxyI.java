package scg.game;

import scg.gen.PlayerContext;
import scg.gen.PlayerSpec;
import scg.gen.PlayerTrans;

/** An interface for Admin/Player communication */
public interface PlayerProxyI {

    /** Get the specification for the represented Player */
    PlayerSpec getSpec();

    /** Take the players turn */
    PlayerTrans takeTurn(PlayerContext currentPlayerContext);

}
