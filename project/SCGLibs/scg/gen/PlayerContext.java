// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayerContext */
public class PlayerContext{
    protected final Config config;
    protected final PlayerID id;
    protected final double balance;
    protected final int currentRound;
    protected final List<OfferedChallenge> ourOffered;
    protected final List<OfferedChallenge> theirOffered;
    protected final List<AcceptedChallenge> accepted;
    protected final List<ProvidedChallenge> provided;

    /** Construct a(n) PlayerContext Instance */
    public PlayerContext(Config config, PlayerID id, double balance, int currentRound, List<OfferedChallenge> ourOffered, List<OfferedChallenge> theirOffered, List<AcceptedChallenge> accepted, List<ProvidedChallenge> provided){
        this.config = config;
        this.id = id;
        this.balance = balance;
        this.currentRound = currentRound;
        this.ourOffered = ourOffered;
        this.theirOffered = theirOffered;
        this.accepted = accepted;
        this.provided = provided;
    }
    /** Is the given object Equal to this PlayerContext? */
    public boolean equals(Object o){
        if(!(o instanceof PlayerContext))return false;
        if(o == this)return true;
        PlayerContext oo = (PlayerContext)o;
        return (((Object)config).equals(oo.config))&&(((Object)id).equals(oo.id))&&(((Object)balance).equals(oo.balance))&&(((Object)currentRound).equals(oo.currentRound))&&(((Object)ourOffered).equals(oo.ourOffered))&&(((Object)theirOffered).equals(oo.theirOffered))&&(((Object)accepted).equals(oo.accepted))&&(((Object)provided).equals(oo.provided));
    }
    /** Parse an instance of PlayerContext from the given String */
    public static PlayerContext parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayerContext();
    }
    /** Parse an instance of PlayerContext from the given Stream */
    public static PlayerContext parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerContext();
    }
    /** Parse an instance of PlayerContext from the given Reader */
    public static PlayerContext parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerContext();
    }

    /** Field Class for PlayerContext.config */
    public static class config extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.id */
    public static class id extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.balance */
    public static class balance extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.currentRound */
    public static class currentRound extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.ourOffered */
    public static class ourOffered extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.theirOffered */
    public static class theirOffered extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.accepted */
    public static class accepted extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerContext.provided */
    public static class provided extends edu.neu.ccs.demeterf.control.Fields.any{}

	public boolean isOverTime(){
	  return currentRound > config.getNumrounds();
	}
    /**  */
    public List<OfferedChallenge> getAllOffered(){ return ourOffered.append(theirOffered); }
    
    public void isLegal(PlayerTrans trans){
        boolean overTime = config.isOverTime(currentRound);
        if (!overTime) {
            acceptOrReoffer(trans);//Rule 2
        }
        offerNewChallenges(trans); //Rule 3
        provideAllAccepted(trans);//Rule 4
        provideCorrectProblems(trans);//Rule 4
        solveFromProvided(trans);//Rule 4
        checkReoffersDecrement(trans);//Rule 5
        acceptFromOthers(trans);//Rule 6*
        acceptAffordableChallenges(trans);//Rule 7
        reofferFromOthers(trans);//Rule 7
        challengerAffordsSolution(trans);//Rule 7
        currentPlayerResponded(trans);
    }

    private void reofferFromOthers(PlayerTrans trans){
        boolean allOffered = getTheirOffered().containsAllG(trans.getReofferTrans(),
                Config.<OfferedChallenge, ReofferTrans> getComp());
        if (!allOffered) {
            throw new RuntimeException("Reoffered a challenge not offered by others");
        }

    }

    private void checkReoffersDecrement(PlayerTrans trans){
        for (ReofferTrans rt : trans.getReofferTrans()) {
            double offeredPrice = findChallenge(getTheirOffered(), rt.getChallengeid()).getPrice();
            if (rt.getPrice() >= offeredPrice || rt.getPrice() > 0
                    && Util.lessThan(offeredPrice - rt.getPrice(), config.getMindecr())) {
                throw new RuntimeException("Reoffer by less than mindec (" + (offeredPrice - rt.getPrice()) + ")");
            }
        }
    }

    /*
     * TODO: This method doesn't make sense... The current player should be
     * *paid* when solving a challenge. The player who Offered is the one who
     * gets charged. Again we should check for negative balabces at the end of
     * the round, all in one place.
     */
    private void challengerAffordsSolution(PlayerTrans trans){
        double balance = getBalance();
        for (SolveTrans solTrans : trans.getSolvedTrans()) {
            Solution sol = solTrans.getSol();
            int challengeID = solTrans.getChallengeid();
            for (ProvidedChallenge challenge : getProvided()) {
                if (challenge.getKey() == challengeID) {
                    double quality = config.getObjective().value(challenge.getInstance(), sol);
                    double payoff = config.getObjective().calculatePayoff(quality,
                            config.getObjective().getDefaultQuality(challenge.getInstance()), challenge.getPrice(),
                            config.getProfitFactor());
                    balance -= payoff;
                    if (balance < 0) {
                        throw new RuntimeException("Challenger cannot affort solution");
                    }
                }
            }
        }
    }

    private void solveFromProvided(PlayerTrans trans){
        boolean allSolved = getProvided().sameG(trans.getSolvedTrans(),
                Config.<ProvidedChallenge, SolveTrans> getComp());
        if (!allSolved) {
            throw new RuntimeException("Solved and provided challenges don't match");
        }
    }

    private void provideAllAccepted(PlayerTrans trans){
        boolean same = getAccepted()
                .sameG(trans.getProvidedTrans(), Config.<AcceptedChallenge, ProvideTrans> getComp());
        if (!same) {
            throw new RuntimeException("Didn't provide all problems");
        }
    }

    private void provideCorrectProblems(PlayerTrans trans){
        for (ProvideTrans proTrans : trans.getProvidedTrans()) {
            Problem problem = proTrans.getInst();
            int challengeId = proTrans.getChallengeid();
            for (AcceptedChallenge challenge : getAccepted()) {
                if (challenge.getKey() == challengeId) {
                    if (!config.getPredicate().valid(problem, challenge.getPred())) {
                        throw new RuntimeException("Provided problem does not conform to challenge predicate");
                    }
                }
            }
        }
    }

    private void acceptOrReoffer(PlayerTrans trans){
        if (getTheirOffered().length() > 0) {
            int size = trans.getAcceptTrans().length() + trans.getReofferTrans().length();
            /* TODO: Should check these individually based on the Configuration */
            if (size <= 0) {
                throw new RuntimeException("Didn't buy nor reoffer");
            }
        }
    }

    private void acceptAffordableChallenges(final PlayerTrans trans){
        double sum = trans.getAcceptTrans().fold(new edu.neu.ccs.demeterf.lib.List.Fold<AcceptTrans, Double>() {

            @Override
            public Double fold(AcceptTrans trans, Double sum){
                int challengeID = trans.getChallengeid();

                /*
                 * TODO: Can we filter TheirOffered then fold, instead of
                 * running this loop everytime?
                 */
                for (Challenge challenge : getTheirOffered()) {
                    if (challenge.getKey() == challengeID) {
                        return sum + challenge.getPrice();
                    }
                }
                return sum;// will be caught by another check.
            }
        }, 0.0);
        /* TODO: I (BC) Think this is/was broken... */
        // if (sum > getId().getId()) {
        /* TODO: Here's what I think was meant */
        if (sum > getBalance()) {
            throw new RuntimeException("Insufficient funds");
        }
        /*
         * TODO: I think we should check for a positive Balance after the entire
         * round has been processed... not just one turn. They might be able to
         * make money on other players (solve) and also might lose money
         * (provide) so there are other ways to have insufficient funds. Better
         * to check for negative balance all in one place.
         */
    }

    private void acceptFromOthers(PlayerTrans trans){
        boolean allOffered = getTheirOffered().containsAllG(trans.getAcceptTrans(),
                Config.<OfferedChallenge, AcceptTrans> getComp());
        if (!allOffered) {
            throw new RuntimeException("Accepted a challenge not offered by others");
        }

    }

    /** Offer challenges not in store */
    private void offerNewChallenges(PlayerTrans trans){
        boolean notFresh = getAllOffered().containsAnyG(trans.getOfferTrans(),
                new edu.neu.ccs.demeterf.lib.List.GComp<OfferedChallenge, OfferTrans>() {

                    @Override
                    public boolean comp(OfferedChallenge ch, OfferTrans trans){
                        return ch.getPred().equals(trans.getPred());
                    }
                });
        if (notFresh) {
            throw new RuntimeException("Challenge already in store");
        }
    }

    /** Check that the correct player has responded */
    private void currentPlayerResponded(PlayerTrans trans){
        if (!getId().equals(trans.getId())) {
            throw new RuntimeException("Incorrect Player Responded");
        }
    }

    /** Find the given Challenge ID in the given List */
    static <CH extends Challenge> CH findChallenge(edu.neu.ccs.demeterf.lib.List<CH> lst, final int chID){
        return lst.find(new edu.neu.ccs.demeterf.lib.List.Pred<CH>() {

            @Override
            public boolean huh(CH ch){
                return ch.getKey() == chID;
            }
        });
    }
    

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayerContext.provided */
    public List<ProvidedChallenge> getProvided(){ return provided; }
    /** Getter for field PlayerContext.accepted */
    public List<AcceptedChallenge> getAccepted(){ return accepted; }
    /** Getter for field PlayerContext.theirOffered */
    public List<OfferedChallenge> getTheirOffered(){ return theirOffered; }
    /** Getter for field PlayerContext.ourOffered */
    public List<OfferedChallenge> getOurOffered(){ return ourOffered; }
    /** Getter for field PlayerContext.currentRound */
    public int getCurrentRound(){ return currentRound; }
    /** Getter for field PlayerContext.balance */
    public double getBalance(){ return balance; }
    /** Getter for field PlayerContext.id */
    public PlayerID getId(){ return id; }
    /** Getter for field PlayerContext.config */
    public Config getConfig(){ return config; }

}


