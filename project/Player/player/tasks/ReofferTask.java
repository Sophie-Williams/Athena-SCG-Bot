package player.tasks;

import logging.Logger;
import scg.gen.Config;
import scg.gen.OfferedChallenge;
import scg.gen.ReofferTrans;

/** Reoffer all of the other Players Challenges at lower prices */
public class ReofferTask {

    private final Logger log;

    /** Create a ReofferTask with the given Logger */
    public ReofferTask(Logger l) {
        log = l;
    }

    /** Do the actual price lowering */
    public ReofferTrans reoffer(OfferedChallenge ch, Config config){
        log.notify("Reoffer Task : #"+ch.getKey());
        return new ReofferTrans(ch.getKey(), Math.max(ch.getPrice() - config.getMindecr()-0.0001, 0));
    }
}
