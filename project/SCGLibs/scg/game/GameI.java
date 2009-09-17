package scg.game;

import scg.gen.AcceptTrans;
import scg.gen.OfferTrans;
import scg.gen.ProvideTrans;
import scg.gen.ReofferTrans;
import scg.gen.SolveTrans;

/** Interface representation of GameI */
public interface GameI {

    /** Handle an offer challenge transaction */
    void installTransaction(int challengerID, OfferTrans ot);

    /** Handle an accept challenge transaction */
    void installTransaction(int challengeeID, AcceptTrans at);

    /** Handle Provide Transactions */
    void installTransaction(int challengerID, ProvideTrans pt);

    /** Handle Solve Transactions */
    void installTransaction(int challengeeID, SolveTrans st);

    /** Handle Reoffer Transactions */
    void installTransaction(int newChallenger, ReofferTrans rt);

}
