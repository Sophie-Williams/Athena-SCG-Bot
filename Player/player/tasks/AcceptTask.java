package player.tasks;

import logging.Logger;
import scg.Util;
import scg.gen.AcceptTrans;
import scg.gen.OfferedChallenge;
import scg.gen.PlayerContext;
import edu.neu.ccs.demeterf.lib.List;

/** Accept Challenges we want to solve... choose wisely :) */
public class AcceptTask {

    private final Logger log;

    /** Create a AcceptTask with the given Logger */
    public AcceptTask(Logger l) {
        log = l;
    }

    /** Do the actual acceptances */
    public List<AcceptTrans> accept(PlayerContext context){
        log.notify("With Account: $"+context.getBalance());
        List<OfferedChallenge> chs = context.getTheirOffered();
        return chs.fold(new List.Fold<OfferedChallenge, Accepts>() {
            public Accepts fold(OfferedChallenge off, Accepts acc){
                if (!acc.shouldAccept(off)) {
                    log.notify("Not Accepting : #"+off.getKey());
                    return acc;
                }
                log.notify(":) Accepting : #"+off.getKey());
                return acc.doAccept(off);
            }
        }, Accepts.start(context.getBalance())).getTransactions();
    }

    /**
     * Keeps track of our account balance and what we've accepted so far.
     * Obviously more complicated schemes are possible (and suggested)
     */
    private static class Accepts {

        double account;
        List<AcceptTrans> toget;

        /** Private Constructor */
        private Accepts(double a, List<AcceptTrans> get) {
            account = a;
            toget = get;
        }

        /** Starts with the empty list of Accept Transactions */
        static Accepts start(double a){
            return create(a, List.<AcceptTrans> create());
        }

        /** Create an Accepts Instance */
        static Accepts create(double a, List<AcceptTrans> get){
            return new Accepts(a, get);
        }

        /** Should this OfferedChallenge be accepted? Change this to implement your
         *    Challenge selection choices. */
        boolean shouldAccept(OfferedChallenge off){
            return off.getPrice() < account && Util.coinFlip();
        }

        /** Do the 'accepting' of the Challenge */
        Accepts doAccept(OfferedChallenge off){
            return create(account - off.getPrice(), toget.push(new AcceptTrans(off.getKey())));
        }

        /** Return the accumulated Accept Transactions */
        public List<AcceptTrans> getTransactions(){ return toget; }
    }
}
