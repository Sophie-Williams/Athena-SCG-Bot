package player.tasks;

import logging.Logger;
import scg.Util;
import scg.gen.OfferTrans;
import scg.gen.OfferedChallenge;
import scg.gen.PlayerContext;
import scg.gen.ProblemType;
import edu.neu.ccs.demeterf.lib.List;

/** Offer new Challenges, at good prices, that are not currently offered. */
public class OfferTask {

    private final Logger log;

    /** Create a OfferTask with the given Logger */
    public OfferTask(Logger l) {
        log = l;
    }

    /** Do the actual creation/offering of new Challenges */
    public List<OfferTrans> offer(PlayerContext context){
        final int MAX_OFFERS = 2; /* Should be in Config */
        return create(MAX_OFFERS, context.getAllOffered().map(new List.Map<OfferedChallenge, ProblemType>() {
            public ProblemType map(OfferedChallenge off){
                return off.getPred();
            }
        })).map(new List.Map<ProblemType, OfferTrans>() {
            public OfferTrans map(ProblemType t){
                log.notify("Offering : "+t);
                return new OfferTrans(t, Util.random());
            }
        });
    }

    public List<ProblemType> create(int many, List<ProblemType> ts){
        if (many == 0) {
            return List.create();
        }
        List<ProblemType> newts = create(many - 1, ts);
        return newts.push(freshPred(newts.push(ts)));
    }

    public static ProblemType freshPred(List<ProblemType> ts){
        ProblemType pt;
        do {
            pt = ProblemType.random();
        } while (ts.contains(pt));
        return pt;
    }
}
